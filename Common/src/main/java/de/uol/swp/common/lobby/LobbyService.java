package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface LobbyService {

    /**
     * zum Aktualiseren der Lobbytabelle
     *
     * @return eine Liste mit Lobbies
     */
    List<Lobby> retrieveAllLobbies();

    ArrayList<LobbyUser> retrieveAllUsersInLobby(String LobbyName);

    void setLobbyUserStatus(String LobbyName, User user, boolean Status);

    /**
     * @author Timo, Rike
     * @since Sprint 3
     * @implNote Notwendige Ergänzung im LobbyService Interface
     */
    void setMaxPlayer(Integer maxPlayer, UUID lobbyID);
}
