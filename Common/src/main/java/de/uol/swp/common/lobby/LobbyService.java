package de.uol.swp.common.lobby;

import de.uol.swp.common.lobby.Lobby;

import java.util.List;

public interface LobbyService {

    /**
     * Retrieve a list of all currently existing lobbies
     * @return a list of lobbies
     */
    List<Lobby> retrieveAllLobbies();

}
