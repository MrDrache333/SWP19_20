package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.lobby.request.UpdateAllOnlineLobbiesRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class LobbyService implements de.uol.swp.common.lobby.LobbyService {

    private static final Logger LOG = LogManager.getLogger(LobbyService.class);
    private final EventBus bus;

    @Inject
    public LobbyService(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public List<Lobby> retrieveAllLobbies() {
        RetrieveAllOnlineLobbiesRequest cmd = new RetrieveAllOnlineLobbiesRequest();
        bus.post(cmd);
        return null;
    }

    //TODO name nach Möglichkeit durch UUID der Lobby ersetzen
    // (muss beim Beitreten oder Verlassen einer Lobby mitgesendet werden),
    // da Name nicht eindeutig
    @Override
    public List<Lobby> updateAllLobbies(String name, boolean joinLobby) {
        UpdateAllOnlineLobbiesRequest cmd = new UpdateAllOnlineLobbiesRequest(name, joinLobby);
        bus.post(cmd);
        return null;
    }
}
