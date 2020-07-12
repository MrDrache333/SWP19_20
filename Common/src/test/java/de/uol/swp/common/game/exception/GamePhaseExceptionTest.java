package de.uol.swp.common.game.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der GamePhaseException
 *
 * @author Timo
 * @since Sprint 10
 */
public class GamePhaseExceptionTest {

    /**
     * Testet die GamePhaseException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void GamePhaseExceptionTest() {
        try {
            throw new GamePhaseException("GamePhaseException");
        } catch (GamePhaseException exp) {
            String msg = "GamePhaseException";
            assertEquals(GamePhaseException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}