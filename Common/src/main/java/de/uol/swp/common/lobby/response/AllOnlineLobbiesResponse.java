package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.message.AbstractResponseMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllOnlineLobbiesResponse extends AbstractResponseMessage {

    final private ArrayList<LobbyDTO> lobbies = new ArrayList<>();

    public AllOnlineLobbiesResponse() {
        // needed for serialization
    }

    public AllOnlineLobbiesResponse(Collection<Lobby> lobbies) {
        for (Lobby lobby : lobbies) {
            this.lobbies.add(new LobbyDTO(lobby.getName(), lobby.getOwner(), lobby.getLobbyID(), lobby.getUsers(), lobby.getPlayers()));
            this.lobbies.add(new LobbyDTO(lobby.getName(), lobby.getOwner(), lobby.getLobbyID(), lobby.getPlayers(), lobby.getMaxPlayer()));
        }
    }

    public List<LobbyDTO> getLobbies() {
        return lobbies;
    }
}
