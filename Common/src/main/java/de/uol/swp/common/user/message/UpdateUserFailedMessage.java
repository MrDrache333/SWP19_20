package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

/**
 * Die Nachricht, welche übermittelt wird, wenn eine Veränderung an den Nutzerdaten fehlschlägt.
 */
public class UpdateUserFailedMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -8102266479611328349L;
    private User user;
    private String message;

    /**
     * Konstruktor der UpdateUserFailedMessage.
     *
     * @param user    der User
     * @param message die Nachricht des Fehlers
     * @author Julia
     */
    public UpdateUserFailedMessage(User user, String message) {
        this.user = user;
        this.message = message;
    }

    /**
     * Gibt den User zurück.
     *
     * @return der User
     * @author Julia
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die Nachricht zurück.
     *
     * @return die Fehlermeldung
     * @author Julia
     */
    public String getMessage() {
        return message;
    }

}
