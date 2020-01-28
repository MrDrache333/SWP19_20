package de.uol.swp.server.chat;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.chat.message.ChatExceptionMessage;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.request.ChatHistoryRequest;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.message.ResponseMessage;
import de.uol.swp.server.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Der Chatservice. Verantwortlich für die entgegennahme der Chat-spezifischen Nachrichten auf dem Eventbus.
 *
 * @author KenoO
 * @since Sprint 1
 */
@SuppressWarnings("UnstableApiUsage")
public class ChatService extends AbstractService {


    private static final Logger LOG = LogManager.getLogger(ChatService.class);

    private final ChatManagement chatManagement;

    /**
     * Erstellt einen neuen Chat service.
     *
     * @param eventBus       Der verwendete EventBus
     * @param chatManagement Das verwendete ChatManagement
     */
    @Inject
    public ChatService(EventBus eventBus, ChatManagement chatManagement) {
        super(eventBus);
        this.chatManagement = chatManagement;
    }


    /**
     * Wenn eine neue ChatNachricht von einem CLient auf dem Bus gesendet wurde.
     *
     * @param request Der Request, gesendet wurde
     */
    @Subscribe
    private void onNewChatMessageRequest(NewChatMessageRequest request) {
        LOG.debug("New Message from " + request.getMessage().getSender().getUsername() + " in Chat " + request.getChatid());
        AbstractResponseMessage returnMessage;
        if (request.getMessage().getMessage().equals("")) return;
        try {
            //Nachricht im ChatManagement versuchen hinzuzufügen
            chatManagement.addMessage(request.getChatid(), request.getMessage());
        } catch (ChatException e) {
            returnMessage = new ChatExceptionMessage(request.getMessage().getSender(), e);
            LOG.error(e);

            //Wenn eine Session in dem Request übergeben wurde, dann übernehme diese
            if (request.getSession().isPresent())
                returnMessage.setSession(request.getSession().get());

            //Wenn ein Kontext in dem Request übergeben wurde, dann übernehme diesen
            if (request.getMessageContext().isPresent())
                returnMessage.setMessageContext(request.getMessageContext().get());
            post(returnMessage);
            return;
        }
        //Die neue Chatnachricht an alle Clients senden an alle Clients senden
        NewChatMessage message = new NewChatMessage(request.getChatid(), request.getMessage());
        post(message);
    }

    /**
     * Wenn eine Anfrage für die gesamte Historie eines Chats von einem Client an den Server gesendet wurde.
     *
     * @param request Der Request, der gesendet wurde
     */
    @Subscribe
    private void onChatHistoryRequest(ChatHistoryRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("New ChatHistoryRequest from " + request.getSender().getUsername() + " for Chat " + request.getChatId());
        }
        ResponseMessage returnMessage;
        try {
            //Versuchen, die Chathistorie für den angeforderten Chat abzurufen
            returnMessage = new ChatResponseMessage(chatManagement.getChat(request.getChatId()), request.getSender().getUsername());
        } catch (ChatException e) {
            //Bei fehlern eine Fehlernachricht an den Client zurück übermitteln
            returnMessage = new ChatExceptionMessage(request.getSender(), e);
            LOG.error(e);
        }
        returnMessage.initWithMessage(request);
        //Wenn ein Kontext in dem Request übergeben wurde, dann übernehme diesen
        if (request.getMessageContext().isPresent())
            returnMessage.setMessageContext(request.getMessageContext().get());
        post(returnMessage);
    }
}