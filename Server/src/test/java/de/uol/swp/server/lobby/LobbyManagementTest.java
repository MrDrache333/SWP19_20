package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LobbyManagementTest {

    static final User defaultLobbyOwner = new UserDTO("Owner", "Test", "123@test.de");
    static final String defaultLobbyName = "Lobby";
    final LobbyManagement lobbyManagement = new LobbyManagement();

    @Test
    void createLobby() {
        UUID newLobby = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyOwner);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isPresent());
    }

    @Test
    void dropLobby() {
        lobbyManagement.createLobby(defaultLobbyName, defaultLobbyOwner);
        lobbyManagement.dropLobby(defaultLobbyName);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isEmpty());
    }

    @Test
    void getLobby() {
        lobbyManagement.createLobby(defaultLobbyName, defaultLobbyOwner);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isPresent());
    }

    @Test
    void leaveLobby() {
        lobbyManagement.leaveLobby(defaultLobbyName, defaultLobbyOwner);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isEmpty());
    }

    @Test
    void getLobbies() {
        lobbyManagement.createLobby(defaultLobbyName, defaultLobbyOwner);
        assertEquals(lobbyManagement.getLobbies().size(), 1);
        lobbyManagement.createLobby("Lobby2", new UserDTO("", "", ""));
        assertEquals(lobbyManagement.getLobbies().size(), 2);
        lobbyManagement.dropLobby(defaultLobbyName);
        lobbyManagement.dropLobby("Lobby2");
        assertEquals(lobbyManagement.getLobbies().size(), 0);
    }
}