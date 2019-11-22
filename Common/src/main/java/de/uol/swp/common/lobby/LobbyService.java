package de.uol.swp.common.lobby;

import java.util.List;

public interface LobbyService {

    /**
     * zum Erstellen der Lobbytabelle beim Login und zum Updaten, wenn eine Lobby erstellt/gelöscht wird
     *
     * @return eine Liste mit Lobbies
     */
    List<Lobby> retrieveAllLobbies();

    /**
     * zum Aktualisieren der Lobbytabelle, wenn ein User die Lobby betritt oder velässt
     *
     * @param name der Name der Lobby
     * @param joinLobby true: User tritt Lobby bei, false: User verlässt Lobby
     * @return eine Liste mit Lobbies
     */
    List<Lobby> updateAllLobbies(String name, boolean joinLobby);

}
