package de.uol.swp.common.lobby.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der KickPlayerException
 *
 * @author Timo
 * @since Sprint 10
 */
public class KickPlayerExceptionTest {

    /**
     * Testet die KickPlayerException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void KickPlayerExceptionTest() {
        try {
            throw new KickPlayerException("KickPlayerException");
        } catch (KickPlayerException exp) {
            String msg = "KickPlayerException";
            assertEquals(KickPlayerException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}
