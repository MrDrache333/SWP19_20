package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

/**
 * Eine Klasse, die eine OpenSettingsRequest erstellt, um die Einstellungen öffnen zu können
 *
 * @author Anna
 * @since 3
 */
public class OpenSettingsRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -4996091811171165930L;
    private final User user;

    /**
     * Der Konstruktor der OpenSettingsRequest
     *
     * @param user den übergebenen User
     * @author Anna
     * @since Sprint 3
     */
    public OpenSettingsRequest(User user) {
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
