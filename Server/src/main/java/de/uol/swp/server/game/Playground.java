package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.messages.DrawHandMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
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
    private Player latestGivedUpPlayer;
    private ArrayList<Short> theIdsFromTheHand = new ArrayList<>(5);
    private GameService gameService;
    private UUID theSpecificLobbyID;
    int thePositionInList;

    /**
     * Erstellt ein neues Spielfeld und übergibt die Spieler. Die Reihenfolge der Spieler wird zufällig zusammengestellt.
     * Es wird außerdem der erste Player gesetzt und der nächste Player und ein GameService gesetzt um dem aktuellen Spieler seine Karten zu schicken.
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
        this.actualPlayer = players.get(0);
        this.nextPlayer = players.get(1);
        this.gameService = gameService;
        this.theSpecificLobbyID = lobby.getLobbyID();
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
        DrawHandMessage theHandMessage = new DrawHandMessage(theIdsFromTheHand, theSpecificLobbyID);
        gameService.sendToSpecificPlayer(actualPlayer, theHandMessage);
    }

    /**
     * Die Methode kümmert sich um das Aufgeben des Spielers in dem spezifizierten Game/Playground.
     *
     * @param lobbyID
     * @param theGivingUpUser
     * @param wantsToGiveUp
     * @return Ob der Spieler erfolgreich entfernt worden ist oder nicht.
     */
    public Boolean playerGivedUp(UUID lobbyID, UserDTO theGivingUpUser, Boolean wantsToGiveUp) {
        Boolean success;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerName().equals(theGivingUpUser.getUsername())) {
                thePositionInList = i;
                break;
            } else {
                i++;
            }

        }
        if (this.players.get(thePositionInList).getPlayerName().equals(theGivingUpUser.getUsername()) && wantsToGiveUp && lobbyID.equals(this.theSpecificLobbyID)) {
            latestGivedUpPlayer = this.players.get(thePositionInList);
            this.players.remove(thePositionInList);
            success = true;
            return true;
        } // TODO: Wenn Spielelogik weiter implementiert wird und ein Spieler aufgibt, Handling implementieren wie mit aufgegeben Spielern weiter umgegangen wird.
        else {
            success = false;
            return success;
        }
    }

    /**
     * Gibt den Spieler zurück der als letztes Aufgegeben hat.
     *
     * @return s.o
     * @author Haschem, Ferit
     * @since Sprint5
     */
    public Player getLatestGivedUpPlayer() {
        return latestGivedUpPlayer;
    }
}
