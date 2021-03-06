package de.uol.swp.server;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.message.Message;

@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractService {

    private final EventBus bus;

    public AbstractService(EventBus bus) {
        this.bus = bus;
        bus.register(this);
    }

    protected void post(Message message) {
        bus.post(message);
    }

    public EventBus getBus() {
        return bus;
    }

}
