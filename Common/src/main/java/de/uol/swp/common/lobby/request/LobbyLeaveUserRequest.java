package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class LobbyLeaveUserRequest extends AbstractLobbyRequest {


    private static final long serialVersionUID = -1015160817744213145L;
    private UUID lobbyID;

    public LobbyLeaveUserRequest() {
    }

    public LobbyLeaveUserRequest(String lobbyName, UserDTO user, UUID lobbyID) {
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
