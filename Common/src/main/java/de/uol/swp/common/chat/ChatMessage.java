package de.uol.swp.common.chat;

import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Chat message.
 */
public class ChatMessage implements Serializable {

    private User Sender;
    private String Message;
    private Date TimeStamp;

    /**
     * Instantiates a new Chat message.
     *
     * @param sender  the sender
     * @param message the message
     */
    public ChatMessage(User sender, String message) {
        this.Sender = sender;
        this.Message = message;
        this.TimeStamp = new Date();
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return Message;
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
     * Gets time stamp.
     *
     * @return the time stamp
     */
    public Date getTimeStamp() {
        return TimeStamp;
    }
}
