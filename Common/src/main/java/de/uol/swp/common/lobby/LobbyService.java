package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The interface Lobby service.
 */
public interface LobbyService {

    /**
     * Aktualiseren der Lobbytabelle
     *
     * @return eine Liste mit Lobbies
     * @author Julia
     * @since Sprint 1
     */
    List<Lobby> retrieveAllLobbies();

    /**
     * Lobby beitreten
     *
     * @param lobbyName Der Name der Lobby
     * @param user      Der beitretende User
     * @param lobbyID   Die ID der Lobby
     * @author Keno S.
     * @since Sprint 3
     */
    void joinLobby(String lobbyName, UserDTO user, UUID lobbyID);

    /**
     * Lobby verlassen
     *
     * @param lobbyName Der Name der Lobby
     * @param user      Der verlassende User
     * @param lobbyID   Die ID der Lobby
     * @author Keno S.
     * @since Sprint 3
     */
    void leaveLobby(String lobbyName, UserDTO user, UUID lobbyID);

    /**
     * Verlassen aller beigetretenen Lobbies beim Logout
     *
     * @param user Der ausloggende User
     * @author Keno S.
     * @since Sprint 3
     */
    void leaveAllLobbiesOnLogout(UserDTO user);

    /**
     * Gibt alle Benutzer der Lobby zurück
     *
     * @param lobbyID Die ID der Lobby
     * @return ArrayList der Benutzer
     * @author Keno O.
     * @since Sprint 3
     */
    ArrayList<LobbyUser> retrieveAllUsersInLobby(UUID lobbyID);

    /**
     * Setter für Status eines Lobbymitglieds
     *
     * @param LobbyName Der Name der Lobby
     * @param user      Der Benutzer
     * @param Status    Der Status als boolean
     * @author Keno O.
     * @since Sprint 3
     */
    void setLobbyUserStatus(String LobbyName, UserDTO user, boolean Status);

    /**
     * Setzt die maximale Spieleranzahl
     *
     * @param maxPlayer Die maximale Spieleranzahl
     * @param lobbyID Die LobbyID
     * @param loggedInUser Der User
     * @author Timo, Rike
     * @since Sprint 3
     */
    void setMaxPlayer(Integer maxPlayer, UUID lobbyID, User loggedInUser);
}
