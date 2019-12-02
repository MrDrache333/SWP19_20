package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 *
 */
public class LeaveAllLobbiesOnLogoutRequest extends AbstractLobbyRequest {
    private UUID lobbyID;

    public LeaveAllLobbiesOnLogoutRequest() {


    }
    public LeaveAllLobbiesOnLogoutRequest( User user) {

        this.lobbyID = lobbyID;
    }
}
