package de.uol.swp.server.chat;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The type Chat management.
 */
public class ChatManagement extends AbstractChatManagement {

    private SortedMap<Long,Chat> Chats = new TreeMap<>();

    /**
     * Get the Chat with chatId.
     *
     * @param chatId the chat id
     * @return the chat
     */
    public Chat getChat(long chatId){
        return Chats.getOrDefault(chatId,null);
    }

    /**
     * Create a new Chat.
     *
     * @return the ChatId
     */
    public long createChat(){
        //TODO Chat erstellen und Id zurück geben
        return 0;
    }

}
