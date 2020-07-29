package de.uol.swp.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der GameManagementException
 *
 * @author Timo
 * @since Sprint 10
 */
public class SecurityExceptionTest {

    /**
     * Testet die GameManagementException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void SecurityExceptionTest() {
        try {
            throw new SecurityException("SecurityException");
        } catch (SecurityException exp) {
            String msg = "SecurityException";
            assertEquals(SecurityException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}