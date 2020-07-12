package de.uol.swp.server.usermanagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der UserManagementException
 *
 * @author Timo
 * @since Sprint 10
 */
public class UserManagementExceptionTest {

    /**
     * Testet die UserManagementException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void UserManagementExceptionTest() {
        try {
            throw new UserManagementException("UserManagementException");
        } catch (UserManagementException exp) {
            String msg = "UserManagementException";
            assertEquals(UserManagementException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}
