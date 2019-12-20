package de.uol.swp.common.lobby;

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
     */
    List<Lobby> retrieveAllLobbies();

    /**
     * Lobby beitreten
     *
     * @param lobbyName the lobby name
     * @param user      the user
     * @param lobbyID   the lobby id
     */
    void joinLobby(String lobbyName, UserDTO user, UUID lobbyID);

    /**
     * Lobby verlassen
     *
     * @param lobbyName the lobby name
     * @param user      the user
     * @param lobbyID   the lobby id
     */
    void leaveLobby(String lobbyName, UserDTO user, UUID lobbyID);

    /**
     * Verlassen aller beigetretenen Lobbies beim Logout
     *
     * @param user the user
     */
    void leaveAllLobbiesOnLogout(UserDTO user);

    /**
     * Retrieve all users in lobby array list.
     *
     * @param lobbyID the lobby id
     * @return the array list
     */
    ArrayList<LobbyUser> retrieveAllUsersInLobby(UUID lobbyID);

    /**
     * Sets lobby user status.
     *
     * @param LobbyName the lobby name
     * @param user      the user
     * @param Status    the status
     */
    void setLobbyUserStatus(String LobbyName, UserDTO user, boolean Status);

    void kickUser(String lobbyName, UserDTO gameOwner, UUID lobbyID, UserDTO userToKick);
}
