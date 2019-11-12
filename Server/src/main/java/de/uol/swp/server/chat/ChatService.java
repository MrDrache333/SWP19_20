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
import de.uol.swp.server.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatService extends AbstractService {


    private static final Logger LOG = LogManager.getLogger(ChatService.class);

    private final ChatManagement chatManagement;

    @Inject
    public ChatService(EventBus eventBus, ChatManagement chatManagement) {
        super(eventBus);
        this.chatManagement = chatManagement;
    }


    @Subscribe
    public void onNewChatMessageRequest(NewChatMessageRequest request){
        if (LOG.isDebugEnabled()) {
            LOG.debug("New Message from " + request.getMessage().getSender().getUsername() + " in Chat " + request.getChatid());
        }
        AbstractResponseMessage returnMessage;
        try {
            chatManagement.addMessage(request.getChatid(), request.getMessage());
        } catch (ChatException e) {
            returnMessage = new ChatExceptionMessage(request.getMessage().getSender(), e);
            LOG.error(e);
            if (request.getSession().isPresent())
                returnMessage.setSession(request.getSession().get());
            if (request.getMessageContext().isPresent())
                returnMessage.setMessageContext(request.getMessageContext().get());
            post(returnMessage);
            return;
        }
        NewChatMessage message = new NewChatMessage(request.getChatid(), request.getMessage());
        if (request.getMessageContext().isPresent())
            message.setMessageContext(request.getMessageContext().get());
        post(message);

    }

    @Subscribe
    public void onChatHistoryRequest(ChatHistoryRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("New ChatHistoryRequest from " + request.getSender().getUsername() + " for Chat " + request.getChatId());
        }
        AbstractResponseMessage returnMessage;
        try {
            returnMessage = new ChatResponseMessage(chatManagement.getChat(request.getChatId()));
        } catch (ChatException e) {
            returnMessage = new ChatExceptionMessage(request.getSender(), e);
            LOG.error(e);
        }
        if (request.getSession().isPresent())
            returnMessage.setSession(request.getSession().get());
        returnMessage.setSession(request.getSession().get());
        if (request.getMessageContext().isPresent())
            returnMessage.setMessageContext(request.getMessageContext().get());
        post(returnMessage);
    }



}
