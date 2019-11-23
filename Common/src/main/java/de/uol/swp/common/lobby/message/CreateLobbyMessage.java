package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

/**
 * Message das eine neue Lobby erstellt worden ist und die gegebenen Daten genutz werden können.
 *
 * @author Paula, Haschem, Ferit
 * @version 0.1
 * @since Sprint2
 */
public class CreateLobbyMessage extends AbstractLobbyMessage {
    private String name;
    private User user;
    private Lobby lobby;

    public CreateLobbyMessage() {
    }

    public CreateLobbyMessage(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public CreateLobbyMessage(Lobby lobby) {
        this.lobby = lobby;
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


}


