package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.lexer.Token;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.CRNoLF;
import io.github.rigsto.emailvalidator.result.reason.ExpectingDTEXT;
import io.github.rigsto.emailvalidator.result.reason.UnusualElements;
import io.github.rigsto.emailvalidator.warning.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

/**
 * Parser for domain literals in email addresses.
 * <p>
 * Domain literals are enclosed in square brackets and can contain
 * IPv4 addresses, IPv6 addresses, or other address formats. This parser
 * validates the syntax and structure of domain literals according to
 * RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DomainLiteral extends PartParser {
    /**
     * Regular expression pattern for validating IPv4 addresses.
     */
    public static final Pattern IPV4_REGEX = Pattern.compile(
            "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
                    + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    /**
     * List of token types that generate obsolete warnings in domain literals.
     */
    public static final List<Integer> OBSOLETE_WARNINGS = Arrays.asList(INVALID, C_DEL, S_LF, S_BACKSLASH);

    /**
     * Creates a new DomainLiteral parser with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public DomainLiteral(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Parses a domain literal enclosed in square brackets.
     * <p>
     * Validates the syntax of domain literals, checks for IPv4/IPv6 addresses,
     * and generates appropriate warnings for deprecated or unusual elements.
     * </p>
     * 
     * @return ValidEmail if the domain literal is valid, InvalidEmail otherwise
     */
    @Override
    public Result parse() {
        addTagWarnings();

        boolean ipv6Tag = false;
        StringBuilder addressLiteral = new StringBuilder();

        do {
            if (this.lexer.current.isA(C_NUL)) {
                return new InvalidEmail(new ExpectingDTEXT(), this.lexer.current.value);
            }

            addObsoleteWarnings();

            if (this.lexer.isNextToken(S_OPENBRACKET)) {
                return new InvalidEmail(new ExpectingDTEXT(), this.lexer.current.value);
            }

            if (this.lexer.isNextTokenAny(Arrays.asList(S_HTAB, S_SP, CRLF))) {
                this.warnings.add(new CFWSWithFWS());
                parseFWS();
            }

            if (this.lexer.isNextToken(S_CR)) {
                return new InvalidEmail(new CRNoLF(), this.lexer.current.value);
            }

            if (this.lexer.current.isA(S_BACKSLASH)) {
                return new InvalidEmail(new UnusualElements(this.lexer.current.value), this.lexer.current.value);
            }

            if (this.lexer.current.isA(S_IPV6TAG)) {
                ipv6Tag = true;
            }

            if (this.lexer.current.isA(S_CLOSEBRACKET)) {
                break;
            }

            addressLiteral.append(this.lexer.current.value);
        } while (this.lexer.moveNext());

        String addr = addressLiteral.toString().replace("[", "");

        boolean continueParsing = checkIPv4Tag(addr);
        if (!continueParsing) {
            return new ValidEmail();
        }

        addr = convertIPv4ToIPv6(addr);

        if (!ipv6Tag) {
            this.warnings.add(new io.github.rigsto.emailvalidator.warning.DomainLiteral());
            return new ValidEmail();
        }

        this.warnings.add(new AddressLiteral());

        checkIPv6Tag(addr, 8);
        return new ValidEmail();
    }

    /**
     * Converts IPv4 addresses to IPv6 format within domain literals.
     * <p>
     * This method handles the conversion of IPv4 addresses to IPv6 format
     * as specified in RFC standards for domain literals.
     * </p>
     * 
     * @param addressLiteralIPv4 the IPv4 address literal to convert
     * @return the converted IPv6 format address literal
     */
    public String convertIPv4ToIPv6(String addressLiteralIPv4) {
        Matcher matcher = IPV4_REGEX.matcher(addressLiteralIPv4);

        if (matcher.find()) {
            String matched =  matcher.group();
            int index = addressLiteralIPv4.lastIndexOf(matched);

            if (index > 0) {
                return addressLiteralIPv4.substring(0, index) + "0:0";
            }
        }

        return addressLiteralIPv4;
    }

    /**
     * Validates IPv6 address format and generates appropriate warnings.
     * <p>
     * Checks IPv6 address syntax, group counts, double colons, and other
     * IPv6-specific validation rules.
     * </p>
     * 
     * @param addressLiteral the IPv6 address literal to validate
     * @param maxGroups the maximum number of groups allowed
     */
    public void checkIPv6Tag(String addressLiteral, int maxGroups) {
        Token<Integer, String> prev = this.lexer.getPrevious();
        if (prev.isA(S_COLON)) {
            this.warnings.add(new IPV6ColonEnd());
        }

        if (addressLiteral.length() < 5) return;
        String ipv6 = addressLiteral.substring(5);

        String[] groups = ipv6.split(":", -1);
        int groupCount = groups.length;
        int firstDoubleColon = ipv6.indexOf("::");

        boolean hasBad = false;
        for (String group : groups) {
            if (!group.matches("^[0-9A-Fa-f]{0,4}$")) {
                hasBad = true;
                break;
            }
        }

        if (hasBad) {
            this.warnings.add(new IPV6BadChar());
        }

        if (firstDoubleColon == -1) {
            if (groupCount != maxGroups) {
                this.warnings.add(new IPV6GroupCount());
            }
            return;
        }

        if (firstDoubleColon != ipv6.lastIndexOf("::")) {
            this.warnings.add(new IPV6DoubleColon());
            return;
        }

        if (firstDoubleColon == 0 || firstDoubleColon == ipv6.length() - 2) {
            maxGroups++;
        }

        if (groupCount > maxGroups) {
            this.warnings.add(new IPV6MaxGroups());
        } else if (groupCount == maxGroups) {
            this.warnings.add(new IPV6Deprecated());
        }
    }

    /**
     * Checks if the given address literal contains a valid IPv4 address.
     * 
     * @param addressLiteral the address literal to check
     * @return true if the address literal contains a valid IPv4 address, false otherwise
     */
    protected boolean checkIPv4Tag(String addressLiteral) {
        Matcher matcher = IPV4_REGEX.matcher(addressLiteral);

        if (matcher.find()) {
            String matched = matcher.group();

            if (addressLiteral.startsWith(matched)) {
                this.warnings.add(new AddressLiteral());
                return false;
            }
        }
        return true;
    }

    private void addObsoleteWarnings() {
        if (OBSOLETE_WARNINGS.contains(this.lexer.current.type)) {
            this.warnings.add(new ObsoleteDTEXT());
        }
    }

    private void addTagWarnings() {
        if (this.lexer.isNextToken(S_COLON)) {
            this.warnings.add(new IPV6ColonStart());
        }

        if (this.lexer.isNextToken(S_IPV6TAG)) {
            Token<Integer, String> token = lexer.peek();
            lexer.resetPeek();

            if (token != null && token.isA(S_DOUBLECOLON)) {
                this.warnings.add(new IPV6ColonStart());
            }
        }
    }
}
