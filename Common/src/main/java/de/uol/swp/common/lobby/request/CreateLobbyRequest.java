package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.User;

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

    public CreateLobbyRequest(String name, User owner) {
        super(name, owner);
    }

    public void setOwner(User owner) {
        setUser(owner);
    }

    public User getOwner() {
        return getUser();
    }

}
