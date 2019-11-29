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
     * @param name der Name der Lobby
     * @param user
     */
    void joinLobby(String name, User user, UUID id);

    /**
     * Lobby verlassen
     *
     * @param name der Name der Lobby
     * @param user
     */
    void leaveLobby(String name, User user, UUID id);
}