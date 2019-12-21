package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class LobbyManagement {
    static final Logger LOG = LogManager.getLogger(LobbyManagement.class);

    private final Map<String, Lobby> lobbies = new HashMap<>();

    /**
     * Um UUID Map erweitert
     *
     * @author Marvin
     * @since Sprint3
     */
    private Map<UUID, String> lobbyNames = new HashMap<>();

    /**
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

    public void dropLobby(String name) {
        if (!lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " not found!");
        }
        lobbies.remove(name);
        LOG.info("Lobby " + name + "removed");
    }

    public Optional<Lobby> getLobby(String name) {
        Lobby lobby = lobbies.get(name);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

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
        // TODO: error handling not existing lobby
    }

    public Collection<Lobby> getLobbies() {
        return lobbies.values();
    }

    /**
     * Getter für Name
     *
     * @param lobbyID Die Lobby ID
     * @author Marvin
     * @since Sprint3
     */
    public Optional<String> getName(UUID lobbyID) {
        return lobbyNames.get(lobbyID) != null ? Optional.of(lobbyNames.get(lobbyID)) : Optional.empty();
    }
}