package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import java.util.UUID;

/**
 * The type Retrieve all online users in lobby request.
 */
public class RetrieveAllOnlineUsersInLobbyRequest extends AbstractRequestMessage {

    private UUID lobbyId;
    /**
     * Instantiates a new Retrieve all online users in lobby request.
     * Alternative mit ID statt Name.
     *
     * @TODO Auf lobbyID oder lobbyName als Standard einigen.
     * @param LobbyID the lobby id
     * @author Marvin
     */
    public RetrieveAllOnlineUsersInLobbyRequest(UUID LobbyID) {
        this.lobbyId = LobbyID;
    }

    /**
     * Gets lobby id.
     *
     * @return the lobby id
     */
    public UUID getLobbyId() {
        return lobbyId;
    }
}
