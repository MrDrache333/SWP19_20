package de.uol.swp.server.chat;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.chat.NewChatMessageRequest;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.usermanagement.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatService extends AbstractService {


    private static final Logger LOG = LogManager.getLogger(UserService.class);

    private final ChatManagement chatManagement;

    @Inject
    public ChatService(EventBus eventBus, ChatManagement chatManagement) {
        super(eventBus);
        this.chatManagement = chatManagement;
    }


    @Subscribe
    public void onNewChatMessageRequest(NewChatMessageRequest request){

    }



}
