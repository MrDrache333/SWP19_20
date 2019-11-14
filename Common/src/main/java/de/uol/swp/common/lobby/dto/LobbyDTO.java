package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class LobbyDTO implements Lobby {

    private final String name;
    private User owner;
    private Set<User> users = new TreeSet<>();
    private int lobbyID;


    public LobbyDTO(String name, User creator, int lobbyID) {
        this.name = name;
        this.owner = creator;
        this.users.add(creator);
        this.lobbyID = lobbyID;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void joinUser(User user) {
        if (users.size() < 4) {
            this.users.add(user);
        }
       // ToDo = Fehlermeldung?
    }

    @Override
    public void leaveUser(User user) {
        if (users.size() == 1) {
            throw new IllegalArgumentException("Lobby must contain at least one user!");
        }
        if (users.contains(user)) {
            this.users.remove(user);
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
    public int getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(int lobbyID) {
        this.lobbyID = lobbyID;
    }


}
