package de.uol.swp.common.lobby;

import java.util.List;
import java.util.UUID;

public interface LobbyService {

    /**
     * Retrieve a list of all currently existing lobbies
     * @return a list of lobbies
     */
    List<Lobby> retrieveAllLobbies();

    List<Lobby> updateAllLobbies(String name);

}
