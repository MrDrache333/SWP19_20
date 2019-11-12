package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatMessage;

import java.util.ArrayList;

/**
 * The type Chat.
 */
public class Chat {

    private long ChatId;
    private ArrayList<ChatMessage> Messages = new ArrayList<>();


    /**
     * Sets new ChatId.
     *
     * @param ChatId New value of ChatId.
     */
    public void setChatId(long ChatId) {
        this.ChatId = ChatId;
    }

    /**
     * Gets Messages.
     *
     * @return Value of Messages.
     */
    public ArrayList<ChatMessage> getMessages() {
        return Messages;
    }

    /**
     * Sets new Messages.
     *
     * @param Messages New value of Messages.
     */
    public void setMessages(ArrayList<ChatMessage> Messages) {
        this.Messages = Messages;
    }

    /**
     * Gets ChatId.
     *
     * @return Value of ChatId.
     */
    public long getChatId() {
        return ChatId;
    }
}
