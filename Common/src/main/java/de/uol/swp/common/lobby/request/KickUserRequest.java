package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class KickUserRequest extends AbstractLobbyRequest {

    private final UserDTO userToKick;

    public KickUserRequest(UUID lobbyID, UserDTO owner, UserDTO userToKick) {
        super(lobbyID, owner);
        this.userToKick = userToKick;
    }

    public UserDTO getUserToKick() {
        return userToKick;
    }

}