package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

/**
 * Klasse des LeaveAllLobbiesOnLogoutRequest
 * @author Paula
 * @since Sprint2
 */
public class LeaveAllLobbiesOnLogoutRequest extends AbstractLobbyRequest {

    private UserDTO user;

    public LeaveAllLobbiesOnLogoutRequest() {
    }

    /**
     * Übergibt den User, der die Lobbys verlässt
     *
     * @param user der ausgeloggt wird
     * @author Paula, Keno S, Julia
     * @since Sprint 2
     */
    public LeaveAllLobbiesOnLogoutRequest(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }
}
