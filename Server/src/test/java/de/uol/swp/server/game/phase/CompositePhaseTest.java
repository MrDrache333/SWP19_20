package de.uol.swp.server.game.phase;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"OptionalGetWithoutIsPresent", "UnstableApiUsage"})
class CompositePhaseTest {
    private static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    private static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    private static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    private static final EventBus bus = new EventBus();
    private static final ChatManagement chatManagement = new ChatManagement();
    private static final LobbyManagement lobbyManagement = new LobbyManagement();
    private static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    private static final AuthenticationService authenticationService = new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore()), lobbyManagement);
    @SuppressWarnings("unused")
    private static final GameService gameService = new GameService(bus, gameManagement, authenticationService);
    private static ArrayList<Short> chosenCards = new ArrayList<>();

    private static UUID gameID;

    @BeforeAll
    static void init() {
        gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
        chatManagement.createChat(gameID.toString());
        lobbyManagement.getLobby(gameID).get().joinUser(secondPlayer);
        lobbyManagement.getLobby(gameID).get().joinUser(thirdPlayer);
        chosenCards.add((short) 7);
        chosenCards.add((short) 8);
        chosenCards.add((short) 9);
        chosenCards.add((short) 10);
        chosenCards.add((short) 11);

        lobbyManagement.getLobby(gameID).get().setChosenCards(chosenCards);
        bus.post(new StartGameInternalMessage(gameID));
    }

    @Test
    void checkIfGameIsFinished() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();

        assertFalse(playground.getCompositePhase().checkIfGameIsFinished());
        playground.getCardField().replace((short) 6, 0);
        assertTrue(playground.getCompositePhase().checkIfGameIsFinished());
        playground.getCardField().replace((short) 6, 10);
        assertFalse(playground.getCompositePhase().checkIfGameIsFinished());
        for (int i = 8; i < 11; i++) {
            playground.getCardField().replace((short) i, 0);
        }
        assertTrue(playground.getCompositePhase().checkIfGameIsFinished());
    }
}
