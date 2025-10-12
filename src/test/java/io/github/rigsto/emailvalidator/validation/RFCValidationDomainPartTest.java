package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.reason.*;
import com.xtmd.emailvalidator.warning.*;
import io.github.rigsto.emailvalidator.result.reason.*;
import io.github.rigsto.emailvalidator.warning.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RFCValidationDomainPartTest {

    RFCValidation validator;
    EmailLexer lexer;

    @BeforeEach
    public void setup() {
        validator = new RFCValidation();
        lexer = new EmailLexer();
    }

    @AfterEach
    public void teardown() {
        validator = null;
    }

    @ParameterizedTest
    @MethodSource("validEmails")
    void testValidEmails(String email) {
        assertTrue(validator.isValid(email, lexer));
    }
    
    @ParameterizedTest
    @MethodSource("invalidEmails")
    void testInvalidEmails(String email) {
        assertFalse(validator.isValid(email, lexer));
    }

    @ParameterizedTest
    @MethodSource("validEmailsWithWarnings")
    void testValidEmailsWithWarningsCheck(String email, List<Warning> expectedWarnings) {
        assertTrue(validator.isValid(email, lexer));

        List<Warning> warns = validator.getWarnings();
        assertEquals(expectedWarnings.size(), warns.size());

        List<Integer> expectedCodes = expectedWarnings.stream().map(Warning::code).sorted().toList();
        List<Integer> actualCodes = warns.stream().map(Warning::code).sorted().toList();
        assertEquals(expectedCodes, actualCodes);
    }

    @ParameterizedTest
    @MethodSource("invalidEmailsWithErrors")
    void testInvalidEmailsWithErrorsCheck(InvalidEmail expected, String email) {
        assertFalse(validator.isValid(email, lexer));

        InvalidEmail actual = validator.getError();
        assertNotNull(actual);
        assertEquals(expected.getReason().getClass(), actual.getReason().getClass());
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("invalidUTF16Chars")
    void testInvalidUTF16(String email) {
        assertFalse(validator.isValid(email, lexer));
    }

    static Stream<String> validEmails() {
        return Stream.of(
                "fabien@symfony.com",
                "example@example.co.uk",
                "example@localhost",
                "example@faked(fake).co.uk",
                "инфо@письмо.рф",
                "müller@möller.de",
                "1500111@профи-инвест.рф",
                "validipv6@[IPv6:2001:db8:1ff::a0b:dbd0]",
                "validipv4@[127.0.0.0]",
                "validipv4@127.0.0.0",
                "withhyphen@domain-exam.com",
                "valid_long_domain@71846jnrsoj91yfhc18rkbrf90ue3onl8y46js38kae8inz0t1.5a-xdycuau.na49.le.example.com"
        );
    }

    static Stream<String> invalidEmails() {
        return Stream.of(
                "test@example.com test",
                "example@example@example.co.uk",
                "test_exampel@example.fr]",
                "example@local\\host",
                "example@localhost\\",
                "example@localhost.",
                "username@ example . com",
                "username@ example.com",
                "example@(fake].com",
                "example@(fake.com",
                "username@example,com",
                "test@" + (char)226 + ".org",
                "test@iana.org \r\n",
                "test@iana.org \r\n ",
                "test@iana.org \r\n \r\n",
                "test@iana.org \r\n\r\n",
                "test@iana.org  \r\n\r\n ",
                "test@iana/icann.org",
                "test@foo;bar.com",
                "test@example..com",
                "test@examp'le.com",
                "email.email@email.\"",
                "test@email>",
                "test@email<",
                "test@email{",
                "username@examp,le.com",
                "test@ ",
                "invalidipv4@[127.\0.0.0]",
                "test@example.com []",
                "test@example.com. []",
                "test@test. example.com",
                (
                        "example@toolonglocalparttoolonglocalparttoolonglocalparttoolonglocalparttoolonglocalparttoolonglocal" +
                                "parttoolonglocalparttoolonglocalparttoolonglocalparttoolonglocalparttoolonglocalparttoolonglocalpart" +
                                "toolonglocalparttoolonglocalparttoolonglocalparttoolonglocalpar"
                ),
                "example@toolonglocalparttoolonglocalparttoolonglocalparttoolonglocalpart.co.uk",
                "example@toolonglocalparttoolonglocalparttoolonglocalparttoolonglocalpart.test.co.uk",
                "example@test.toolonglocalparttoolonglocalparttoolonglocalparttoolonglocalpart.co.uk",
                "test@email*a.com",
                "test@email!a.com",
                "test@email&a.com",
                "test@email^a.com",
                "test@email%a.com",
                "test@email$a.com",
                "test@email`a.com",
                "test@email|a.com",
                "test@email~a.com",
                "test@email{a.com",
                "test@email}a.com",
                "test@email=a.com",
                "test@email+a.com",
                "test@email_a.com",
                "test@email¡a.com",
                "test@email?a.com",
                "test@email#a.com",
                "test@email¨a.com",
                "test@email€a.com",
                "test@email$a.com",
                "test@email£a.com"
        );
    }

    static Stream<Arguments> invalidEmailsWithErrors() {
        return Stream.of(
                Arguments.of(new InvalidEmail(new NoDomainPart(), ""), "example@"),
                Arguments.of(new InvalidEmail(new DomainHyphened("Hypen found near DOT"), "-"), "example@example-.co.uk"),
                Arguments.of(new InvalidEmail(new CRNoLF(), "\r"), "example@example\r.com"),
                Arguments.of(new InvalidEmail(new DomainHyphened("Hypen found at the end of the domain"), "-"), "example@example-"),
                Arguments.of(new InvalidEmail(new ConsecutiveAt(), "@"), "example@@example.co.uk"),
                Arguments.of(new InvalidEmail(new ConsecutiveDot(), "."), "example@example..co.uk"),
                Arguments.of(new InvalidEmail(new DotAtStart(), "."), "example@.localhost"),
                Arguments.of(new InvalidEmail(new DomainHyphened("After AT"), "-"), "example@-localhost"),
                Arguments.of(new InvalidEmail(new DotAtEnd(), ""), "example@localhost."),
                Arguments.of(new InvalidEmail(new UnopenedComment(), ")"), "example@comment)localhost"),
                Arguments.of(new InvalidEmail(new UnopenedComment(), ")"), "example@localhost(comment))"),
                Arguments.of(new InvalidEmail(new UnopenedComment(), "com"), "example@(comment))example.com"),
                Arguments.of(new InvalidEmail(new ExpectingDTEXT(), "["), "example@[[]"),
                Arguments.of(new InvalidEmail(new CRNoLF(), "\r"), "example@exa\rmple.co.uk"),
                Arguments.of(new InvalidEmail(new CRNoLF(), "["), "example@[\\r]"),
                Arguments.of(new InvalidEmail(new ExpectingATEXT("Invalid token in domain: ,"), ","), "example@exam,ple.com"),
                Arguments.of(new InvalidEmail(new ExpectingATEXT("Invalid token in domain: '"), "'"), "test@example.com'"),
                Arguments.of(new InvalidEmail(new LabelTooLong(), "."), String.format("example@%s.com", "ъ".repeat(64))),
                Arguments.of(new InvalidEmail(new LabelTooLong(), "."), String.format("example@%s.com", "a4t".repeat(22))),
                Arguments.of(new InvalidEmail(new LabelTooLong(), ""), String.format("example@%s", "a4t".repeat(22)))
        );
    }

    static Stream<Arguments> validEmailsWithWarnings() {
        return Stream.of(
                Arguments.of(
                        "example@invalid.example(examplecomment).com", List.of(new Comment(), new CFWSNearAt())
                ),
                Arguments.of(
                        "example@[127.0.0.1]", List.of(new AddressLiteral(), new TLD())
                ),
                Arguments.of(
                        "example@[IPv6:2001:0db8:85a3:0000:0000:8a2e:0370:7334]", List.of(new AddressLiteral(), new TLD())
                ),
                Arguments.of(
                        "example@[IPv6:2001:0db8:85a3:0000:0000:8a2e:0370::]", List.of(new AddressLiteral(), new IPV6Deprecated(), new TLD())
                ),
                Arguments.of(
                        "example@[IPv6:2001:0db8:85a3:0000:0000:8a2e:0370:7334::]", List.of(new AddressLiteral(), new IPV6MaxGroups(), new TLD())
                ),
                Arguments.of(
                        "example@[IPv6:1::1::1]", List.of(new AddressLiteral(), new IPV6DoubleColon(), new TLD())
                ),
                Arguments.of(
                        "example@[\n]", List.of(new ObsoleteDTEXT(), new DomainLiteral(), new TLD())
                ),
                Arguments.of(
                        "example@[::1]", List.of(new DomainLiteral(), new TLD())
                ),
                Arguments.of(
                        "example@[::123.45.67.178]", List.of(new DomainLiteral(), new TLD())
                ),
                Arguments.of(
                        "example@[IPv6::2001:0db8:85a3:0000:0000:8a2e:0370:7334]", List.of(new IPV6ColonStart(), new AddressLiteral(), new IPV6GroupCount(), new TLD())
                ),
                Arguments.of(
                        "example@[IPv6:z001:0db8:85a3:0000:0000:8a2e:0370:7334]", List.of(new AddressLiteral(), new IPV6BadChar(), new TLD())
                ),
                Arguments.of(
                        "example@[IPv6:2001:0db8:85a3:0000:0000:8a2e:0370:]", List.of(new AddressLiteral(), new IPV6ColonEnd(), new TLD())
                )
        );
    }

    static Stream<String> invalidUTF16Chars() {
        return Stream.of("example@symƒony.com");
    }
}
