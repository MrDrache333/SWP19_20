package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.User;


public class LeaveAllLobbiesOnLogoutRequest extends AbstractLobbyRequest {

    private User user;

    public LeaveAllLobbiesOnLogoutRequest() {
    }

    public LeaveAllLobbiesOnLogoutRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
