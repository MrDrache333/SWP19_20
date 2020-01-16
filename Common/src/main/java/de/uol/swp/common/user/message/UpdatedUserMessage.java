package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

/**
 * Die UpdatedUserMessage
 */
public class UpdatedUserMessage extends AbstractServerMessage {

    private User user;
    private User oldUser;

    /**
     * Initialisiert eine neue UpdatedUserMessage
     *
     * @param user    der User
     * @param oldUser der User mit den alten Einstellungen
     * @author Julia
     * @since Sprint4
     */

    public UpdatedUserMessage(User user, User oldUser) {
        this.user = user;
        this.oldUser = oldUser;
    }

    /**
     * Gibt den User mit neuen Einstellungen zurück
     *
     * @return den User mit neuen Einstellungen
     * @author Julia
     * @since Sprint4
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt den User mit den alten Einstellungen zurück
     *
     * @return den User mit seinen alten Einstellungen
     * @author Timo
     * @since Sprint3
     */
    public User getOldUser() {
        return oldUser;
    }
}
