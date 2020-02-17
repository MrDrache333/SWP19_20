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
     * Map von UUID zu Lobby
     *
     * @author Marvin
     * @since Sprint5
     */
    private final Map<UUID, Lobby> lobbies = new HashMap<>();

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
        lobbies.put(lobbyID, new LobbyDTO(name, owner, lobbyID, lobbyPassword));
        return lobbyID;
    }

    /**
     * Löscht die Lobby, falls eine Lobby mit der ID existiert.
     *
     * @param id die LobbyID.
     * @throws IllegalArgumentException wenn keine Lobby mit der ID gefunden werden konnte.
     * @author Marvin
     * @since Sprint5
     */
    public void dropLobby(UUID id) {
        if (!lobbies.containsKey(id)) {
            throw new IllegalArgumentException("LobbyID not found! ID: " + id);
        }
        lobbies.remove(id);
        LOG.info("Lobby " + getLobby(id) + "removed");
    }

    /**
     * Getter für Lobby als Optional.
     *
     * @param id der Lobbyname
     * @return Optional mit Lobby wenn vorhanden, sonst leere Optional
     * @author Marvin
     * @since Sprint5
     */
    public Optional<Lobby> getLobby(UUID id) {
        Lobby lobby = lobbies.get(id);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    /**
     * Handling das ein User die Lobby verlässt. Überprüft, ob die gegebene Lobby existiert.
     *
     * @param id   die LobbyID
     * @param user der User, welches die Lobby verlässt.
     * @return Gibt wahr zurück, wenn der User aus der Lobby entfernt worden ist.
     * @author Marvin
     */
    public boolean leaveLobby(UUID id, User user) {
        Optional<Lobby> lobby = this.getLobby(id);
        if (lobby.isPresent()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("User " + user.getUsername() + " is leaving lobby " + getLobby(id));
            }
            lobby.get().leaveUser(user);
            if (lobby.get().getPlayers() == 0) {
                this.dropLobby(id);
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
     * @author Julia, Marvin
     * @since Sprint4
     */
    public void updateLobbies(UserDTO updatedUser, UserDTO oldUser) {
        Map<UUID, Lobby> updatedLobbies = new HashMap<>();
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
                updatedLobbies.put(lobbyToUpdate.getLobbyID(), lobbyToUpdate);
            }
        }
        updatedLobbies.entrySet().forEach(l -> lobbies.replace(l.getKey(), l.getValue()));
    }

    /**
     * Benutzer wird aus der Lobby-Liste entfernt wenn der Spielbesitzer ihn gekickt hat.
     *
     * @param id         die LobbyID
     * @param userToKick der zu entfernende Benutzer
     * @param owner      der Lobbybesitzer
     * @return Boolean ob der Kick gerechtfertigt ist
     * @author Darian, Marvin
     * @since sprint4
     */
    public boolean kickUser(UUID id, User userToKick, User owner) {
        Optional<Lobby> lobby = this.getLobby(id);
        if (lobby.isPresent() && lobby.get().getOwner().getUsername().equals(owner.getUsername())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("User " + userToKick.getUsername() + " is kicked out of the lobby " + getLobby(id));
            }
            lobby.get().leaveUser(userToKick);
            if (lobby.get().getPlayers() == 0) {
                this.dropLobby(id);
            }
            return true;
        }
        return false;
        // TODO: error handling not existing lobby
    }

    /**
     * Getter um alle Lobbys zurückzugeben.
     *
     * @return Collection mit allen Lobbies
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
        return lobbies.get(lobbyID) != null ? Optional.of(lobbies.get(lobbyID).getName()) : Optional.empty();
    }

    /**
     * Getter für den Lobby Besitzer.
     *
     * @param lobbyID Die ID der Lobby, von der der Besitzer zurückgegeben werden soll.
     * @author Timo, Rike, Marvin
     * @since Sprint 3
     */
    public User getLobbyOwner(UUID lobbyID) {
        return lobbies.get(lobbyID).getOwner();
    }

    /**
     * Setzt die Anzahl der maximalen Spieler der Lobby und gibt, wenn es erfolgreich war, true zurück.
     *
     * @param lobbyID        Die LobbyID, der Lobby wo die Max-Player geändert werden soll.
     * @param loggedInUser   Der User, welcher die setMaxPlayerRequest an den Server geschickt hat.
     * @param maxPlayerValue Der Wert, welcher verändert wird.
     * @author Timo, Rike
     * @since Sprint 3
     */
    public boolean setMaxPlayer(UUID lobbyID, User loggedInUser, Integer maxPlayerValue) {

        if (lobbies.get(lobbyID).getOwner().equals(loggedInUser)) {
            lobbies.get(lobbyID).setMaxPlayer(maxPlayerValue);
            return true;
        } else {
            return false;
        }
    }
}