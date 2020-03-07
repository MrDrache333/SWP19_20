package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.messages.DrawHandMessage;
import de.uol.swp.common.game.messages.StartActionPhaseMessage;
import de.uol.swp.common.game.messages.StartBuyPhaseMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.card.ActionCard;
import de.uol.swp.server.game.card.Card;
import de.uol.swp.server.game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Playground stellt das eigentliche Spielfeld dar
 */
class Playground {

    /**
     * Die Spieler
     */
    private List<Player> players = new ArrayList<>();
    private Player actualPlayer;
    private Player nextPlayer;
    private ArrayList<Short> theIdsFromTheHand = new ArrayList<>(5);
    private GameService gameService;
    private UUID gameID;

    /**
     * Erstellt ein neues Spielfeld und übergibt die Spieler. Die Reihenfolge der Spieler wird zufällig zusammengestellt.
     * Es wird außerdem ein GameService gesetzt um dem aktuellen Spieler seine Karten zu schicken.
     *
     * @param lobby Die zu nutzende Lobby
     * @author KenoO, Julia, Ferit
     * @since Sprint 5
     */
    @Inject
    Playground(Lobby lobby, GameService gameService) {
        for (User user : lobby.getUsers()) {
            Player player = new Player(user.getUsername());
            player.setTheUserInThePlayer(user);
            players.add(player);
        }
        Collections.shuffle(players);
        this.gameService = gameService;
        this.gameID = lobby.getLobbyID();
    }

    /**
     * Initialisiert actual- und nextPlayer und aktualisiert diese, wenn ein Spieler alle Phasen durchlaufen hat.
     * Dem neuen aktuellen Spieler wird eine StartActionPhaseMessage gesendet,
     * wenn er eine Aktionskarte auf der Hand hat bzw. eine StartBuyPhaseMessage wenn nicht.
     *
     * @author Julia
     * @since Sprint5
     */
    public void newTurn() {
        if (actualPlayer == null && nextPlayer == null) {
            actualPlayer = players.get(0);
            nextPlayer = players.get(1);
        } else {
            //if: User befindet sich nicht in Clearphase: return
            int index = players.indexOf(nextPlayer);
            actualPlayer = nextPlayer;
            nextPlayer = players.get(++index % players.size());
        }
        if (checkForActionCard()) {
            //aktuelle Phase = Aktionsphase
            gameService.sendToAllPlayers(gameID, new StartActionPhaseMessage(actualPlayer.getTheUserInThePlayer(), gameID));
        } else {
            //aktuelle Phase = Buyphase
            gameService.sendToAllPlayers(gameID, new StartBuyPhaseMessage(actualPlayer.getTheUserInThePlayer(), gameID));
        }
    }

    /**
     * Methode, welche vom aktuellen Player die Hand versendet. Holz sich von der aktuellen Hand des Spielers die Karten und speichert die IDs dieser in einer ArrayList.
     *
     * @author Ferit
     * @version 1
     * @since Sprint5
     */
    public void sendPlayersHand() {
        for (Card card : actualPlayer.getPlayerDeck().getHand()) {
            theIdsFromTheHand.add(card.getId());
        }
        DrawHandMessage theHandMessage = new DrawHandMessage(theIdsFromTheHand, gameID);
        gameService.sendToSpecificPlayer(actualPlayer, theHandMessage);
    }

    /**
     * Überprüft, ob der aktuelle Spieler eine Aktionskarte auf der Hand hat, die er spielen könnte.
     *
     * @return true, wenn er eine Aktionskarte auf der Hand hat, sonst false
     * @author Julia
     * @since Sprint5
     */
    public boolean checkForActionCard() {
        for (Card card : actualPlayer.getPlayerDeck().getHand()) {
            if (card instanceof ActionCard) {
                return true;
            }
        }
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

}
