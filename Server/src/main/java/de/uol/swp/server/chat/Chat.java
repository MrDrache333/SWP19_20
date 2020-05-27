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

    private String chatID;
    private ArrayList<ChatMessage> messages = new ArrayList<>();

    /**
     * Initalisiert einen neuen Chat
     *
     * @param chatID Die Chat ID
     * @author Keno S
     * @since SPrint2
     */
    public Chat(String chatID) {
        this.chatID = chatID;
    }

    /**
     * Übergibt die ChatID.
     *
     * @return Wert der ChatID
     * @author Keno
     * @since Sprint 2
     */
    public String getChatId() {
        return chatID;
    }

    /**
     * Setzt die ChatID
     *
     * @param chatID Neuer Wert der ChatID.
     * @author KenoS
     * @since Sprint3
     */
    public void setChatId(String chatID) {
        this.chatID = chatID;
    }

    /**
     * Bekommt die Nachrichten
     *
     * @return Wert der Nachrichten
     * @author KenoO
     * @since Sprint2
     */
    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    /**
     * Setzt die Nachrichten
     *
     * @param messages Neuen Wert der Nachricht.
     * @author KenoO
     * @since Sprint2
     */
    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }
}
