package de.uol.swp.common.chat;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

/**
 * The type New chat message request.
 */
public class NewChatMessageRequest extends AbstractRequestMessage implements ChatMessage{

    private long Chatid;
    private User Sender;
    private String Message;

    /**
     * Instantiates a new New chat message request.
     *
     * @param sender  the sender
     * @param message the message
     */
    public NewChatMessageRequest(User sender, String message){
        this.Sender = sender;
        this.Message = message;
    }

    /**
     * Instantiates a new New chat message request.
     *
     * @param chatid  the chatid
     * @param sender  the sender
     * @param message the message
     */
    public NewChatMessageRequest(long chatid, User sender, String message){
        this.Sender = sender;
        this.Message = message;
        this.Chatid = chatid;
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
     * Sets new Chatid.
     *
     * @param chatid New value of Chatid.
     */
    @Override
    public void setChatId(long chatid) {
        this.Chatid = chatid;
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
     * Sets new Sender.
     *
     * @param Sender New value of Sender.
     */
    @Override
    public void setSender(User Sender) {
        this.Sender = Sender;
    }

    /**
     * Gets Chatid.
     *
     * @return Value of Chatid.
     */
    @Override
    public long getChatId() {
        return Chatid;
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
