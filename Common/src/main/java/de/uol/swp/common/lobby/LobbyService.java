package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;

import java.util.List;
import java.util.UUID;

public interface LobbyService {

    /**
     * Aktualiseren der Lobbytabelle
     *
     * @return eine Liste mit Lobbies
     */
    List<Lobby> retrieveAllLobbies();

    /**
     * Lobby beitreten
     *
     * @param name
     * @param user
     * @param lobbyID
     */
    void joinLobby(String name, User user, UUID lobbyID);

    /**
     * Lobby verlassen
     *
     * @param name
     * @param user
     * @param lobbyID
     */
    void leaveLobby(String name, User user, UUID lobbyID);

    /**
     * Verlassen aller beigetretenen Lobbies beim Logout
     *
     * @param user
     */
    void leaveAllLobbiesOnLogout(User user);
}