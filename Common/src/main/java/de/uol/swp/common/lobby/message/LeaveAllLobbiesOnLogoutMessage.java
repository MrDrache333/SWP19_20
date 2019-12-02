package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

public class LeaveAllLobbiesOnLogoutMessage extends AbstractLobbyMessage {

    private User user;

    public LeaveAllLobbiesOnLogoutMessage() {}

    public LeaveAllLobbiesOnLogoutMessage(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
