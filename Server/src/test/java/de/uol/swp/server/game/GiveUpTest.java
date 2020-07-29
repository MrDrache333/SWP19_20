package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse, in der das Aufgeben getestet wird
 *
 * @author Ferit
 * @since Sprint 6
 */
@SuppressWarnings("UnstableApiUsage")
public class GiveUpTest {
    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final EventBus bus = new EventBus();
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final AuthenticationService authenticationService = new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore()), lobbyManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authenticationService);
    private static final ArrayList<Short> chosenCards = new ArrayList<>();
    static UUID gameID;

    /**
     * Initialisiert die benötigten Objekte/Parameter
     *
     * @author Ferit
     * @since Sprint 6
     */
    @BeforeAll
    static void init() {
        try {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setChosenCards(chosenCards);
            bus.post(new StartGameInternalMessage(gameID));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet ob ein Nutzer Aufgeben kann.
     *
     * @author Ferit
     * @since Sprint 6
     */
    @Test
    void testGiveUp() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            Boolean successTest = playground.playerGaveUp(gameID, (UserDTO) secondPlayer, true);
            assertEquals(true, successTest);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }
}
