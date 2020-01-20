package de.uol.swp.server.game;

import de.uol.swp.common.game.Game;
import de.uol.swp.common.lobby.Lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Das Gamemanagement verwaltet die laufenden Spiele
 *
 * @author kenoO
 * @since Sprint 4
 */
public class GameManagement implements de.uol.swp.common.game.GameManagement {

    private Map<UUID, Game> games;

    /**
     * Erstellt ein neues GameManagement
     */
    public GameManagement() {
        this.games = new HashMap<>();
    }


    @Override
    public void createGame(Lobby lobby) {

    }

    @Override
    public void deleteGame(UUID id) {
        games.remove(id);
    }

    @Override
    public Game getGame(UUID id) {
        return games.get(id);
    }
}
