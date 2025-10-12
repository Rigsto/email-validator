package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.dummy.AnotherDummyReason;
import io.github.rigsto.emailvalidator.dummy.DummyReason;
import io.github.rigsto.emailvalidator.exception.EmptyValidationList;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.MultipleErrors;
import io.github.rigsto.emailvalidator.result.reason.Reason;
import io.github.rigsto.emailvalidator.warning.AddressLiteral;
import io.github.rigsto.emailvalidator.warning.DomainLiteral;
import io.github.rigsto.emailvalidator.warning.Warning;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleValidationWithAndTest {

    static final class StubValidation implements EmailValidation {
        boolean result;
        List<Warning> warnings = new ArrayList<>();
        InvalidEmail error;

        StubValidation(boolean result) {
            this.result = result;
        }

        @Override
        public boolean isValid(String email, EmailLexer emailLexer) {
            return result;
        }

        @Override
        public InvalidEmail getError() {
            return error;
        }

        @Override
        public List<Warning> getWarnings() {
            return warnings;
        }
    }

    @Test
    void testUsesAndLogicalOperation() throws EmptyValidationList {
        InvalidEmail invalid = new InvalidEmail(new DummyReason(), "");

        EmailLexer lexer = new EmailLexer();
        StubValidation vTrue = new StubValidation(true);
        StubValidation vFalse = new StubValidation(false);
        vTrue.error = invalid;

        MultipleValidationWithAnd mv = new MultipleValidationWithAnd(List.of(vTrue, vFalse));
        assertFalse(mv.isValid("exmpale@example.com", lexer));
    }

    @Test
    void testEmptyListIsNotAllowed() {
        assertThrows(EmptyValidationList.class, () -> new MultipleValidationWithAnd(List.of()));
    }

    @Test
    void testValidationIsValid() throws EmptyValidationList {
        EmailLexer lexer = new EmailLexer();
        StubValidation v = new StubValidation(true);

        MultipleValidationWithAnd mv = new MultipleValidationWithAnd(List.of(v));
        assertTrue(mv.isValid("example@example.com", lexer));
        assertNull(mv.getError());
    }

    @Test
    void testAccumulatesWarnings() throws EmptyValidationList {
        InvalidEmail invalid = new InvalidEmail(new DummyReason(), "");
        EmailLexer lexer = new EmailLexer();

        StubValidation v1 = new StubValidation(true);
        v1.warnings.add(new AddressLiteral());
        v1.error = invalid;

        StubValidation v2 = new StubValidation(false);
        v2.warnings.add(new DomainLiteral());
        v2.error = invalid;

        MultipleValidationWithAnd mv = new MultipleValidationWithAnd(List.of(v1, v2));
        mv.isValid("example@example.com", lexer);

        List<Warning> warnings = mv.getWarnings();
        assertTrue(warnings.stream().anyMatch(w -> w instanceof AddressLiteral));
        assertTrue(warnings.stream().anyMatch(w -> w instanceof DomainLiteral));
    }

    @Test
    void testGathersAllTheErrors() throws EmptyValidationList {
        InvalidEmail e1 = new InvalidEmail(new DummyReason(), "");
        InvalidEmail e2 = new InvalidEmail(new AnotherDummyReason(), "");

        EmailLexer lexer = new EmailLexer();
        StubValidation v1 = new StubValidation(false);
        v1.error = e1;
        StubValidation v2 = new StubValidation(false);
        v2.error = e2;

        MultipleValidationWithAnd mv = new MultipleValidationWithAnd(List.of(v1, v2));
        mv.isValid("example@example.com", lexer);

        assertInstanceOf(MultipleErrors.class, mv.getError());

        List<Reason> reasons = ((MultipleErrors) mv.getError()).getReasons();
        assertTrue(reasons.stream().anyMatch(r -> r.getClass().getSimpleName().equals("DummyReason")));
        assertTrue(reasons.stream().anyMatch(r -> r.getClass().getSimpleName().equals("AnotherDummyReason")));
    }

    @Test
    void testStopsAfterFirstError() throws EmptyValidationList {
        InvalidEmail e1 = new InvalidEmail(new DummyReason(), "");
        InvalidEmail e2 = new InvalidEmail(new AnotherDummyReason(), "");

        EmailLexer lexer = new EmailLexer();
        StubValidation v1 = new StubValidation(false);
        v1.error = e1;

        final boolean[] called = { false };
        EmailValidation v2 = new EmailValidation() {
            @Override public boolean isValid(String email, EmailLexer emailLexer) { called[0] = true; return false; }
            @Override public InvalidEmail getError() { return e2; }
            @Override public List<Warning> getWarnings() { return List.of(); }
        };

        MultipleValidationWithAnd mv = new MultipleValidationWithAnd(List.of(v1, v2), MultipleValidationWithAnd.STOP_ON_ERROR);
        mv.isValid("example@example.com", lexer);

        assertInstanceOf(MultipleErrors.class, mv.getError());

        List<Reason> reasons = ((MultipleErrors) mv.getError()).getReasons();
        assertEquals(1, reasons.size());
        assertEquals("DummyReason", reasons.get(0).getClass().getSimpleName());
        assertFalse(called[0], "Second validation should not have been called");
    }

    @Test
    void testBreakOutOfLoopWhenError() throws EmptyValidationList {
        InvalidEmail e1 = new InvalidEmail(new DummyReason(), "");
        EmailLexer lexer = new EmailLexer();

        StubValidation v1 = new StubValidation(false);
        v1.error = e1;

        final boolean[] called = { false };
        EmailValidation v2 = new EmailValidation() {
            @Override public boolean isValid(String email, EmailLexer emailLexer) { called[0] = true; return true; }
            @Override public InvalidEmail getError() { return null; }
            @Override public List<Warning> getWarnings() { return List.of(); }
        };

        MultipleValidationWithAnd mv = new MultipleValidationWithAnd(List.of(v1, v2), MultipleValidationWithAnd.STOP_ON_ERROR);
        mv.isValid("example@example.com", lexer);

        assertInstanceOf(MultipleErrors.class, mv.getError());
        assertFalse(called[0], "Second validation should not have been called");
    }

    @Test
    void testBreakoutOnInvalidEmail() throws EmptyValidationList {
        EmailLexer lexer = new EmailLexer();

        final boolean[] called = { false };
        EmailValidation neverCalled = new EmailValidation() {
            @Override public boolean isValid(String email, EmailLexer emailLexer) { called[0] = true; return true; }
            @Override public InvalidEmail getError() { return null; }
            @Override public List<Warning> getWarnings() { return List.of(); }
        };

        MultipleValidationWithAnd mv = new MultipleValidationWithAnd(
                List.of(
                        new RFCValidation(),
                        neverCalled
                ),
                MultipleValidationWithAnd.STOP_ON_ERROR
        );

        assertFalse(mv.isValid("invalid-email", lexer));
        assertFalse(called[0], "Trailing validation should not run when RFCValidation fails.");
    }
}
