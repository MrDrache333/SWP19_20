package de.uol.swp.client;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.lobby.LobbyService;

public class AbstractPresenter {

    @Inject
    protected UserService userService;
    @Inject
    protected LobbyService lobbyService;


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
