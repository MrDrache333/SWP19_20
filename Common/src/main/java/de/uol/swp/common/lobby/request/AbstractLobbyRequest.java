package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.Objects;

public class AbstractLobbyRequest extends AbstractRequestMessage {

    private String lobbyName;
    private UserDTO user;

    public AbstractLobbyRequest() {
    }

    public AbstractLobbyRequest(String lobbyName, UserDTO user) {
        this.lobbyName = lobbyName;
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLobbyRequest that = (AbstractLobbyRequest) o;
        return Objects.equals(lobbyName, that.lobbyName) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyName, user);
    }
}
