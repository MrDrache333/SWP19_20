package de.uol.swp.client.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

public class OpenJoinLobbyRequest {
    private User user;

    private Lobby lobby;

    /**
     * Der Konstruktor der OpenLobbyCreateRequest
     *
     * @param user den übergebenen User
     * @author Paula
     * @since Sprint 7
     */
    public OpenJoinLobbyRequest(User user, Lobby lobby) {
        this.user = user;
        this.lobby = lobby;
    }

    /**
     * Gibt den User zurück
     *
     * @return den gegebenen User
     * @author Paula
     * @since Sprint 7
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die Lobby zurück.
     *
     * @return lobby die Lobby
     * @author Paula
     * @since Sprint 7
     */
    public Lobby getLobby() {
        return lobby;
    }
}





