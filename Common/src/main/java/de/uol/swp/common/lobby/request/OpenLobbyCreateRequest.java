package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

public class OpenLobbyCreateRequest extends AbstractRequestMessage {
    private final User user;
    
    /**
     * Der Konstruktor der OpenLobbyCreateRequest
     *
     * @param user den übergebenen User
     * @author Paula
     * @since Sprint 7
     */
    public OpenLobbyCreateRequest(User user) {
        this.user = user;
    }

    /**
     * Getter der Klasse
     *
     * @return den gegebenen User
     * @author Paula
     * @since Sprint 7
     */
    public User getUser() {
        return user;
    }
}




