package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

/**
 * Klasse des LeaveAllLobbiesOnLogoutRequest
 *
 * @author Paula
 * @since Sprint2
 */
public class LeaveAllLobbiesOnLogoutRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = -6775833613653810675L;

    public LeaveAllLobbiesOnLogoutRequest() {
    }

    /**
     * Übergibt den User, der die Lobbys verlässt
     *
     * @param user der ausgeloggt wird
     * @author Paula, Keno S, Julia, Marvin
     * @since Sprint 2
     */
    public LeaveAllLobbiesOnLogoutRequest(UserDTO user) {
        super.setUser(user);
    }

}
