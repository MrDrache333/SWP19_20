package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SkipPhaseMessageTest {
    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final EventBus bus = new EventBus();
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final AuthenticationService authenticationService = new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore()));
    static final GameService gameService = new GameService(bus, gameManagement, authenticationService);
    static UUID gameID;
    static final Phase.Type startPhase = gameManagement.getGame(gameID).get().getPlayground().getActualPhase();

    @BeforeAll
    static void init() {
        gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
        chatManagement.createChat(gameID.toString());
        lobbyManagement.getLobby(gameID).get().joinUser(secondPlayer);
        lobbyManagement.getLobby(gameID).get().joinUser(thirdPlayer);
        bus.post(new SkipPhaseRequest(defaultOwner, gameID));
    }

    /**
     * Testet ob das skippen einer Phase funktioniert.
     *
     * @author Devin
     * @since Sprint6
     */
    @Test
    void testSkipPhase() {
        GameService gameService = new GameService(bus, gameManagement, authenticationService);
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        Phase.Type endPhase = playground.getActualPhase();
        Boolean successTest;
        successTest = (startPhase == Phase.Type.ActionPhase && endPhase == Phase.Type.Buyphase) || (startPhase == Phase.Type.Buyphase && endPhase == Phase.Type.Clearphase) || (startPhase == Phase.Type.Clearphase && endPhase == Phase.Type.ActionPhase);
        assertEquals(false, successTest);
    }

}