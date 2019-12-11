package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

public class AbstractLobbyRequest extends AbstractRequestMessage {

    private String lobbyName;
    private User user;

    public AbstractLobbyRequest() {}

    public AbstractLobbyRequest(String lobbyName, User user) {
        this.lobbyName = lobbyName;
        this.user = user;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
