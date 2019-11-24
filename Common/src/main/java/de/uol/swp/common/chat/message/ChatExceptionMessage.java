package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;

/**
 * The type Chat exception message.
 */
public class ChatExceptionMessage extends AbstractResponseMessage {

    private User Sender;
    private ChatException exception;

    /**
     * Instantiates a new Chat exception message.
     *
     * @param sender the sender
     * @param e      the e
     */
    public ChatExceptionMessage(User sender, ChatException e) {
        this.Sender = sender;
        this.exception = e;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public User getSender() {
        return Sender;
    }

    /**
     * Gets exception.
     *
     * @return the exception
     */
    public ChatException getException() {
        return exception;
    }
}
