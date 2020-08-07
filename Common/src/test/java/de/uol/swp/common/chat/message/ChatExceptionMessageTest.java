package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testet die ChatExceptionMessage
 *
 * @author Rike
 * @since Sprint10
 */
public class ChatExceptionMessageTest {

    private static final User defaultUser = new UserDTO("Herbert", "1234", "herbert@muster.de");
    private static final ChatException testChatException = new ChatException("Test");

    @Test
    public void createChatExceptionMessage() {
        ChatExceptionMessage msg = new ChatExceptionMessage(defaultUser, testChatException);
        assertEquals(defaultUser, msg.getSender());
        assertEquals(testChatException, msg.getException());
    }
}
