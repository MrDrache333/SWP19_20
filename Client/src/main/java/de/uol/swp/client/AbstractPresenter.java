package de.uol.swp.client;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;

public class AbstractPresenter {
    @Inject
    protected UserService userService;
    @Inject
    protected LobbyService lobbyService;
    @Inject
    protected ChatService chatService;

    protected User loggedInUser;
    protected EventBus eventBus;

    /**
     * Es wird ein neuer Eventbus gesetzt.
     *
     * @param eventBus Der neue Eventbus
     * @author Marco
     * @since Start
     */
    @Inject
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    /**
     * Es wird der gesetzte Eventbus gelöscht
     *
     * @author Marco
     * @since Start
     */
    public void clearEventBus() {
        this.eventBus.unregister(this);
        this.eventBus = null;
    }
}
