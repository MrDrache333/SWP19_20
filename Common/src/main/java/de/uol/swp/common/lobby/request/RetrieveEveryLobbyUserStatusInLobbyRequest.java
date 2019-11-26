package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * The type Retrieve every lobby user status in lobby.
 */
public class RetrieveEveryLobbyUserStatusInLobbyRequest extends AbstractRequestMessage {

    private String LobbyName;

    /**
     * Instantiates a new Retrieve every lobby user status in lobby.
     *
     * @param lobbyname the lobbyname
     */
    public RetrieveEveryLobbyUserStatusInLobbyRequest(String lobbyname) {
        this.LobbyName = lobbyname;
    }

    /**
     * Gets lobby name.
     *
     * @return the lobby name
     */
    public String getLobbyName() {
        return LobbyName;
    }
}
