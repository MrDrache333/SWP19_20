package de.uol.swp.server.chatmanagement;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.chat.ChatService;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatServiceTest {

    static final User chatMember = new UserDTO("Keno", "Keno", "Keno@OG.com");

    final EventBus bus = new EventBus();
    final ChatManagement chatManagement = new ChatManagement();
    final UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    final LobbyManagement lobbyManagement = new LobbyManagement();

    final AuthenticationService authenticationService = new AuthenticationService(bus, userManagement, lobbyManagement);
    final ChatService userService = new ChatService(bus, chatManagement, authenticationService);


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
        if (chatManagement.getChat("global").isEmpty()) chatManagement.createChat("global");
        assertNotNull(chatManagement.getChat("global"));
        //2:
        NewChatMessageRequest request = new NewChatMessageRequest(new ChatMessage(chatMember, "Test"));
        bus.post(request);
        assertEquals("Test", chatManagement.getChat("global").get().getMessages().get(0).getMessage());
        //3:
        String newChatId = chatManagement.createChat();
        request = new NewChatMessageRequest(newChatId, new ChatMessage(chatMember, "Test"));
        bus.post(request);
        assertEquals("Test", chatManagement.getChat(newChatId).get().getMessages().get(0).getMessage());
    }
}