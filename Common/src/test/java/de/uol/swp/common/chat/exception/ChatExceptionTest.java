package de.uol.swp.common.chat.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse der ChatException
 *
 * @author Timo
 * @since Sprint 10
 */
public class ChatExceptionTest {

    /**
     * Testet die ChatException
     *
     * @author Timo
     * @since Sprint 10
     */
    @Test
    public void ChatExceptionTest() {
        try {
            throw new ChatException("ChatException");
        } catch (ChatException exp) {
            String msg = "ChatException";
            assertEquals(ChatException.class, exp.getClass());
            assertEquals(msg, exp.getMessage());
        }
    }
}