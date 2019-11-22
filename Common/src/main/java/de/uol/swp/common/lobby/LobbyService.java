package de.uol.swp.common.lobby;

import java.util.List;

public interface LobbyService {

    /**
     * Retrieve a list of all currently existing lobbies
     * @return a list of lobbies
     */
    List<Lobby> retrieveAllLobbies();

}
