package de.uol.swp.server.chat;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.chat.message.ChatExceptionMessage;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Der Chatservice. Verantwortlich für die entgegennahme der Chat-spezifischen Nachrichten auf dem Eventbus.
 *
 * @author KenoO
 * @since Sprint 1
 */
@SuppressWarnings("UnstableApiUsage, unused")
public class ChatService extends AbstractService {


    private static final Logger LOG = LogManager.getLogger(ChatService.class);

    private final ChatManagement chatManagement;
    private final AuthenticationService authenticationService;

    /**
     * Erstellt einen neuen Chatservice.
     *
     * @param eventBus                  Der verwendete EventBus.
     * @param chatManagement            Das verwendete ChatManagement.
     * @param authenticationService     Der verwendete AuthenticationService.
     */
    @Inject
    public ChatService(EventBus eventBus, ChatManagement chatManagement, AuthenticationService authenticationService) {
        super(eventBus);
        this.chatManagement = chatManagement;
        this.authenticationService = authenticationService;
    }

    /**
     * Wenn eine neue ChatNachricht von einem CLient auf dem Bus gesendet wurde.
     *
     * @param req Der Request, gesendet wurde.
     */
    @Subscribe
    private void onNewChatMessageRequest(NewChatMessageRequest req) {
        LOG.debug("Neue Nachricht von " + req.getMessage().getSender().getUsername() + " in Chat " + req.getChatid());
        AbstractResponseMessage returnMessage;
        if (req.getMessage().getMessage().equals("")) return;
        try {
            //Nachricht im ChatManagement versuchen hinzuzufügen
            chatManagement.addMessage(req.getChatid(), req.getMessage());
        } catch (ChatException e) {
            returnMessage = new ChatExceptionMessage(req.getMessage().getSender(), e);
            LOG.error(e);

            //Wenn eine Session in dem Request übergeben wurde, dann übernehme diese
            if (req.getSession().isPresent())
                returnMessage.setSession(req.getSession().get());

            //Wenn ein Kontext in dem Request übergeben wurde, dann übernehme diesen
            if (req.getMessageContext().isPresent())
                returnMessage.setMessageContext(req.getMessageContext().get());
            post(returnMessage);
            return;
        }
        //Die neue Chatnachricht an alle Clients senden
        NewChatMessage message = new NewChatMessage(req.getChatid(), req.getMessage());
        authenticationService.sendToLoggedInPlayers(message);
    }
}