package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testet die NewChatMessage
 *
 * @author Rike
 * @since Sprint 10
 */

public class NewChatMessageTest {

    private static final User defaultUser = new UserDTO("Herbert", "1234", "herbert@muster.de");
    private static final ChatMessage chatMessage = new ChatMessage(defaultUser, "Test");

    @Test
    public void createNewChatMessage() {
        NewChatMessage msg = new NewChatMessage("1234", chatMessage);
        assertEquals(msg.getChatId(), "1234");
        assertEquals(msg.getMessage(), chatMessage);
    }
}
