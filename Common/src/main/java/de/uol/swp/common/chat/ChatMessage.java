package de.uol.swp.common.chat;

import de.uol.swp.common.user.User;

/**
 * The interface Chat message.
 */
public interface ChatMessage {

    /**
     * Gets message.
     *
     * @return the message
     */
    String getMessage();

    /**
     * Gets chat id.
     *
     * @return the chat id
     */
    long getChatId();

    /**
     * Gets sender.
     *
     * @return the sender
     */
    User getSender();

    /**
     * Sets message.
     */
    void setMessage(String message);

    /**
     * Sets chat id.
     */
    void setChatId(long chatid);

    /**
     * Sets sender.
     */
    void setSender(User sender);
}
