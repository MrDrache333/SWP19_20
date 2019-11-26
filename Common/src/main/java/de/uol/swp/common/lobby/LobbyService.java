package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.List;

public interface LobbyService {

    /**
     * zum Aktualiseren der Lobbytabelle
     *
     * @return eine Liste mit Lobbies
     */
    List<Lobby> retrieveAllLobbies();

    ArrayList<LobbyUser> retrieveAllUsersInLobby(String LobbyName);

    void setLobbyUserStatus(String LobbyName, User user, boolean Status);
}
