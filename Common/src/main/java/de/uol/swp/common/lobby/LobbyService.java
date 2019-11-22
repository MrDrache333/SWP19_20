package de.uol.swp.common.lobby;

import java.util.List;
import java.util.UUID;

public interface LobbyService {

    /**
     * zum Erstellen der Lobby-Tabelle beim Login
     * @return eine Liste aus Lobbies
     */
    List<Lobby> retrieveAllLobbies();

    /**
     * zum Aktualisieren der Lobbytabelle, wenn ein User die Lobby betritt oder velässt
     * @param name
     * @param value true: User tritt Lobby bei, false: User verlässt Lobby
     * @return
     */
    List<Lobby> updateAllLobbies(String name, boolean value);

}
