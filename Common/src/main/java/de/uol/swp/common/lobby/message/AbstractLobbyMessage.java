package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class AbstractLobbyMessage extends AbstractServerMessage {

    private String lobbyName;
    private User user;
    private UUID lobbyID;

    public AbstractLobbyMessage() {
    }

    public AbstractLobbyMessage(String lobbyName, User user, UUID lobbyID) {
        this.lobbyName = lobbyName;
        this.user = user;
        this.lobbyID = lobbyID;
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

    public UUID getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }
}
