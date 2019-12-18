package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.Objects;
import java.util.UUID;

public class AbstractLobbyMessage extends AbstractServerMessage {

    private String name;
    private UserDTO user;
    private UUID lobbyID;
    private String lobbyName;


    public AbstractLobbyMessage() {}

    public AbstractLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID) {
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLobbyMessage that = (AbstractLobbyMessage) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user);
    }
}
