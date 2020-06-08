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
     * @param lobbyID Die ID der Lobby
     * @param user    Der beitretende User
     * @author Keno S., Marvin
     * @since Sprint 3
     */
    void joinLobby(UUID lobbyID, UserDTO user);

    /**
     * Lobby verlassen
     *
     * @param lobbyID Die ID der Lobby
     * @param user    Der verlassende User
     * @author Keno S., Marvin
     * @since Sprint 3
     */
    void leaveLobby(UUID lobbyID, UserDTO user);

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
     * @param lobbyID Die ID der Lobby
     * @param user    Der Benutzer
     * @param Status  Der Status als boolean
     * @author Keno O.
     * @since Sprint 3
     */
    void setLobbyUserStatus(UUID lobbyID, UserDTO user, boolean Status);

    /**
     * Setzt die maximale Spieleranzahl
     *
     * @param maxPlayer    Die maximale Spieleranzahl
     * @param lobbyID      Die LobbyID
     * @param loggedInUser Der User
     * @author Timo, Rike
     * @since Sprint 3
     */
    void setMaxPlayer(UUID lobbyID, User loggedInUser, Integer maxPlayer);

    /**
     * Entfernt einen Nutzer aus einer Lobby
     *
     * @param lobbyID    Die ID der Lobby
     * @param gameOwner  Der Besitzer der Lobby
     * @param userToKick Der zu entfernende Nutzer
     * @author Marvin
     */
    void kickUser(UUID lobbyID, UserDTO gameOwner, UserDTO userToKick);
}
