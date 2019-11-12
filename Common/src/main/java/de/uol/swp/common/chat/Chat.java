package de.uol.swp.common.chat;

import java.util.ArrayList;

/**
 * The interface Chat.
 */
public interface Chat {

    /**
     * Gets Messages.
     *
     * @return Value of Messages.
     */
    ArrayList<ChatMessage> getMessages();

    /**
     * Sets new Messages.
     *
     * @param Messages New value of Messages.
     */
    void setMessages(ArrayList<ChatMessage> Messages);

    /**
     * Gets ChatId.
     *
     * @return Value of ChatId.
     */
    String getChatId();

    /**
     * Sets new ChatId.
     *
     * @param ChatId New value of ChatId.
     */
    void setChatId(String ChatId);
}
