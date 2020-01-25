package de.uol.swp.server.chatmanagement;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatManagementTest {

    static final User chatMember = new UserDTO("Keno", "Keno", "Keno@OG.com");

    final EventBus bus = new EventBus();
    final ChatManagement chatManagement = new ChatManagement();

    @Test
    void getChat() {
        //Create a global Chat
        chatManagement.createChat("global");
        assertTrue(chatManagement.getChat("global").isPresent());
        assertNotNull(chatManagement.getChat("global").get());
    }

    @Test
    void createChat() {
        String newChat = chatManagement.createChat();
        assertTrue(chatManagement.getChat(newChat).isPresent());
        assertNotNull(chatManagement.getChat(newChat).get());
        assertNotNull(chatManagement.getChat(newChat));
    }

    @Test
    void deleteChat() {
        String newChat = chatManagement.createChat();
        chatManagement.deleteChat(newChat);
        assertTrue(chatManagement.getChat(newChat).isEmpty());
    }

    @Test
    void addMessage() {
        //Create a global Chat
        chatManagement.createChat("global");
        chatManagement.addMessage("global", new ChatMessage(chatMember, "Test"));
        assertEquals("Test", chatManagement.getChat("global").get().getMessages().get(0).getMessage());
    }
}