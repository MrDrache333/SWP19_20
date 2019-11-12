package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

/**
 * The type New chat message.
 */
public class NewChatMessage extends AbstractServerMessage {

    private User Sender;
    private String ChatId;
    private ChatMessage Message;

    /**
     * Instantiates a new New chat message.
     *
     * @param chatid  the chatid
     * @param sender  the sender
     * @param message the message
     */
    public NewChatMessage(String chatid, User sender, ChatMessage message) {
        this.Sender = sender;
        this.Message = message;
        this.ChatId = chatid;
    }

    /**
     * Instantiates a new New chat message.
     *
     * @param sender  the sender
     * @param message the message
     */
    public NewChatMessage(User sender, ChatMessage message) {
        this.Sender = sender;
        this.Message = message;
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