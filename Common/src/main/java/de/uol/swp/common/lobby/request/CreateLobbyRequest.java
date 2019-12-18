package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

/**
 * Request eine neue Lobby zu erstellen vom Client auf den Server
 *
 * @author Paula, Haschem, Ferit
 * @version 0.1
 * @since Sprint2
 */
public class CreateLobbyRequest extends AbstractLobbyRequest {

    public CreateLobbyRequest() {
    }

    public CreateLobbyRequest(String lobbyName, UserDTO owner) {
        super(lobbyName, owner);
    }

    public UserDTO getOwner() {
        return getUser();
    }

    public void setOwner(UserDTO owner) {
        setUser(owner);
    }
}
