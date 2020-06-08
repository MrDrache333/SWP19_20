package de.uol.swp.common.lobby.message;

import java.util.UUID;

/**
 * Message nachdem der inGame-Status der Lobby nach Spielende wieder auf false gesetzt wurde
 *
 * @author Julia
 * @since Sprint 6
 */
public class UpdatedInGameMessage extends AbstractLobbyMessage {

    private UUID lobbyID;

    public UpdatedInGameMessage(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    @Override
    public UUID getLobbyID() {
        return lobbyID;
    }
}
