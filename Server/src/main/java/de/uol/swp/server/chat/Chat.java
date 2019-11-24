package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatMessage;

import java.util.ArrayList;

/**
 * The type Chat.
 */
public class Chat implements de.uol.swp.common.chat.Chat {

    private String ChatId;
    private ArrayList<ChatMessage> Messages = new ArrayList<>();

    public Chat(String chatId) { this.ChatId = chatId; }

    /**
     * Gets ChatId.
     *
     * @return Value of ChatId.
     */
    public String getChatId() {
        return ChatId;
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
     * Sets new ChatId.
     *
     * @param ChatId New value of ChatId.
     */
    public void setChatId(String ChatId) {
        this.ChatId = ChatId;
    }
}
