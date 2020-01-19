package de.uol.swp.common.chat.request;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * The type New chat message request.
 */
public class NewChatMessageRequest extends AbstractRequestMessage {

    private String ChatId;
    private ChatMessage Message;


    /**
     * Erstellt eine neue MainMenu-Chatnachticht-Request
     *
     * @param message Die Chatnachricht die versendet werden soll
     * @author Keno O
     * @since Sprint 2
     */
    public NewChatMessageRequest(ChatMessage message) {
        this.ChatId = "global";
        this.Message = message;
    }

    /**
     * Erstellt eine neue Lobbychatnachticht-Request
     *
     * @param message       Die Chatnachricht die versendet werden soll
     * @param chatId        Die ChatId für den Chat für den die Chatnachricht ist
     * @author Keno O
     * @since Sprint 2
     */
    public NewChatMessageRequest(String chatId, ChatMessage message) {
        this.Message = message;
        this.ChatId = chatId;
    }

    /**
     * Gibt die ChatId zurück für den Chat für den die Nachricht bestimmt ist.
     *
     * @return die ChatId für den Chat für die die Nachricht ist
     * @author Keno O
     * @since Sprint 2
     */
    public String getChatid() {
        return ChatId;
    }

    /**
     * Gibt die Chatnachricht zurück die von dem Benutzer versendet wurde.
     *
     * @return versendete Chatnachricht
     * @author Keno O
     * @since Sprint 2
     */
    public ChatMessage getMessage() {
        return Message;
    }
}
