package de.uol.swp.common.chat.message;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.message.AbstractServerMessage;

/**
 * Die Klasse NewChatMessage.
 */
public class NewChatMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -254449775979067706L;

    private final String chatId;
    private final ChatMessage message;

    /**
     * Initialisiert eine neue NewChatMessage.
     *
     * @param chatId  die Chat ID des aktuellen Chats.
     * @param message Die aktuelle Nachricht.
     * @author Keno Oelrichs Garcia, Darian Alves
     * @since Sprint 1
     */
    public NewChatMessage(String chatId, ChatMessage message) {
        this.message = message;
        this.chatId = chatId;
    }

    /**
     * Gibt die aktuelle Chat ID zurück.
     *
     * @return Die Chat ID.
     * @author Keno Oelrichs Garcia, Darian Alves
     * @since Sprint 3
     */
    public String getChatId() {
        return chatId;
    }

    /**
     * Gibt die Message zurück.
     *
     * @return Die Message.
     * @author Keno Oelrichs Garcia, Darian Alves
     * @since Sprint 3
     */
    public ChatMessage getMessage() {
        return message;
    }
}