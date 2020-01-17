package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    UserDTO gameOwner;
    private LobbyDTO lobby;

    public UserJoinedLobbyMessage() {
    }

    public UserJoinedLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID, UserDTO gameOwner, LobbyDTO lobby) {
        super(lobbyName, user, lobbyID);
        this.gameOwner = gameOwner;
        this.lobby = lobby;
    }

    public UserDTO getGameOwner() {
        return gameOwner;
    }

    public LobbyDTO getLobby() { return lobby; }
}
