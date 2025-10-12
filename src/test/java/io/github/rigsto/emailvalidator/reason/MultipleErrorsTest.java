package io.github.rigsto.emailvalidator.reason;

import io.github.rigsto.emailvalidator.dummy.AnotherDummyReason;
import io.github.rigsto.emailvalidator.dummy.DummyReason;
import io.github.rigsto.emailvalidator.result.MultipleErrors;
import io.github.rigsto.emailvalidator.result.reason.EmptyReason;
import io.github.rigsto.emailvalidator.result.reason.Reason;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultipleErrorsTest {

    @Test
    void testRegisterSameReason() {
        Reason error1 = new DummyReason();
        Reason error2 = new DummyReason();

        MultipleErrors multiError = new MultipleErrors();
        multiError.addReason(error1);
        multiError.addReason(error2);

        assertEquals(1, multiError.getReasons().size());
    }

    @Test
    void testRegisterDifferentReasons() {
        Reason error1 = new DummyReason();
        Reason error2 = new AnotherDummyReason();

        String expectedReason = error1.description() + System.lineSeparator() + error2.description();

        MultipleErrors multiError = new MultipleErrors();
        multiError.addReason(error1);
        multiError.addReason(error2);

        assertEquals(2, multiError.getReasons().size());
        assertEquals(expectedReason, multiError.description());
        assertEquals(error1, multiError.getReason());
    }

    @Test
    void testRetrieveFirstReasonWithReasonCodeEqualsZero() {
        Reason error1 = new DummyReason();

        MultipleErrors multiError = new MultipleErrors();
        multiError.addReason(error1);

        assertEquals(error1, multiError.getReason());
    }

    @Test
    void testRetrieveFirstReasonWithReasonCodeDistinctToZero() {
        Reason error1 = new AnotherDummyReason();

        MultipleErrors multiError = new MultipleErrors();
        multiError.addReason(error1);

        assertEquals(error1, multiError.getReason());
    }

    @Test
    void testRetrieveFirstReasonWithNoReasonAdded() {
        Reason emptyReason = new EmptyReason();
        MultipleErrors multipleErrors = new MultipleErrors();

        assertEquals(emptyReason.code(), multipleErrors.getReason().code());
    }
}
