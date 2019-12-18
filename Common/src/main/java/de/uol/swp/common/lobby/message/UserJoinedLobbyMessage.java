package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    private LobbyDTO lobby;

    public UserJoinedLobbyMessage() {
    }

    public UserJoinedLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID, LobbyDTO lobby) {
        super(lobbyName, user, lobbyID);
        this.lobby = lobby;
    }

    public LobbyDTO getLobby() { return lobby; }
}
