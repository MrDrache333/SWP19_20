package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

import java.util.UUID;

public class LobbyLeaveUserRequest extends AbstractLobbyRequest {


    private UUID lobbyID;

    public LobbyLeaveUserRequest() {
    }

    public LobbyLeaveUserRequest(String lobbyName, User user, UUID lobbyID) {
        super(lobbyName, user);
        this.lobbyID = lobbyID;
    }


    public void setLobbyID(UUID lobbyID){
        this.lobbyID = lobbyID;
    }

    public UUID getLobbyID(){
        return  lobbyID;
    }

}
