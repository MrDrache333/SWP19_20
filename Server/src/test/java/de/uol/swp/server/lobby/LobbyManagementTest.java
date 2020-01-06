package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LobbyManagementTest {

    static final User defaultLobbyOwner = new UserDTO("Owner", "Test", "123@test.de");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    final LobbyManagement lobbyManagement = new LobbyManagement();

    @BeforeEach
    void createLobby() {
        lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner);
    }

    @Test
    void createLobbyTest() {
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isPresent());
    }

    @Test
    void createLobbyFailedTest() {
        //Lobby name already exists
        assertThrows(IllegalArgumentException.class, () -> lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner));
    }

    @Test
    void dropLobbyTest() {
        lobbyManagement.dropLobby(defaultLobbyName);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isEmpty());
    }

    @Test
    void dropLobbyFailedTest() {
        //Lobby does not exist
        assertThrows(IllegalArgumentException.class, () -> lobbyManagement.dropLobby("Test"));
    }

    @Test
    void getLobbyTest() {
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isPresent());

        lobby = lobbyManagement.getLobby("Test");
        assertTrue(lobby.isEmpty());
    }

    @Test
    void leaveLobbyTest() {
        lobbyManagement.leaveLobby(defaultLobbyName, defaultLobbyOwner);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isEmpty());
    }

    @Test
    void getLobbiesTest() {
        assertEquals(1, lobbyManagement.getLobbies().size());
        lobbyManagement.createLobby("Lobby2","", new UserDTO("", "", ""));
        assertEquals(2, lobbyManagement.getLobbies().size());
        lobbyManagement.dropLobby(defaultLobbyName);
        lobbyManagement.dropLobby("Lobby2");
        assertEquals(0, lobbyManagement.getLobbies().size());
    }
}