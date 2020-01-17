package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class UserLeftLobbyMessage extends AbstractLobbyMessage {

    private UserDTO gameOwner;
    private LobbyDTO lobby;

    public UserLeftLobbyMessage() {
    }

    public UserLeftLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID, UserDTO gameOwner, LobbyDTO lobby) {
        super(lobbyName, user, lobbyID);
        this.lobby = lobby;
        this.gameOwner = gameOwner;
    }

    public UserDTO getGameOwner(){
        return gameOwner;
    }

    public LobbyDTO getLobby() { return lobby; }
}
