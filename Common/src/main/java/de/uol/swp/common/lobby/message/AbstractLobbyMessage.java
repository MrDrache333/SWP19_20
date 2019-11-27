package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class AbstractLobbyMessage extends AbstractServerMessage {

    private String name;
    private User user;
    private UUID lobbyID;

    public AbstractLobbyMessage() {
    }

    public AbstractLobbyMessage(String name, User user, UUID lobbyID) {
        this.name = name;
        this.user = user;
        this.lobbyID = lobbyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getLobbyID(){
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID){
        this.lobbyID = lobbyID;
    }
}
