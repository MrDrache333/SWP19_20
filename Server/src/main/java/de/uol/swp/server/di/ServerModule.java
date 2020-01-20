package de.uol.swp.server.di;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;

public class ServerModule extends AbstractModule {

    private final EventBus bus = new EventBus();
    private final UserStore store = new MainMemoryBasedUserStore();

    @Override
    protected void configure() {
        // All usermanagements and eventbusses must be the same instance (!)
        bind(ChatManagement.class).toInstance(new ChatManagement());
        bind(GameManagement.class).toInstance(new GameManagement());
        bind(UserStore.class).toInstance(store);
        bind(EventBus.class).toInstance(bus);

    }
}



