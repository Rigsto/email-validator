package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.reason.*;
import io.github.rigsto.emailvalidator.warning.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RFCValidationTest {

    RFCValidation validator;
    EmailLexer lexer;

    @BeforeEach
    public void setUp() {
        validator = new RFCValidation();
        lexer = new EmailLexer();
    }

    @AfterEach
    public void tearDown() {
        validator = null;
    }

    @ParameterizedTest
    @MethodSource("validEmails")
    void testValidEmails(String email) {
        assertTrue(validator.isValid(email, lexer));
    }

    @ParameterizedTest
    @MethodSource("validEmailsWithWarnings")
    void testValidEmailsWithWarningsCheck(String email, Warning[] expected) {
        assertTrue(validator.isValid(email, lexer));

        List<Warning> warns = validator.getWarnings();
        assertEquals(expected.length, warns.size());

        List<Integer> codesExpected = Arrays.stream(expected).map(Warning::code).sorted().toList();
        List<Integer> codesActual = warns.stream().map(Warning::code).sorted().toList();
        assertEquals(codesExpected, codesActual);
    }

    @Test
    void testInvalidUTF8Email() {
        String email = "\u0080\u0081\u0082@\u0083\u0084\u0085.\u0086\u0087\u0088";
        assertFalse(validator.isValid(email, lexer));
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void testInvalidEmails(Object email) {
        assertFalse(validator.isValid(String.valueOf(email), lexer));
    }

    @ParameterizedTest
    @MethodSource("invalidEmailsWithErrors")
    void testInvalidEmailsWithErrorsCheck(InvalidEmail expected, String email) {
        assertFalse(validator.isValid(email, lexer));

        InvalidEmail actual = validator.getError();
        assertNotNull(actual);
        assertEquals(expected.getReason().getClass(), actual.getReason().getClass());
    }

    static Stream<String> validEmails() {
        return Stream.of(
                "â@iana.org",
                "fabien@symfony.com",
                "example@example.co.uk",
                "fabien_potencier@example.fr",
                "fab'ien@symfony.com",
                "fab\\ ien@symfony.com",
                "example((example))@fakedfake.co.uk",
                "fabien+a@symfony.com",
                "exampl=e@example.com",
                "инфо@письмо.рф",
                "\"username\"@example.com",
                "\"user,name\"@example.com",
                "\"user name\"@example.com",
                "\"user@name\"@example.com",
                "\"user\\\"name\"@example.com",
                "\"\\a\"@iana.org",
                "\"test\\ test\"@iana.org",
                "\"\"@iana.org",
                "\"\\\"\"@iana.org",
                "müller@möller.de",
                "1500111@профи-инвест.рф",
                String.format("example@%s.com", "ъ".repeat(40))
        );
    }

    static Stream<Arguments> validEmailsWithWarnings() {
        return Stream.of(
                Arguments.of(
                        "a5aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com",
                        new Warning[]{ new LocalTooLong() }
                ),
                Arguments.of(
                        "example@example",
                        new Warning[]{ new TLD() }
                ),
                Arguments.of(
                        "example @invalid.example.com",
                        new Warning[]{ new CFWSNearAt() }
                ),
                Arguments.of(
                        "example(examplecomment)@invalid.example.com",
                        new Warning[]{ new Comment(), new CFWSNearAt() }
                ),
                Arguments.of(
                        "\"\t\"@invalid.example.com",
                        new Warning[]{ new QuotedString("", "\""), new CFWSWithFWS() }
                ),
                Arguments.of(
                        "\"\r\"@invalid.example.com",
                        new Warning[]{ new QuotedString("", "\""), new CFWSWithFWS() }
                ),
                Arguments.of(
                        "\"example\"@invalid.example.com",
                        new Warning[]{ new QuotedString("", "\"") }
                ),
                Arguments.of(
                        "too_long_localpart_too_long_localpart_too_long_localpart_too_long_localpart@invalid.example.com",
                        new Warning[]{ new LocalTooLong() }
                )
        );
    }

    static Stream<Object> invalidEmails() {
        return Stream.of(
                "user  name@example.com",
                "user   name@example.com",
                "example.@example.co.uk",
                "example@example@example.co.uk",
                "(test_exampel@example.fr",
                "example(example]example@example.co.uk",
                ".example@localhost",
                "ex\\ample@localhost",
                "user name@example.com",
                "usern,ame@example.com",
                "user[na]me@example.com",
                "\"\"\"@iana.org",
                "\"\\\"@iana.org",
                "\"test\"test@iana.org",
                "\"test\"\"test\"@iana.org",
                "\"test\".\"test\"@iana.org",
                "\"test\".test@iana.org",
                "\"test" + (char)0 + "\"@iana.org",
                "\"test\\\"@iana.org",
                (char)226 + "@iana.org",
                "\\r\\ntest@iana.org",
                "\\r\\n test@iana.org",
                "\\r\\n \\r\\ntest@iana.org",
                "\\r\\n \\r\\ntest@iana.org",
                "\\r\\n \\r\\n test@iana.org",
                "test;123@foobar.com",
                "examp║le@symfony.com",
                "example@invalid-.domain.com",
                "example@-invalid.com",
                "0",
                0
        );
    }

    static Stream<Arguments> invalidEmailsWithErrors() {
        return Stream.of(
                Arguments.of(new InvalidEmail(new NoLocalPart(), "@"), "@example.co.uk"),
                Arguments.of(new InvalidEmail(new ConsecutiveDot(), "."), "example..example@example.co.uk"),
                Arguments.of(new InvalidEmail(new ExpectingATEXT("Invalid token found"), "<"), "<example_example>@example.fr"),
                Arguments.of(new InvalidEmail(new DotAtStart(), "."), ".example@localhost"),
                Arguments.of(new InvalidEmail(new DotAtEnd(), "."), "example.@example.co.uk"),
                Arguments.of(new InvalidEmail(new UnclosedComment(), "("), "(example@localhost"),
                Arguments.of(new InvalidEmail(new UnclosedQuotedString(), "\""), "\"example@localhost"),
                Arguments.of(
                        new InvalidEmail(
                                new ExpectingATEXT("https://tools.ietf.org/html/rfc5322#section-3.2.4 - quoted string should be a unit"),
                                "\""
                        ),
                        "exa\"mple@localhost"
                ),
                Arguments.of(new InvalidEmail(new UnopenedComment(), ")"), "comment)example@localhost"),
                Arguments.of(new InvalidEmail(new UnopenedComment(), ")"), "example(comment))@localhost"),
                Arguments.of(new InvalidEmail(new AtextAfterCFWS(), "\n"), "exampl\ne@example.co.uk"),
                Arguments.of(new InvalidEmail(new AtextAfterCFWS(), "\t"), "exampl\te@example.co.uk"),
                Arguments.of(new InvalidEmail(new CRNoLF(), "\r"), "exam\rple@example.co.uk")
        );
    }
}
