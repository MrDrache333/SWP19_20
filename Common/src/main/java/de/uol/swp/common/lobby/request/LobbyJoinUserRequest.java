package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.dto.UserDTO;
import java.util.UUID;

public class LobbyJoinUserRequest extends AbstractLobbyRequest {

    private UUID lobbyID;

    public LobbyJoinUserRequest() {
    }

    public LobbyJoinUserRequest(String lobbyName, UserDTO user, UUID lobbyID) {
        super(lobbyName, user);
        this.lobbyID = lobbyID;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }
}
