package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

/**
 * The type Lobby dto.
 */
public class LobbyDTO implements Lobby, Serializable {

    private final String name;
    private User owner;
    private TreeMap<String, Boolean> readyStatus = new TreeMap<>();
    private Set<User> users = new TreeSet<>();
    private int players;
    private String lobbyPassword;
    /**
     * Eindeutige UUID für die Lobby um Lobbys mit gleichen Namen unterscheiden zu können Serverseitig.
     */
    private UUID lobbyID;



    /**
     * Instantiates a new Lobby dto.
     *
     * @param name    the name
     * @param creator the creator
     * @param lobbyID the lobby id
     * @param lobbyPassword the lobbyPassword
     */
    public LobbyDTO(String name, User creator, UUID lobbyID, String lobbyPassword) {
        this.name = name;
        this.owner = creator;
        this.users.add(creator);
        this.readyStatus.put(creator.getUsername(), false);
        this.lobbyID = lobbyID;
        this.players = 1;
        this.lobbyPassword = lobbyPassword;
    }

    /**
     * Instantiates a new Lobby dto.
     *
     * @param name    the name
     * @param creator the creator
     * @param lobbyID the lobby id
     * @param players the players
     */
    public LobbyDTO(String name, User creator, UUID lobbyID, String lobbyPassword, Set<User> users, int players) {
        this.name = name;
        this.owner = creator;
        this.readyStatus.put(creator.getUsername(), false);
        this.users = users;
        this.lobbyID = lobbyID;
        this.players = players;
        this.lobbyPassword = lobbyPassword;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void joinUser(User user) {
        if (users.size() < 4) {
            this.users.add(user);
            this.readyStatus.put(user.getUsername(), false);
            players++;
        }
        // TODO: Hier Fehlermeldung implementieren?
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
    public String getLobbyPassword(){
        return lobbyPassword;
    }

    /**
     * Gets readyStatus.
     *
     * @return Value of readyStatus.
     */
    public TreeMap<String, Boolean> getEveryReadyStatus() {
        return readyStatus;
    }
}