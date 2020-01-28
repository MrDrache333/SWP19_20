package de.uol.swp.server.chatmanagement;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.chat.ChatService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatServiceTest {

    static final User chatMember = new UserDTO("Keno", "Keno", "Keno@OG.com");

    final EventBus bus = new EventBus();
    final ChatManagement chatManagement = new ChatManagement();
    final ChatService userService = new ChatService(bus, chatManagement);


    /**
     * 1:
     * Erstellt GlobalChat; überprüft Erstellung
     * 2:
     * Erstellt Chatnachricht und die entsprechende Request; postet die Request; überprüft ob sie ankommt
     * 3:
     * Erstellt LobbyChat, dann wie 2
     *
     * @author Keno O.
     * @since Sprint 1
     */
    @Test
    void onNewChatMessageRequest() {
        //1:
        chatManagement.createChat("global");
        assertNotNull(chatManagement.getChat("global"));
        //2:
        NewChatMessageRequest request = new NewChatMessageRequest(new ChatMessage(chatMember, "Test"));
        bus.post(request);
        assertEquals("Test", chatManagement.getChat("global").getMessages().get(0).getMessage());
        //3:
        String newChatId = chatManagement.createChat();
        request = new NewChatMessageRequest(newChatId, new ChatMessage(chatMember, "Test"));
        bus.post(request);
        assertEquals("Test", chatManagement.getChat(newChatId).getMessages().get(0).getMessage());
    }
}