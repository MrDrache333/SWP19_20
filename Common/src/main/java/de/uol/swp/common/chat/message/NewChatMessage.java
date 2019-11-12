package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.message.AbstractServerMessage;

/**
 * The type New chat message.
 */
public class NewChatMessage extends AbstractServerMessage {

    private String ChatId;
    private ChatMessage Message;

    /**
     * Instantiates a new New chat message.
     *
     * @param chatid  the chatid
     * @param message the message
     */
    public NewChatMessage(String chatid, ChatMessage message) {
        this.Message = message;
        this.ChatId = chatid;
    }

    /**
     * Gets chat id.
     *
     * @return the chat id
     */
    public String getChatId() {
        return ChatId;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public ChatMessage getMessage() {
        return Message;
    }
}