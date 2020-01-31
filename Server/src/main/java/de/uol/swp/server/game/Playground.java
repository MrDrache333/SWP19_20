package de.uol.swp.server.game;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Playground stellt das eigentliche Spielfeld dar
 */
class Playground {

    /**
     * Die Spieler
     */
    private List<Player> players = new ArrayList<>();

    /**
     * Erstellt ein neues Spielfeld
     *
     * @param lobby Die zu nutzende Lobby
     * @author KenoO, Julia
     * @since Sprint 5
     */
    Playground(Lobby lobby) {
        for (User user : lobby.getUsers()) {
            Player player = new Player(user.getUsername());
            players.add(player);
        }
    }
}
