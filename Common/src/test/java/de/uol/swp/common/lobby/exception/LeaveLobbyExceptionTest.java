package de.uol.swp.common.lobby.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der LeaveLobbyException
 *
 * @author Timo
 * @since Sprint 10
 */
public class LeaveLobbyExceptionTest {

    /**
     * Testet die GameManagementException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void LeaveLobbyExceptionTest() {
        try {
            throw new LeaveLobbyException("LeaveLobbyException");
        } catch (LeaveLobbyException exp) {
            String msg = "LeaveLobbyException";
            assertEquals(LeaveLobbyException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}
