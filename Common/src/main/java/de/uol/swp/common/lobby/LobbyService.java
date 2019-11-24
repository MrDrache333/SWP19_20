package de.uol.swp.common.lobby;

import java.util.List;

public interface LobbyService {

    /**
     * zum Aktualiseren der Lobbytabelle
     *
     * @return eine Liste mit Lobbies
     */
    List<Lobby> retrieveAllLobbies();

}
