package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * The type Retrieve all online users in lobby request.
 */
public class RetrieveAllOnlineUsersInLobbyRequest extends AbstractRequestMessage {

    private String LobbyId;

    /**
     * Instantiates a new Retrieve all online users in lobby request.
     *
     * @param LobbyID the lobby id
     */
    public RetrieveAllOnlineUsersInLobbyRequest(String LobbyID) {
        this.LobbyId = LobbyID;
    }

    /**
     * Gets lobby id.
     *
     * @return the lobby id
     */
    public String getLobbyId() {
        return LobbyId;
    }
}
