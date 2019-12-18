package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;


public class LeaveAllLobbiesOnLogoutRequest extends AbstractLobbyRequest {

    private UserDTO user;

    public LeaveAllLobbiesOnLogoutRequest() {
    }

    public LeaveAllLobbiesOnLogoutRequest(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }
}
