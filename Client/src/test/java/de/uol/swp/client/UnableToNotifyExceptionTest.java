package de.uol.swp.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der UnableToNotifyException
 *
 * @author Timo
 * @since Sprint 10
 */
public class UnableToNotifyExceptionTest {

    /**
     * Testet die UnableToNotifyException inklusive Throwable
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void UnableToNotifyExceptionTest() {
        try {
            throw new UnableToNotifyException("UnableToNotifyException", new Throwable("Throwable 1"));
        } catch (UnableToNotifyException exp) {
            String msgOne = "UnableToNotifyException";
            String msgTwo = "Throwable 1";
            assertEquals(UnableToNotifyException.class, exp.getClass());
            assertEquals(msgOne, exp.getMessage());
            assertEquals(msgTwo, exp.getCause().getMessage());
        }
    }
}
