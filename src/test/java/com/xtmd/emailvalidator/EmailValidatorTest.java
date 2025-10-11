package com.xtmd.emailvalidator;

import com.xtmd.emailvalidator.dummy.DummyReason;
import com.xtmd.emailvalidator.exception.EmptyValidationList;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.validation.EmailValidation;
import com.xtmd.emailvalidator.validation.MultipleValidationWithAnd;
import com.xtmd.emailvalidator.warning.Warning;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorTest {

    static class StubValidation implements EmailValidation {
        boolean isValid;
        List<Warning> warns = List.of();
        InvalidEmail err;

        StubValidation(boolean ok) {
            isValid = ok;
        }

        @Override
        public boolean isValid(String email, EmailLexer emailLexer) {
            return isValid;
        }

        @Override
        public InvalidEmail getError() {
            return err;
        }

        @Override
        public List<Warning> getWarnings() {
            return warns;
        }
    }

    @Test
    void testValidationIsUsed() {
        InvalidEmail invalidEmail = new InvalidEmail(new DummyReason(), "");
        EmailValidator validator = new EmailValidator();

        StubValidation v = new StubValidation(true);
        v.warns = List.of();
        v.err = invalidEmail;

        assertTrue(validator.isValid("example@example.com", v));
    }

    @Test
    void testMultipleValidation() throws EmptyValidationList {
        EmailValidator validator = new EmailValidator();

        StubValidation v = new StubValidation(true);
        v.warns = List.of();

        MultipleValidationWithAnd multiple = new MultipleValidationWithAnd(List.of(v));
        assertTrue(validator.isValid("example@example.com", multiple));
    }

    @Test
    void testValidationIsFalse() {
        InvalidEmail invalidEmail = new InvalidEmail(new DummyReason(), "");
        EmailValidator validator = new EmailValidator();

        StubValidation v = new StubValidation(false);
        v.warns = List.of();
        v.err = invalidEmail;

        assertFalse(validator.isValid("example@example.com", v));
        assertFalse(validator.hasWarnings());
        assertEquals(List.of(), validator.getWarnings());
        assertSame(invalidEmail, validator.getError());
    }
}
