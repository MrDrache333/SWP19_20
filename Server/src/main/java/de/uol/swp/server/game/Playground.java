package de.uol.swp.server.game;

import de.uol.swp.common.game.messages.DrawHandMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.card.Card;
import de.uol.swp.server.game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Playground stellt das eigentliche Spielfeld dar
 */
class Playground {

    /**
     * Die Spieler
     */
    private List<Player> players = new ArrayList<>();
    private static Player actualPlayer;
    private static Player nextPlayer;
    private static ArrayList<Short> theIdsFromTheHand;
    private static PlaygroundService playgroundService;

    /**
     * Erstellt ein neues Spielfeld und übergibt die Spieler. Die Reihenfolge der Spieler wird zufällig zusammengestellt.
     * Es wird außerdem der erste Player gesetzt und der nächste Player.
     *
     * @param lobby Die zu nutzende Lobby
     * @author KenoO, Julia, Ferit
     * @since Sprint 5
     */

    Playground(Lobby lobby) {
        for (User user : lobby.getUsers()) {
            Player player = new Player(user.getUsername());
            players.add(player);
        }
        Collections.shuffle(players);
        actualPlayer = players.get(0);
        nextPlayer = players.get(1);

    }

    public void sendPlayersHand() {
        ArrayList<Card> zwischenspeicherung = actualPlayer.getPlayerDeck().getHand();
        for (Card card : zwischenspeicherung) {
            theIdsFromTheHand.add(card.getId());
        }
        DrawHandMessage theHandMessage = new DrawHandMessage(theIdsFromTheHand);
        playgroundService.sendToSpecificPlayer(actualPlayer, theHandMessage);
    }
}
