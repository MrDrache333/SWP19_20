package de.uol.swp.common.chat;

/**
 * Der Interface-Chat-Dienst.
 *
 * @author Keno O.
 * @since Sprint 2
 */
public interface ChatService {

    /**
     * Sendet eine Nachricht
     *
     * @param message Die Nachricht
     * @author Keno O.
     * @since Sprint 2
     */
    void sendMessage(ChatMessage message);

    /**
     * Sendet eine Nachricht.
     *
     * @param chatID  die Chat id
     * @param message die Nachricht
     * @author Keno O.
     * @since Sprint 2
     */
    void sendMessage(String chatID, ChatMessage message);
}