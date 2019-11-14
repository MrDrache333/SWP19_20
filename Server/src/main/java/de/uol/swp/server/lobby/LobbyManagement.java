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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LobbyManagement {


    private Map<Integer, Lobby> lobbies = new HashMap<>();

    public void createLobby(String name, User owner) {
        if (lobbies.containsKey(lobbies.size())) {
            throw new IllegalArgumentException("Lobby name " + name + " already exists!");
        }


        lobbies.put(lobbies.size(), new LobbyDTO(name, owner,lobbies.size()));





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



}
