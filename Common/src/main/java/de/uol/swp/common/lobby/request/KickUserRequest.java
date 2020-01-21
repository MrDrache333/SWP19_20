package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class KickUserRequest extends AbstractLobbyRequest {

    private UserDTO userToKick;
    private UUID lobbyID;

    public KickUserRequest(String lobbyName, UserDTO owner, UUID lobbyID, UserDTO userToKick){
        super(lobbyName, owner);
        this.lobbyID = lobbyID;
        this.userToKick = userToKick;
    }

    public UserDTO getUserToKick(){
        return userToKick;
    }

    public UUID getLobbyID(){
        return lobbyID;
    }
}
