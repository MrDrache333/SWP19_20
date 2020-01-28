package de.uol.swp.common.chat;

import de.uol.swp.common.user.User;

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
     * @param ChatId  die Chat id
     * @param message die Nachricht
     * @author Keno O.
     * @since Sprint 2
     */
    void sendMessage(String ChatId, ChatMessage message);


    /**
     * Ruft den Chatverlauf ab.
     *
     * @param sender der Absender
     * @return
     * @author Keno O.
     * @since Sprint 2
     */
    Chat getChatHistory(User sender);

    /**
     * Ruft den Chatverlauf ab.
     *
     * @param ChatId die Chat id
     * @param sender der Absender
     * @return
     * @author Keno O.
     * @since Sprint 2
     */
    Chat getChatHistory(String ChatId, User sender);
}
