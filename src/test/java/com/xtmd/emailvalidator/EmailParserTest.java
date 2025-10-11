package com.xtmd.emailvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailParserTest {

    @ParameterizedTest
    @MethodSource("emailPartsProvider")
    void testGetParts(String email, String local, String domain) {
        EmailParser parser = new EmailParser(new EmailLexer());
        parser.parse(email);

        assertEquals(local, parser.getLocalPart());
        assertEquals(domain, parser.getDomainPart());
    }

    @Test
    void MultipleEmailAddresses() {
        EmailParser parser = new EmailParser(new EmailLexer());
        parser.parse("some-local-part@some-random-but-large-domain-part.example.com");

        assertEquals("some-local-part", parser.getLocalPart());
        assertEquals("some-random-but-large-domain-part.example.com", parser.getDomainPart());

        parser.parse("another-local-part@another.example.com");

        assertEquals("another-local-part", parser.getLocalPart());
        assertEquals("another.example.com", parser.getDomainPart());
    }

    static Stream<Arguments> emailPartsProvider() {
        return Stream.of(
                Arguments.of("test@foo.com", "test", "foo.com"),
                Arguments.of("\"user@name\"@example.com", "\"user@name\"", "example.com"),
                Arguments.of("validipv6@[IPv6:2001:db8:1ff::a0b:dbd0]", "validipv6", "[IPv6:2001:db8:1ff::a0b:dbd0]"),
                Arguments.of("validipv4@[127.0.0.0]", "validipv4", "[127.0.0.0]")
        );
    }
}
