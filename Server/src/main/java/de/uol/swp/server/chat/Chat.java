package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatMessage;

import java.util.ArrayList;

/**
 * Die Klasse Chat
 *
 * @author Keno
 * @since Sprint 2
 */
public class Chat implements de.uol.swp.common.chat.Chat {

    private String ChatId;
    private ArrayList<ChatMessage> Messages = new ArrayList<>();

    /**
     * Initalisiert einen neuen Chat
     *
     * @param chatId
     * @author Keno S
     * @since SPrint2
     */
    public Chat(String chatId) {
        this.ChatId = chatId;
    }

    /**
     * Übergibt die ChatID.
     *
     * @return Wert der ChatID
     * @author Keno
     * @since Sprint 2
     */
    public String getChatId() {
        return ChatId;
    }

    /**
     * Setzt die ChatID
     *
     * @param ChatId Neuer Wert der ChatID.
     * @author KenoS
     * @since Sprint3
     */
    public void setChatId(String ChatId) {
        this.ChatId = ChatId;
    }

    /**
     * Bekommt die Nachrichten
     *
     * @return Wert der Nachrichten
     * @author KenoO
     * @since Sprint2
     */
    public ArrayList<ChatMessage> getMessages() {
        return Messages;
    }

    /**
     * Setzt die Nachrichten
     *
     * @param Messages Neuen Wert der Nachricht.
     * @author KenoO
     * @since Sprint2
     */
    public void setMessages(ArrayList<ChatMessage> Messages) {
        this.Messages = Messages;
    }
}
