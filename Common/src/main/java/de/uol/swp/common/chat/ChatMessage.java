package de.uol.swp.common.chat;

import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {

    private User sender;
    private String message;
    private Date timeStamp;

    /**
     * Instanziiert eine neue ChatMessage
     *
     * @param sender  der Absender
     * @param message die Nachricht
     * @author Keno O.
     * @since Sprint2
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
     * @author Keno O.
     * @since Sprint2
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gibt den Absender zurück
     *
     * @return der Absender
     * @author Keno O.
     * @since Sprint2
     */
    public User getSender() {
        return sender;
    }

    /**
     * Gibt den Zeitstempel zurück
     *
     * @return der Zeitstempel
     * @author Keno O.
     * @since Sprint2
     */
    public Date getTimeStamp() {
        return timeStamp;
    }
}
