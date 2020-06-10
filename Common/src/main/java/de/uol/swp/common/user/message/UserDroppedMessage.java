package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

/**
 * Nachricht, die beinhaltet, wessen Benutzeraccount gelöscht wurde
 *
 * @author Anna
 * @since Sprint 4
 */
public class UserDroppedMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -8939300536981907236L;
    private final User user;

    /**
     * Erstellt eine neue User dropped message.
     *
     * @param user Der gelöschte Benutzer
     */
    public UserDroppedMessage(User user) {
        this.user = user;
    }

    /**
     * Gibt den gelöschten Benutzer zurück
     *
     * @return Der gelöschte Benutzer
     */
    public User getUser() {
        return user;
    }
}
