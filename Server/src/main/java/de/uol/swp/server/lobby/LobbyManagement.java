package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class LobbyManagement {
    static final Logger LOG = LogManager.getLogger(LobbyManagement.class);
    /**
     * Mapping lobbys zum lobbynamen.
     */
    private final Map<String, Lobby> lobbies = new HashMap<>();

    /**
     * Mapping lobbynamen zur UUID.
     *
     * @author Marvin
     * @since Sprint3
     */
    private Map<UUID, String> lobbyNames = new HashMap<>();

    /**
     * Erstellt eine Lobby mit den übergebenen Parametern. Überprüft, ob die Lobby mit dem Namen schon existiert.
     *
     * @author Paula, Haschem, Ferit, Rike
     * @version 0.1
     * lobbyID hat folgende Form: 067e6162-3b6f-4ae2-a171-2470b63dff00  (Beispiel) / UUID Object
     */

    public UUID createLobby(String name, String lobbyPassword, User owner) {
        if (lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " already exists!");
        }
        // Erstellen der UUID für die Lobbys.
        UUID lobbyID = UUID.randomUUID();
        LOG.info("Die Lobby " + name + " hat folgende UUID erstellt bekommen: " + lobbyID);
        lobbies.put(name, new LobbyDTO(name, owner, lobbyID, lobbyPassword));
        lobbyNames.put(lobbyID, name);
        return lobbyID;
    }

    /**
     * Löscht die Lobby und überprüft, ob der Lobbyname noch existiert.
     *
     * @param name der Lobbyname
     */
    public void dropLobby(String name) {
        if (!lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " not found!");
        }
        lobbies.remove(name);
        LOG.info("Lobby " + name + "removed");
    }

    /**
     * Getter um eine Lobby zurückzugeben.
     *
     * @param name der Lobbyname
     * @return null
     */
    public Optional<Lobby> getLobby(String name) {
        Lobby lobby = lobbies.get(name);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    /**
     * Handling das ein User die Lobby verlässt. Überprüft, ob die gegebene Lobby existiert.
     *
     * @param name der Lobbyname
     * @param user der User, welches die Lobby verlässt.
     * @return Gibt wahr zurück, wenn der User aus der Lobby entfernt worden ist.
     */
    public boolean leaveLobby(String name, User user) {
        Optional<Lobby> lobby = this.getLobby(name);
        if (lobby.isPresent()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("User " + user.getUsername() + " is leaving lobby " + name);
            }
            lobby.get().leaveUser(user);
            if (lobby.get().getPlayers() == 0) {
                this.dropLobby(name);
            }
            return true;
        }
        return false;
    }

    /**
     * Aktualisiert jede Lobby, in der der aktualisierte User drinne ist.
     *
     * @param updatedUser der aktualisierte User
     * @param oldUser     der alte User
     * @author Julia
     * @since Sprint4
     */
    public void updateLobbies(UserDTO updatedUser, UserDTO oldUser) {
        Map<String, Lobby> updatedLobbies = new HashMap<>();
        for (Lobby lobby : lobbies.values()) {
            List<String> userNames = new ArrayList<>();
            List<User> users = new ArrayList<>(lobby.getUsers());
            users.forEach(user -> userNames.add(user.getUsername()));
            if (userNames.contains(oldUser.getUsername())) {
                User updatedOwner = lobby.getOwner();
                //ggf. Owner aktualisieren
                if (lobby.getOwner().getUsername().equals(oldUser.getUsername())) {
                    updatedOwner = updatedUser;
                }
                //ReadyStatus Set aktualisieren
                lobby.getEveryReadyStatus().put(updatedUser.getUsername(), lobby.getReadyStatus(oldUser));
                lobby.getEveryReadyStatus().remove(oldUser.getUsername());
                //Userliste aktualisieren
                List<User> updatedUsers = new ArrayList<>(lobby.getUsers());
                updatedUsers.remove(oldUser);
                updatedUsers.add(updatedUser);
                Set<User> newUsers = new TreeSet<>(updatedUsers);
                Lobby lobbyToUpdate = new LobbyDTO(lobby.getName(), updatedOwner, lobby.getLobbyID(), lobby.getLobbyPassword(), newUsers, lobby.getPlayers(), lobby.getMaxPlayer());
                updatedLobbies.put(lobbyToUpdate.getName(), lobbyToUpdate);
            }
        }
        updatedLobbies.entrySet().forEach(l -> lobbies.replace(l.getKey(), l.getValue()));
    }

    /**
     * Benutzer wird aus der Lobby-Liste entfernt wenn der Spielbesitzer ihn gekickt hat.
     *
     * @param name
     * @param userToKick
     * @param owner
     * @author Darian
     * @since sprint4
     * @return Boolean ob der Kick gerechtfertigt ist
     */
    public boolean kickUser(String name, User userToKick, User owner) {
        Optional<Lobby> lobby = this.getLobby(name);
        if (lobby.isPresent() && lobby.get().getOwner().getUsername().equals(owner.getUsername())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("User " + userToKick.getUsername() + " is kicked out of the lobby " + name);
            }
            lobby.get().leaveUser(userToKick);
            if (lobby.get().getPlayers() == 0) {
                this.dropLobby(name);
            }
            return true;
        }
        return false;
        // TODO: error handling not existing lobby
    }

    /**
     * getter um alle Lobbys zurückzugeben.
     *
     * @return
     */
    public Collection<Lobby> getLobbies() {
        return lobbies.values();
    }

    /**
     * Getter für den Namen der Lobby.
     *
     * @param lobbyID Die übergebene Lobby ID
     * @author Marvin
     * @since Sprint3
     */
    public Optional<String> getName(UUID lobbyID) {
        return lobbyNames.get(lobbyID) != null ? Optional.of(lobbyNames.get(lobbyID)) : Optional.empty();
    }

    /**
     * Getter für den Lobby Besitzer.
     *
     * @param lobbyID Die ID der Lobby, von der der Besitzer zurückgegeben werden soll.
     * @author Timo, Rike
     * @since Sprint 3
     */
    public User getLobbyOwner(UUID lobbyID) {
        return lobbies.get(lobbyNames.get(lobbyID)).getOwner();
    }

    /**
     * Setzt die Anzahl der Maximalen Spieler der Lobby und gibt, wenn es erfolgreich wahr ein true zurück.
     *
     * @param lobbyID        Die LobbyID, der Lobby wo die Max-Player geändert werden soll.
     * @param loggedInUser   Der User, welcher die setMaxPlayerRequest an den Server geschickt hat.
     * @param maxPlayerValue Der Wert, welcher verändert wird.
     * @author Timo, Rike
     * @since Sprint 3
     */
    public boolean setMaxPlayer(Integer maxPlayerValue, UUID lobbyID, User loggedInUser) {
        String tmp = lobbyNames.get(lobbyID);

        if (lobbies.get(tmp).getOwner().equals(loggedInUser)) {
            lobbies.get(tmp).setMaxPlayer(maxPlayerValue);
            return true;
        } else {
            return false;
        }
    }
}