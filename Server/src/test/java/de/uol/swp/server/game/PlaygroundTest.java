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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlaygroundTest {

    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(new ChatManagement(), lobbyManagement);
    static final EventBus bus = new EventBus();
    static final GameService gameService = new GameService(bus, gameManagement, new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore())));

    static UUID gameID;

    @BeforeAll
    static void init() {
        gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
        lobbyManagement.getLobby(gameID).get().joinUser(secondPlayer);
        lobbyManagement.getLobby(gameID).get().joinUser(thirdPlayer);
        bus.post(new StartGameInternalMessage(gameID));
    }

    /**
     * Testet ob actual- und nextPlayer korrekt aktualisiert werden
     *
     * @author Julia
     * @since Sprint5
     */
    @Test
    void testNewTurn() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        int actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        int next = playground.getPlayers().indexOf(playground.getNextPlayer());
        //Player an erster Position der Liste beginnt
        assertEquals(0, actual);
        assertEquals(1, next);

        playground.newTurn();
        actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        next = playground.getPlayers().indexOf(playground.getNextPlayer());
        assertEquals(1, actual);
        assertEquals(2, next);

        playground.newTurn();
        actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        next = playground.getPlayers().indexOf(playground.getNextPlayer());
        assertEquals(2, actual);
        assertEquals(0, next);
    }

    /**
     * Testet die checkForActionCard - Methode
     *
     * @author Julia
     * @since Sprint5
     */
    @Test
    void testCheckForActionCard() {
        //Bei Spielbeginn hat der Spieler keine Aktionskarten auf der Hand
        assertFalse(gameManagement.getGame(gameID).get().getPlayground().checkForActionCard());

        //TODO: weitere Fälle testen, wenn weitere Funktion (Kauf von Aktionskarten) implementiert wurde
    }
}
