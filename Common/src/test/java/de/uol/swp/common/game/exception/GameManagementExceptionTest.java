package de.uol.swp.common.game.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der GameManagementException
 *
 * @author Timo
 * @since Sprint 10
 */
public class GameManagementExceptionTest {

    /**
     * Testet die GameManagementException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void GameManagementExceptionTest() {
        try {
            throw new GameManagementException("GameManagementException");
        } catch (GameManagementException exp) {
            String msg = "GameManagementException";
            assertEquals(GameManagementException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}
