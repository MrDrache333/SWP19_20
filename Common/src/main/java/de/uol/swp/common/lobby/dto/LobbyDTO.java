package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class LobbyDTO implements Lobby, Serializable {

    private final String name;
    private User owner;
    private Set<User> users = new TreeSet<>();
    private int players;
    /**
     * Eindeutige UUID für die Lobby um Lobbys mit gleichen Namen unterscheiden zu können Serverseitig.
     */
    private UUID lobbyID;


    public LobbyDTO(String name, User creator, UUID lobbyID) {
        this.name = name;
        this.owner = creator;
        this.users.add(creator);
        this.lobbyID = lobbyID;
        this.players = 1;
    }

    public LobbyDTO(String name, User creator, UUID lobbyID, Set<User> users, int players) {
        this.name = name;
        this.owner = creator;
        this.users = users;
        this.lobbyID = lobbyID;
        this.players = players;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void joinUser(User user) {
        if (users.size() < 4) {
            this.users.add(user);
            players++;
        }
        // TODO: Hier Fehlermeldung implementieren?
    }

    @Override
    public void leaveUser(User user) {
        if (users.size() == 1) {
            throw new IllegalArgumentException("Lobby must contain at least one user!");
        }
        if (users.contains(user)) {
            this.users.remove(user);
            players--;
            if (this.owner.equals(user)) {
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
        return Collections.unmodifiableSet(users);
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
}
