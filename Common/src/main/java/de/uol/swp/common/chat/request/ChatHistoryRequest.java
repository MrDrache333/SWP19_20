package de.uol.swp.common.chat.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

public class ChatHistoryRequest extends AbstractRequestMessage {

    private String ChatId;
    private User Sender;

    /**
     * Konstruktor für ChatHistoryRequest mit ChatID
     *
     * @param chatId Die ChatID
     * @param sender Der Absender
     * @author Keno O
     * @since Sprint 1
     */
    public ChatHistoryRequest(String chatId, User sender) {
        this.ChatId = chatId;
        this.Sender = sender;
    }

    /**
     * Konstruktor für ChatHistoryRequest ohne ChatID
     *
     * @param sender Der Absender
     * @author Keno O
     * @since Sprint 1
     */
    public ChatHistoryRequest(User sender) {
        this.ChatId = "global";
        this.Sender = sender;
    }


    /**
     * Getter für ChatID
     *
     * @return ChatID
     * @author Keno O
     * @since Sprint 1
     */
    public String getChatId() {
        return ChatId;
    }

    /**
     * Getter für Absender
     *
     * @return Absender
     * @author Keno O
     * @since Sprint 1
     */
    public User getSender() {
        return Sender;
    }
}
