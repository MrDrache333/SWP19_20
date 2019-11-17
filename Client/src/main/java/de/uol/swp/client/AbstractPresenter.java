package de.uol.swp.client;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;

public class AbstractPresenter {


    protected static User loggedInUser;

    @Inject
    protected UserService userService;

    @Inject
    protected ChatService chatService;

    protected EventBus eventBus;

    @Inject
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    public void clearEventBus(){
        this.eventBus.unregister(this);
        this.eventBus = null;
    }
}
