package de.uol.swp.common.lobby.request;

import java.util.UUID;

/**
 * Request um den inGame-Status der Lobby bei Spielende wieder auf false zu setzen
 *
 * @author Julia
 * @since Sprint 6
 */
public class UpdateInGameRequest extends AbstractLobbyRequest {

    private final UUID lobbyID;

    public UpdateInGameRequest(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    @Override
    public UUID getLobbyID() {
        return lobbyID;
    }
}
