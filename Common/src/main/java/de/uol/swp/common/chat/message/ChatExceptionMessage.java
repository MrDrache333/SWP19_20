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

    private static final long serialVersionUID = 2939228420446388785L;

    private User Sender;
    private ChatException exception;

    /**
     * Konstruktor der Chat exception message.
     *
     * @param sender Der Sender
     * @param e      Die Exception
     * @author Keno O.
     * @since Sprint 2
     */
    public ChatExceptionMessage(User sender, ChatException e) {
        this.Sender = sender;
        this.exception = e;
    }

    /**
     * Gibt den Sender zurück.
     *
     * @return Sender the sender
     * @author Keno O.
     * @since Sprint 2
     */
    public User getSender() {
        return Sender;
    }

    /**
     * Gibt die Exception zurück.
     *
     * @return exception Die Exception
     * @author Keno O.
     * @since Sprint 2
     */
    public ChatException getException() {
        return exception;
    }
}
