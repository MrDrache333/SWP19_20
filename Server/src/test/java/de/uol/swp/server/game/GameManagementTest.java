package de.uol.swp.server.game;

import de.uol.swp.common.game.exception.GameManagementException;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse für das GameManagement
 *
 * @author Julia
 * @since Sprint 10
 */
public class GameManagementTest {

    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static UUID gameID;


    /**
     * Erstellt vor jedem Test eine Lobby
     *
     * @author Julia
     * @since Sprint 10
     */
    @BeforeEach
    void createLobby() {
        gameID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultOwner);
        chatManagement.createChat(gameID.toString());
        lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondPlayer);
    }

    /**
     * Testet das Erstellen eines Games
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testCreateGame() {
        gameManagement.createGame(gameID);
        assertTrue(gameManagement.getGame(gameID).isPresent());
        UUID id = UUID.randomUUID();
        assertThrows(GameManagementException.class, () -> gameManagement.createGame(id));
    }

    /**
     * Testet das Löschen eines Games
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testDeleteGame() {
        gameManagement.createGame(gameID);
        assertTrue(gameManagement.getGame(gameID).isPresent());
        gameManagement.deleteGame(gameID);
        assertTrue(gameManagement.getGame(gameID).isEmpty());
    }

}
