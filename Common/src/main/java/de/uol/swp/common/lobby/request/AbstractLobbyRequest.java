package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.dto.UserDTO;

public class AbstractLobbyRequest extends AbstractRequestMessage {

    private String lobbyName;
    private UserDTO user;

    public AbstractLobbyRequest() {}

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
}
