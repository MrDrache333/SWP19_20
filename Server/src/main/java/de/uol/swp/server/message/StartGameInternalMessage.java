package de.uol.swp.server.message;

import java.util.UUID;

/**
 * Eine ServerinterneMessage die den benötigten Komponenenten mitteilt, dass ein Spiel gestartet worden ist.
 *
 * @author Ferit
 * @since Sprint 5
 */
public class StartGameInternalMessage extends AbstractServerInternalMessage {

    private final UUID lobbyID;

    public StartGameInternalMessage(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }
}
