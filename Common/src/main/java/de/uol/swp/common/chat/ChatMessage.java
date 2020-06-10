package de.uol.swp.common.chat;

import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Die ChatMessage
 *
 * @author Keno O.
 * @since Sprint 2
 */
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 9222541795987990407L;

    private final User sender;
    private final String message;
    private final Date timeStamp;

    /**
     * Instanziiert eine ChatMessage
     *
     * @param sender  der Absender
     * @param message die Nachricht
     */
    public ChatMessage(User sender, String message) {
        this.sender = sender;
        this.message = message;
        this.timeStamp = new Date();
    }

    /**
     * Gibt die Nachricht zurück
     *
     * @return die Nachricht
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gibt den Absender zurück
     *
     * @return der Absender
     */
    public User getSender() {
        return sender;
    }

    /**
     * Gibt den Zeitstempel zurück
     *
     * @return der Zeitstempel
     */
    public Date getTimeStamp() {
        return timeStamp;
    }
}
