package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Die LobbyDTO
 *
 * @author Marco
 * @since Start
 */
public class LobbyDTO implements Lobby, Serializable {

    private static final long serialVersionUID = 998701048176852816L;
    private final String name;
    private User owner;
    private TreeMap<String, Boolean> readyStatus = new TreeMap<>();
    private Set<User> users = new TreeSet<>();
    private int players;
    private String lobbyPassword;
    private UUID lobbyID;
    private Integer maxPlayer;


    /**
     * Insatnziiert eine neue LobbyDTO.
     *
     * @param name          der Name
     * @param creator       der Ersteller
     * @param lobbyID       die LobbyID, um Lobbys mit gleichem Namen unterscheiden zu können - Serverseitig
     * @param lobbyPassword das Lobbypasswort
     * @author Marco, Paula, Julia, Timo, Rike
     * @since Start
     */
    public LobbyDTO(String name, User creator, UUID lobbyID, String lobbyPassword) {
        this.name = name;
        this.owner = creator;
        this.users.add(creator);
        this.readyStatus.put(creator.getUsername(), false);
        this.lobbyID = lobbyID;
        this.players = 1;
        this.lobbyPassword = lobbyPassword;
        this.maxPlayer = 4;
    }

    /**
     * Insatnziiert eine LobbyDTO für die Lobbytable im MainmMenu
     *
     * @param name          der Name der Lobby
     * @param creator       der Ersteller
     * @param lobbyID       die LobbyId, um Lobbys mit gleichem Namen unterscheiden zu können - Serverseitig
     * @param lobbyPassword das Lobbypasswort
     * @param players       die Spieler
     * @param maxPlayer     die maximale Spieleranzahl
     * @author Marco, Julia, Rike, Timo
     * @since Start
     */
    public LobbyDTO(String name, User creator, UUID lobbyID, String lobbyPassword, Set<User> users, int players, Integer maxPlayer) {
        this.name = name;
        this.owner = creator;
        this.readyStatus.put(creator.getUsername(), false);
        this.users = users;
        this.lobbyID = lobbyID;
        this.players = players;
        this.lobbyPassword = lobbyPassword;
        this.maxPlayer = maxPlayer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void joinUser(User user) {
        if (users.size() < maxPlayer) {
            this.users.add(user);
            this.readyStatus.put(user.getUsername(), false);
            players++;
        }
    }

    @Override
    public void leaveUser(User user) {
        if (users.contains(user)) {
            this.users.remove(user);
            this.readyStatus.remove(user.getUsername());
            players--;
            if (this.owner.equals(user) && users.size() > 0) {
                updateOwner(users.iterator().next());
            }
        }
    }

    @Override
    public void updateOwner(User user) {
        if (!this.users.contains(user)) {
            throw new IllegalArgumentException("User " + user.getUsername() + "not found. Owner must be member of lobby!");
        }
        this.owner = user;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public Set<User> getUsers() {
        Set<User> Users = new TreeSet<>();
        Users.addAll(users);
        return Users;
    }

    @Override
    public UUID getLobbyID() {
        return lobbyID;
    }

    @Override
    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    @Override
    public int getPlayers() {
        return players;
    }

    @Override
    public void setReadyStatus(User user, boolean status) {
        if (readyStatus.containsKey(user.getUsername())) {
            readyStatus.replace(user.getUsername(), status);
        }
    }

    @Override
    public boolean getReadyStatus(User user) {
        if (!readyStatus.containsKey(user.getUsername())) return false;
        return readyStatus.get(user.getUsername());
    }

    @Override
    public String getLobbyPassword() {
        return lobbyPassword;
    }

    /**
     * Gibt den readyStatus wieder
     *
     * @return der jeweilige readyStatus.
     * @author Marco
     * @sind Start
     */
    public TreeMap<String, Boolean> getEveryReadyStatus() {
        return readyStatus;
    }

    @Override
    public Integer getMaxPlayer() {
        return this.maxPlayer;
    }

    /**
     * Setzt den Max Player Wert bzw. gibt ihn zurück.
     *
     * @param maxPlayer die maximale Spieleranzahl die gesetzt werden soll
     * @author Timo, Rike
     * @since Sprint 3
     */
    @Override
    public void setMaxPlayer(Integer maxPlayer) {
        this.maxPlayer = maxPlayer;
    }


}