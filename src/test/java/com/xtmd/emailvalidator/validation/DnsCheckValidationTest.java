package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.reason.DomainAcceptsNoMail;
import com.xtmd.emailvalidator.result.reason.LocalOrReservedDomain;
import com.xtmd.emailvalidator.result.reason.NoDNSRecord;
import com.xtmd.emailvalidator.result.reason.UnableToGetDNSRecord;
import com.xtmd.emailvalidator.warning.NoDNSMXRecord;
import com.xtmd.emailvalidator.warning.Warning;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DnsCheckValidationTest {

    @ParameterizedTest
    @MethodSource("validEmailsProvider")
    void testValidDNS(String validEmail) {
        DNSCheckValidation validation = new DNSCheckValidation();
        assertTrue(validation.isValid(validEmail, new EmailLexer()));
    }

    @Test
    void testInvalidDNS() {
        DNSCheckValidation validation = new DNSCheckValidation();
        assertFalse(validation.isValid("example@invalid.example.com", new EmailLexer()));
    }

    @ParameterizedTest
    @MethodSource("localOrReservedEmailsProvider")
    void testLocalOrReservedDomainError(String localOrReservedEmail) {
        DNSCheckValidation validation = new DNSCheckValidation();
        validation.isValid(localOrReservedEmail, new EmailLexer());

        InvalidEmail err = validation.getError();
        assertNotNull(err, "Expected an InvalidEmail error");
        assertInstanceOf(LocalOrReservedDomain.class, err.getReason(),
                "Expected LocalOrReservedDomain, got " + err.getReason().getClass().getSimpleName());
    }

    @Test
    void testDomainAcceptsNoMailError() {
        DNSCheckValidation validation = new DNSCheckValidation();
        boolean ok = validation.isValid("nullmx@example.com",  new EmailLexer());
        assertFalse(ok);

        InvalidEmail err = validation.getError();
        assertNotNull(err);
        assertInstanceOf(DomainAcceptsNoMail.class, err.getReason(),
                "Expected DomainAcceptsNoMail, got " + err.getReason().getClass().getSimpleName());
    }

    @Test
    void testDNSWarnings() {
        DNSCheckValidation validation = new DNSCheckValidation();
        validation.isValid("example@invalid.example.com", new EmailLexer());

        List<Warning> warnings = validation.getWarnings();
        assertTrue(
                warnings.stream().anyMatch(w -> w instanceof NoDNSMXRecord),
                "Expected NoDNSMXRecord warning"
        );
    }

    @Test
    void testNoDNSError() {
        DNSCheckValidation validation = new DNSCheckValidation();
        validation.isValid("example@invalid.example.com", new EmailLexer());

        InvalidEmail err = validation.getError();
        assertNotNull(err);
        assertInstanceOf(NoDNSRecord.class, err.getReason(),
                "Expected NoDNSRecord, got " + err.getReason().getClass().getSimpleName());
    }

    @Test
    void testUnableToGetDNSRecord() {
        DNSGetRecordWrapper wrapper = new DNSGetRecordWrapper() {
            @Override
            public DNSRecords getRecords(String host, int type) {
                return new DNSRecords(List.of(), true);
            }
        };

        DNSCheckValidation validation = new DNSCheckValidation(wrapper);
        boolean ok = validation.isValid("example@invalid.example.com", new EmailLexer());
        assertFalse(ok);

        InvalidEmail err = validation.getError();
        assertNotNull(err);
        assertInstanceOf(UnableToGetDNSRecord.class, err.getReason(),
                "Expected UnableToGetDNSRecord, got " + err.getReason().getClass().getSimpleName());
    }

    @Test
    void testMissingTypeKey() {
        DNSGetRecordWrapper wrapper = new DNSGetRecordWrapper() {
            @Override
            public DNSRecords getRecords(String host, int type) {
                return new DNSRecords(List.of(Map.of("host", "test")), false);
            }
        };

        DNSCheckValidation validation = new  DNSCheckValidation(wrapper);
        boolean ok = validation.isValid("example@invalid.example.com", new EmailLexer());
        assertFalse(ok);

        InvalidEmail err = validation.getError();
        assertNotNull(err);
        assertInstanceOf(NoDNSRecord.class, err.getReason(),
                "Expected NoDNSRecord, got " + err.getReason().getClass().getSimpleName());
    }

    static Stream<String> validEmailsProvider() {
        return Stream.of(
                // dot-atom
                "Abc@ietf.org",
                "Abc@fake.ietf.org",
                "ABC@ietf.org",
                "Abc.123@ietf.org",
                "user+mailbox/department=shipping@ietf.org",
                "!#$%&'*+-/=?^_`.{|}~@ietf.org",

                // quoted string
                "\"Abc@def\"@ietf.org",
                "\"Fred\\ Bloggs\"@ietf.org",
                "\"Joe.\\\\Blow\"@ietf.org",

                // unicode
                "info@ñandu.cl",
                "ñandu@ñandu.cl"
        );
    }

    static Stream<String> localOrReservedEmailsProvider() {
        return Stream.of(
                // Reserved Top Level DNS Names
                "test",
                "example",
                "invalid",
                "localhost",

                // mDNS
                "local",

                // Private DNS Namespaces
                "intranet",
                "internal",
                "private",
                "corp",
                "home",
                "lan"
        );
    }
}
