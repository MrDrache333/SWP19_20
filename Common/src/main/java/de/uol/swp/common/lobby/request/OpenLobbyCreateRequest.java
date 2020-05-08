package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

public class OpenLobbyCreateRequest extends AbstractRequestMessage {
    private User user;
    /**
     * Der Konstruktor der OpenSettingsRequest
     *
     * @param user den übergebenen User
     * @author Anna
     * @since Sprint 3
     */
    public OpenLobbyCreateRequest(User user) {
        this.user = user;
    }

    /**
     * Getter der Klasse
     *
     * @return den gegebenen User
     * @author Anna
     * @since Sprint 3
     */
    public User getUser() {
        return user;
    }
}




