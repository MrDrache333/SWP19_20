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


    @Test
    void onNewChatMessageRequest() {

        //Create a global Chat
        assertNotNull(chatManagement.getChat("global"));

        //Test Global Chat
        NewChatMessageRequest request = new NewChatMessageRequest(new ChatMessage(chatMember, "Test"));
        bus.post(request);
        assertEquals("Test", chatManagement.getChat("global").get().getMessages().get(0).getMessage());

        //Test Lobby Chat
        String newChatId = chatManagement.createChat();
        request = new NewChatMessageRequest(newChatId, new ChatMessage(chatMember, "Test"));
        bus.post(request);
        assertEquals("Test", chatManagement.getChat(newChatId).get().getMessages().get(0).getMessage());

    }
}