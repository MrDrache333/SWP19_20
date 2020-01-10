package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;

import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

/**
 * The interface Lobby.
 */
public interface Lobby {

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Update owner.
     *
     * @param user the user
     */
    void updateOwner(User user);

    /**
     * Gets owner.
     *
     * @return the owner
     */
    User getOwner();

    /**
     * Join user.
     *
     * @param user the user
     */
    void joinUser(User user);

    /**
     * Leave user.
     *
     * @param user the user
     */
    void leaveUser(User user);

    /**
     * Gets users.
     *
     * @return the users
     */
    Set<User> getUsers();

    /**
     * Gets lobby id.
     *
     * @return the lobby id
     */
    UUID getLobbyID();

    /**
     * Sets lobby id.
     *
     * @param lobbyID the lobby id
     */
    void setLobbyID(UUID lobbyID);

    /**
     * Gets players.
     *
     * @return the players
     */
    int getPlayers();

    /**
     * Sets ready status.
     *
     * @param user   the user
     * @param status the status
     */
    void setReadyStatus(User user, boolean status);

    /**
     * Gets ready status.
     *
     * @param user the user
     * @return the ready status
     */
    boolean getReadyStatus(User user);

    /**
     * @author Timo, Rike
     * @since Sprint 3
     * @implNote Notwendige Interface implementierung für die set & get MaxPlayer Methoden
     */
    Integer getMaxPlayer();
    void setMaxPlayer(Integer maxPlayer);

    /**
     * Gets every ready status.
     *
     * @return every ready status
     */
    TreeMap<String, Boolean> getEveryReadyStatus();


    /**
     * @return gibt das Passwort einer Lobby zurück
     */
    String getLobbyPassword();
}
