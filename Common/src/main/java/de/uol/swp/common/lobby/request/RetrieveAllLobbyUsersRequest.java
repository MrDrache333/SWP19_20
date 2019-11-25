package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * Subscriber: server.lobby.LobbyService.onRetrieveAllLobbyUsersRequest
 *
 * @author Marvin
 */

public class RetrieveAllLobbyUsersRequest extends AbstractRequestMessage {
    private String lobbyName;

    public RetrieveAllLobbyUsersRequest() {
    }

    public RetrieveAllLobbyUsersRequest(String name) {
        this.lobbyName = name;
    }

    public String getName() {
        return lobbyName;
    }

    public void setName(String name) {
        this.lobbyName = name;
    }
}
