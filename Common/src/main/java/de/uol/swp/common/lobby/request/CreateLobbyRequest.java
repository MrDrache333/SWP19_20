package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

/**
 * Request eine neue Lobby zu erstellen vom Client auf den Server
 *
 * @author Paula, Haschem, Ferit, Rike
 * @version 0.1
 * @since Sprint2
 */
public class CreateLobbyRequest extends AbstractLobbyRequest {

    String lobbyPassword;

    public CreateLobbyRequest() {
    }

    public CreateLobbyRequest(String lobbyName, UserDTO owner) {
        super(lobbyName, owner);
        this.lobbyPassword = "";
    }

    public CreateLobbyRequest(String lobbyName, String lobbyPassword, UserDTO owner) {
        super(lobbyName, owner);
        this.lobbyPassword = lobbyPassword;
    }

    public UserDTO getOwner() {
        return getUser();
    }

    public void setOwner(UserDTO owner) {
        setUser(owner);
    }

    public String getLobbyPassword() {
        return lobbyPassword;
    }
}
