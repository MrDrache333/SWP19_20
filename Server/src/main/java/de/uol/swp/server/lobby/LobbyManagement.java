package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.exception.KickPlayerException;
import de.uol.swp.common.lobby.exception.LeaveLobbyException;
import de.uol.swp.common.lobby.exception.SetMaxPlayerException;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.KeyException;
import java.util.*;
import java.util.stream.Collectors;

public class LobbyManagement {
    static final Logger LOG = LogManager.getLogger(LobbyManagement.class);
    /**
     * Map von UUID zu Lobby
     *
     * @author Marvin
     * @since Sprint 5
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
     * @since Sprint 5
     */
    public void dropLobby(UUID id) {
        if (!lobbies.containsKey(id)) {
            throw new IllegalArgumentException("LobbyID nicht gefunden! ID: " + id);
        }
        lobbies.remove(id);
        LOG.info("Lobby " + getLobby(id) + "entfernt");
    }

    /**
     * Getter für Lobby als Optional.
     *
     * @param id der Lobbyname
     * @return Optional mit Lobby wenn vorhanden, sonst leere Optional
     * @author Marvin
     * @since Sprint 5
     */
    public Optional<Lobby> getLobby(UUID id) {
        Lobby lobby = lobbies.get(id);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    /**
     * Es wird herausgefunden ob der User ingame ist
     *
     * @param user der User
     * @return Boolean ob der User ingame ist
     * @author Darian
     * @since Sprint 5
     */
    public boolean isUserIngame(User user) {
        for (Map.Entry<UUID, Lobby> lobby : lobbies.entrySet()) {
            if (lobby.getValue().getUsers().contains(user)) {
                if (lobby.getValue().getInGame()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gibt alle aktiven Spiele eines Spielers als UUID Liste zurück
     *
     * @param user Der Benutzer
     * @return Liste aller aktive Spiele des Benutzers
     * @author Marvin
     * @since Sprint8
     */

    public List<UUID> activeGamesOfUser(User user) {
        return lobbies.entrySet().stream()
                .filter(e -> e.getValue().getInGame())
                .filter(e -> e.getValue().getUsers().contains(user))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Handling das ein User die Lobby verlässt. Überprüft, ob die gegebene Lobby existiert.
     *
     * @param id   Die LobbyID
     * @param user Der User, welches die Lobby verlässt.
     * @throws LeaveLobbyException Wenn die Lobby nicht existiert.
     * @author Marvin
     */
    public void leaveLobby(UUID id, User user) {
        Optional<Lobby> lobby = this.getLobby(id);
        if (lobby.isPresent()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("User " + user.getUsername() + " verlässt die Lobby " + getLobby(id).get());
            }
            lobby.get().leaveUser(user);
            if (lobby.get().getPlayers() == 0) {
                this.dropLobby(id);
            }
        } else {
            throw new LeaveLobbyException("Die zu verlassende Lobby existiert nicht.");
        }
    }

    /**
     * Aktualisiert jede Lobby, in der der aktualisierte User drinne ist.
     *
     * @param updatedUser Der aktualisierte User
     * @param oldUser     Der alte User
     * @author Julia, Marvin
     * @since Sprint 4
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
                Lobby lobbyToUpdate = new LobbyDTO(lobby.getName(), updatedOwner, lobby.getLobbyID(), lobby.getLobbyPassword(), newUsers, lobby.getPlayers(), lobby.getMaxPlayer(), lobby.getInGame());
                updatedLobbies.put(lobbyToUpdate.getLobbyID(), lobbyToUpdate);
            }
        }
        updatedLobbies.entrySet().forEach(l -> lobbies.replace(l.getKey(), l.getValue()));
    }

    /**
     * Benutzer wird aus der Lobby-Liste entfernt wenn der Spielbesitzer ihn gekickt hat.
     *
     * @param id         Die LobbyID
     * @param userToKick Der zu entfernende Benutzer
     * @param owner      Der Lobbybesitzer
     * @throws KickPlayerException Wenn der Spieler nicht aus der Lobby gekickt werden kann
     * @author Darian, Marvin
     * @since Sprint 4
     */
    public void kickUser(UUID id, User userToKick, User owner) {
        Optional<Lobby> lobby = this.getLobby(id);
        if (lobby.isPresent()) {

            if (lobby.get().getOwner().getUsername().equals(owner.getUsername())) {
                lobby.get().leaveUser(userToKick);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("User " + userToKick.getUsername() + " ist von der Lobby gekickt worden " + getLobby(id));
                }
                if (lobby.get().getPlayers() == 0) {
                    this.dropLobby(id);
                }
            } else {
                throw new KickPlayerException("Benutzer kann nicht aus der Lobby gekickt werden, da " + owner + " nicht der Lobbybesitzer ist.");
            }
        } else {
            throw new KickPlayerException("Die Lobby existiert nicht, in der der Benutzer gekickt werden soll.");
        }
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
     * @param lobbyID Die übergebene LobbyID
     * @author Marvin
     * @since Sprint 3
     */
    public Optional<String> getName(UUID lobbyID) {
        return lobbies.get(lobbyID) != null ? Optional.of(lobbies.get(lobbyID).getName()) : Optional.empty();
    }

    /**
     * Getter für den Lobby Besitzer.
     *
     * @param lobbyID Die ID der Lobby, von der der Besitzer zurückgegeben werden soll.
     * @author Timo, Rike, Marvin, Ferit, Fenja
     * @since Sprint 3
     */
    public Optional<User> getLobbyOwner(UUID lobbyID) {
        if (lobbies.containsKey(lobbyID)) {
            return Optional.ofNullable(lobbies.get(lobbyID).getOwner());
        }
        return Optional.empty();
    }

    /**
     * Setzt die Anzahl der maximalen Spieler der Lobby und gibt, wenn es erfolgreich war, true zurück.
     *
     * @param lobbyID        Die LobbyID, der Lobby wo die Max-Player geändert werden soll.
     * @param loggedInUser   Der User, welcher die setMaxPlayerRequest an den Server geschickt hat.
     * @param maxPlayerValue Der Wert, welcher verändert wird.
     * @throws SetMaxPlayerException wenn die maximale Spierleranzahl nicht geändert werden kann
     * @author Timo, Rike, Darian
     * @since Sprint 3
     */
    public void setMaxPlayer(UUID lobbyID, User loggedInUser, Integer maxPlayerValue) {
        if (lobbies.get(lobbyID).getOwner().equals(loggedInUser)) {
            if (maxPlayerValue >= getLobby(lobbyID).get().getPlayers()) {
                lobbies.get(lobbyID).setMaxPlayer(maxPlayerValue);
            } else {
                throw new SetMaxPlayerException("Es sind zu viele Benutzer in der Lobby, um die maximale Spierleranzahl ändern.");
            }
        } else {
            throw new SetMaxPlayerException(loggedInUser + " ist nicht der Lobbybesitzer und kann nicht die maximale Spierleranzahl ändern.");
        }
    }
}