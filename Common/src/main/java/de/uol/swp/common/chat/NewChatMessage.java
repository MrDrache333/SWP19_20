package de.uol.swp.common.chat;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

/**
 * The type New chat message.
 */
public class NewChatMessage extends AbstractServerMessage implements ChatMessage{

    private User Sender;
    private long ChatId;
    private String Message;

    /**
     * Instantiates a new New chat message.
     *
     * @param chatid  the chatid
     * @param sender  the sender
     * @param message the message
     */
    public NewChatMessage(long chatid, User sender, String message){
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
    public NewChatMessage(User sender, String message){
        this.Sender = sender;
        this.Message = message;
    }


    /**
     * Gets Sender.
     *
     * @return Value of Sender.
     */
    @Override
    public User getSender() {
        return Sender;
    }

    /**
     * Sets new Message.
     *
     * @param Message New value of Message.
     */
    @Override
    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     * Gets ChatId.
     *
     * @return Value of ChatId.
     */
    @Override
    public long getChatId() {
        return ChatId;
    }

    /**
     * Sets new Sender.
     *
     * @param Sender New value of Sender.
     */
    @Override
    public void setSender(User Sender) {
        this.Sender = Sender;
    }

    /**
     * Sets new ChatId.
     *
     * @param ChatId New value of ChatId.
     */
    @Override
    public void setChatId(long ChatId) {
        this.ChatId = ChatId;
    }

    /**
     * Gets Message.
     *
     * @return Value of Message.
     */
    @Override
    public String getMessage() {
        return Message;
    }
}
