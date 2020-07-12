package de.uol.swp.common.lobby.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der SetMaxPlayerException
 *
 * @author Timo
 * @since Sprint 10
 */
public class SetMaxPlayerExceptionTest {

    /**
     * Testet die SetMaxPlayerException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void GameManagementExceptionTest() {
        try {
            throw new SetMaxPlayerException("SetMaxPlayerException");
        } catch (SetMaxPlayerException exp) {
            String msg = "SetMaxPlayerException";
            assertEquals(SetMaxPlayerException.class, exp.getClass());
            Assertions.assertEquals(msg, exp.getMessage());
        }
    }
}
