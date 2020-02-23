package de.uol.swp.server.game;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type Game management test.
 */
class GameManagementTest {
    static final User defaultLobbyOwner = new UserDTO("Owner", "Test", "123@test.de");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static UUID id;

    /**
     * Initialisiere die benötigten Managements.
     *
     * @author KenoO
     * @since Sprint5
     */
    @BeforeAll
    static void init() {
        lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner);
        id = lobbyManagement.getLobby(defaultLobbyName).get().getLobbyID();
        chatManagement.createChat(id.toString());
    }

    /**
     * Erstelle for jedem Test ein neues Spiel
     *
     * @author KenoO
     * @since Sprint5
     */
    @BeforeEach
    void setUp() {
        gameManagement.createGame(id);
    }

    /**
     * Lösche das Spiel nach jedem Test
     *
     * @author KenoO
     * @since Sprint5
     */
    @AfterEach
    void afterEach() {
        gameManagement.deleteGame(id);
    }

    /**
     * Prüfe, ob das Spiel erfolgreich erstellt werden konnte
     *
     * @author KenoO
     * @since Sprint5
     */
    @Test
    void createGame() {
        assertTrue(gameManagement.getGame(id).isPresent());
    }

    /**
     * Prüfe, ob das Spiel entfernt werden kann
     *
     * @author KenoO
     * @since Sprint5
     */
    @Test
    void deleteGame() {
        gameManagement.deleteGame(id);
        assertTrue(gameManagement.getGame(id).isEmpty());
    }

    /**
     * Prüft, ob das erstellte Spiel zurückgegeben wird
     *
     * @author KenoO
     * @since Sprint5
     */
    @Test
    void getGame() {
        assertTrue(gameManagement.getGame(id).isPresent());
    }
}