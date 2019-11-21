package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.CreateLobbyRequest;
import de.uol.swp.common.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class LobbyManagement {
    static final Logger LOG = LogManager.getLogger(LobbyManagement.class);

    private Map<Integer, Lobby> lobbies = new HashMap<>();

    /**
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * lobbyID hat folgende Form: 067e6162-3b6f-4ae2-a171-2470b63dff00  (Beispiel) / UUID Object
     */

    public void createLobby(String name, User owner) {
        if (lobbies.containsKey(lobbies.size())) {
            throw new IllegalArgumentException("Lobby name " + name + " already exists!");
        }
        // Erstellen der UUID für die Lobbys.
        UUID lobbyID = UUID.randomUUID();
        LOG.info("Die Lobby " + name + " hat folgende UUID erstellt bekommen: " + lobbyID);
        lobbies.put(lobbies.size(), new LobbyDTO(name, owner, lobbyID));
    }

    public void dropLobby(String name) {
        if (!lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " not found!");
        }
        lobbies.remove(name);
    }

    public Optional<Lobby> getLobby(String name) {
        Lobby lobby = lobbies.get(name);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    public Collection<Lobby> getLobbies() {
        return lobbies.values();
    }
}
