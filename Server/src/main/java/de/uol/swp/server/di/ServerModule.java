package de.uol.swp.server.di;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;

@SuppressWarnings("UnstableApiUsage")
public class ServerModule extends AbstractModule {

    private final EventBus bus = new EventBus();
    private final UserStore store = new MainMemoryBasedUserStore();
    private final UserManagement userManagement = new UserManagement(store);
    private final ChatManagement chatManagement = new ChatManagement();
    private final LobbyManagement lobbyManagement = new LobbyManagement();
    private final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    private final GameService gameService = new GameService(bus, gameManagement, new AuthenticationService(bus, userManagement));

    /**
     * Alle Usermanagements und Eventbusse bekommen die gleichen Instanzen
     *
     * @author Marco
     * @since Start
     */
    @Override
    protected void configure() {
        // All usermanagements and eventbusses must be the same instance (!)
        bind(UserManagement.class).toInstance(userManagement);
        bind(LobbyManagement.class).toInstance(lobbyManagement);
        bind(GameManagement.class).toInstance(gameManagement);
        bind(UserStore.class).toInstance(store);
        bind(EventBus.class).toInstance(bus);
        bind(AuthenticationService.class).toInstance(new AuthenticationService(bus, userManagement));
        bind(GameService.class).toInstance(gameService);
    }
}



