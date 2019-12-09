package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.message.AbstractServerMessage;

/**
 * The type New chat message.
 */
public class NewChatMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -2561886836547126480L;
    private String ChatId;
    private ChatMessage Message;

    public NewChatMessage() {
    }

    /**
     * Instantiates a new New chat message.
     *
     * @param chatId  the chatid
     * @param message the message
     */
    public NewChatMessage(String chatId, ChatMessage message) {
        this.Message = message;
        this.ChatId = chatId;
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