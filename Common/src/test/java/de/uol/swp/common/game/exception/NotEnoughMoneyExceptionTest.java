package de.uol.swp.common.game.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der NotEnoughMoneyException
 *
 * @author Timo
 * @since Sprint 10
 */
public class NotEnoughMoneyExceptionTest {

    /**
     * Testet die NotEnoughMoneyException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void NotEnoughMoneyExceptionTest() {
        try {
            throw new NotEnoughMoneyException("NotEnoughMoneyException");
        } catch (NotEnoughMoneyException exp) {
            String msg = "NotEnoughMoneyException";
            assertEquals(NotEnoughMoneyException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }

    /**
     * Testet die NotEnoughMoneyException ohne Message
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void NotEnoughMoneyExceptionTestWithOutMessage() {
        try {
            throw new NotEnoughMoneyException();
        } catch (NotEnoughMoneyException exp) {
            assertEquals(NotEnoughMoneyException.class, exp.getClass());
        }
    }
}
