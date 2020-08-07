package de.uol.swp.server.usermanagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der UserUpdateException
 *
 * @author Timo
 * @since Sprint 10
 */
public class UserUpdateExceptionTest {

    /**
     * Testet die UserUpdateException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void UserUpdateExceptionTest() {
        try {
            throw new UserUpdateException("UserUpdateException");
        } catch (UserUpdateException exp) {
            String msg = "UserUpdateException";
            assertEquals(UserUpdateException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}