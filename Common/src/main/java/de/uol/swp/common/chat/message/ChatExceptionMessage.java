package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;

/**
 * Die Klasse der Chat exception message.
 *
 * @author Keno O.
 * @since Sprint 2
 */
public class ChatExceptionMessage extends AbstractResponseMessage {

    private User Sender;
    private ChatException exception;

    /**
     * Konstruktor der Chat exception message.
     *
     * @author Keno O.
     * @since Sprint 2
     * @param sender Der Sender
     * @param e      Die Exception
     */
    public ChatExceptionMessage(User sender, ChatException e) {
        this.Sender = sender;
        this.exception = e;
    }

    /**
     * Gibt den Sender zurück.
     *
     * @author Keno O.
     * @since Sprint 2
     * @return Sender the sender
     */
    public User getSender() {
        return Sender;
    }

    /**
     * Gibt die Exception zurück.
     *
     * @author Keno O.
     * @since Sprint 2
     * @return exception Die Exception
     */
    public ChatException getException() {
        return exception;
    }
}
