package de.uol.swp.server.game.phase;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class BuyPhaseTest {

        static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
        static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
        static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
        static final EventBus bus = new EventBus();
        static final ChatManagement chatManagement = new ChatManagement();
        static final LobbyManagement lobbyManagement = new LobbyManagement();
        static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
        static final AuthenticationService authenticationService = new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore()));

        static UUID gameID;

        @BeforeAll
        static void init() {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            lobbyManagement.getLobby(gameID).get().joinUser(secondPlayer);
            lobbyManagement.getLobby(gameID).get().joinUser(thirdPlayer);
            bus.post(new StartGameInternalMessage(gameID));
        }

}
