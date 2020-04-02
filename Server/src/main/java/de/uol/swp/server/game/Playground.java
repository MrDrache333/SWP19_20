package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.ValueCard;
import de.uol.swp.common.game.card.parser.CardPack;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.exception.GamePhaseException;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.phase.CompositePhase;
import de.uol.swp.server.game.phase.Phase;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.*;

/**
 * Playground stellt das eigentliche Spielfeld dar
 */
public class Playground {

    private static final Logger LOG = LogManager.getLogger(Playground.class);
    private static Map<Short, Integer> cardField = new TreeMap<>();
    /**
     * Die Spieler
     */
    private List<Player> players = new ArrayList<>();
    private Map<String, Integer> resultsGame = new TreeMap<>();
    private Map<Player, Integer> playerTurns = new HashMap<>();
    private Player actualPlayer;
    private Player nextPlayer;
    private Player latestGavedUpPlayer;
    private Phase.Type actualPhase;
    private ArrayList<Short> theIdsFromTheHand = new ArrayList<>(5);
    private GameService gameService;
    private UUID theSpecificLobbyID;
    private CompositePhase compositePhase;
    private Timer timer = new Timer();
    private short lobbySizeOnStart;
    private CardPack cardsPackField;

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
            playerTurns.put(player, 0);
        }
        Collections.shuffle(players);
        this.gameService = gameService;
        this.theSpecificLobbyID = lobby.getLobbyID();
        this.compositePhase = new CompositePhase(this);
        this.lobbySizeOnStart = (short) lobby.getUsers().size();
        this.cardsPackField = new JsonCardParser().loadPack("Basispack");
        initializeCardField();
    }

    /**
     * Methode initalisiert das Kartenfeld mit der richtigen Anzahl an Karten auf dem Feld.
     */
    private void initializeCardField() {
        for (int i = 0; i < cardsPackField.getCards().getValueCards().size(); i++) {
            Card card = cardsPackField.getCards().getValueCards().get(i);
            if (lobbySizeOnStart < 3) {
                cardField.put(card.getId(), 8);
            } else cardField.put(card.getId(), 12);
        }
        for (int i = 0; i < cardsPackField.getCards().getActionCards().size(); i++) {
            Card card = cardsPackField.getCards().getActionCards().get(i);
            cardField.put(card.getId(), 10);
        }
        for (int i = 0; i < cardsPackField.getCards().getReactionCards().size(); i++) {
            Card card = cardsPackField.getCards().getReactionCards().get(i);
            cardField.put(card.getId(), 10);
        }
        for (int i = 0; i < cardsPackField.getCards().getMoneyCards().size(); i++) {
            Card card = cardsPackField.getCards().getMoneyCards().get(i);
            if (i == 0) cardField.put(card.getId(), 60);
            else if (i == 1) cardField.put(card.getId(), 40);
            else if (i == 2) cardField.put(card.getId(), 30);
            else LOG.debug("Komisch: @ initializeCardField- Else Methode in 104 ausgeschlagen.... @ @ @");
        }
    }

    /**
     * Initialisiert actual- und nextPlayer und aktualisiert diese, wenn ein Spieler alle Phasen durchlaufen hat.
     * Dem neuen aktuellen Spieler wird seine Hand gesendet sowie eine StartActionPhaseMessage,
     * wenn er eine Aktionskarte auf der Hand hat bzw. eine StartBuyPhaseMessage wenn nicht.
     * Es wird zusätzlich der Timestamp (vom Server) mitgeschickt
     *
     * @author Julia, Ferit
     * @since Sprint5
     */
    public void newTurn() {
        if (actualPlayer == null && nextPlayer == null) {
            actualPlayer = players.get(0);
            nextPlayer = players.get(1);
            sendInitialHands();
        } else {
            //Spieler muss Clearphase durchlaufen haben
            if (actualPhase != Phase.Type.Clearphase) return;
            if (actualPlayer != latestGavedUpPlayer) sendPlayersHand();
            int index = players.indexOf(nextPlayer);
            actualPlayer = nextPlayer;
            nextPlayer = players.get(++index % players.size());
        }

        int turns = playerTurns.get(actualPlayer);
        playerTurns.replace(actualPlayer, ++turns);
        actualPhase = Phase.Type.ActionPhase;
        if (checkForActionCard()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            gameService.sendToAllPlayers(theSpecificLobbyID, new StartActionPhaseMessage(actualPlayer.getTheUserInThePlayer(), theSpecificLobbyID, timestamp));
            phaseTimer();
        } else {
            skipCurrentPhase();
        }
    }

    /**
     * Beendet das Spiel, versendet die gameOverMessage und löscht das Spiel im GameManagement.
     *
     * @param lobbyID Die Lobby in der das Spiel beendet ist.
     * @param gameEnd Die GameOverMessage
     */
    public void endGame(UUID lobbyID, ServerMessage gameEnd) {
        gameService.sendToAllPlayers(lobbyID, gameEnd);
        gameService.dropFinishedGame(lobbyID);
    }

    /**
     * Ein Timer skippt nach 35 Sekunden die aktuelle Phase, sofern der Timer vorher nicht gecancelt worden ist. Hilfmethode endTimer ganz unten in der Klasse. Timer wird im GameService gecancelt, wenn eine Karte innerhalb der Zeit ausgewählt worden ist.
     */
    public void phaseTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                skipCurrentPhase();
                timer.cancel();
            }
        }, 35000);
    }

    /**
     * Überspringt die aktuelle Phase und startet die nächste, falls der Spieler sich gerade in der Aktions- oder Kaufphase befindet.
     * Befindet er sich in der Clearphase, wird eine GamePhaseException geworfen.
     *
     * @author Julia
     * @since Sprint5
     */
    public void skipCurrentPhase() {
        if (actualPhase == Phase.Type.Clearphase) {
            throw new GamePhaseException("Du kannst die Clearphase nicht überspringen!");
        }

        if (actualPhase == Phase.Type.ActionPhase) {
            actualPhase = Phase.Type.Buyphase;
            gameService.sendToAllPlayers(theSpecificLobbyID, new StartBuyPhaseMessage(actualPlayer.getTheUserInThePlayer(), theSpecificLobbyID));
            endTimer();
        } else {
            actualPhase = Phase.Type.Clearphase;
            gameService.sendToAllPlayers(theSpecificLobbyID, new StartClearPhaseMessage(actualPlayer.getTheUserInThePlayer(), theSpecificLobbyID));
            compositePhase.executeClearPhase(actualPlayer);
        }
    }

    /**
     * Methode, welche vom aktuellen Player die Hand versendet. Holt sich von der aktuellen Hand des Spielers die Karten und speichert die IDs dieser in einer ArrayList.
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
     * @author Haschem, Ferit
     */
    public Boolean playerGaveUp(UUID lobbyID, UserDTO theGivingUpUser, Boolean wantsToGiveUp) {
        int thePositionInList = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerName().equals(theGivingUpUser.getUsername())) {
                thePositionInList = i;
                break;
            }
        }
        if (this.players.get(thePositionInList).getPlayerName().equals(theGivingUpUser.getUsername()) && wantsToGiveUp && lobbyID.equals(this.theSpecificLobbyID)) {
            latestGavedUpPlayer = this.players.get(thePositionInList);
            gameService.userGavesUpLeavesLobby(lobbyID, theGivingUpUser);
            if (this.players.size() == 2) {
                this.players.remove(thePositionInList);
                List<String> winners = calculateWinners();
                GameOverMessage gameOverByGaveUp = new GameOverMessage(lobbyID, winners, resultsGame);
                endGame(lobbyID, gameOverByGaveUp);
            } else if (actualPlayer.equals(latestGavedUpPlayer)) {
                this.players.remove(thePositionInList);
                actualPhase = Phase.Type.Clearphase;
                newTurn();
            } else if (nextPlayer.equals(latestGavedUpPlayer)) {
                if (thePositionInList < this.players.size() - 1) {
                    nextPlayer = this.players.get(++thePositionInList);
                    this.players.remove(thePositionInList);
                } else {
                    this.players.remove(thePositionInList);
                    nextPlayer = this.players.get(0);
                }
            } else {
                this.players.remove(thePositionInList);
            }
            return true;
        } // TODO: Wenn Spielelogik weiter implementiert wird und ein Spieler aufgibt, Handling implementieren wie mit aufgegeben Spielern weiter umgegangen wird.
        else {
            return false;
        }
    }

    /**
     * Sendet die Initiale Hand an jeden Spieler spezifisch. Überprüfung via SessionID.
     *
     * @author Ferit
     * @since Sprint6
     */
    public void sendInitialHands() {
        for (Player playerhand : players) {
            ArrayList<Short> theIdsFromInitalPlayerDeck = new ArrayList<>(5);
            for (Card card : playerhand.getPlayerDeck().getHand()) {
                theIdsFromInitalPlayerDeck.add(card.getId());
            }
            DrawHandMessage initialHandFromPlayer = new DrawHandMessage(theIdsFromInitalPlayerDeck, theSpecificLobbyID);
            gameService.sendToSpecificPlayer(playerhand, initialHandFromPlayer);
            // TODO: Bessere Logging Message irgendwann später implementieren..
            LOG.debug("All OK with sending initial Hands...");
        }
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

    /**
     * Ermittelt den/die Gewinner des Spiels. Bei Gleichstand gewinnen der/die mit den wenigsten Zügen.
     *
     * @return Liste mit allen Gewinnern
     * @author Julia
     * @since Sprint6
     */
    public List<String> calculateWinners() {
        List<String> winners = new ArrayList<>();
        for (Player player : players) {
            int victoryPoints = 0;
            Deck deck = player.getPlayerDeck();
            deck.getCardsDeck().addAll(deck.getHand());
            deck.getCardsDeck().addAll(deck.getDiscardPile());
            deck.getHand().clear();
            deck.getDiscardPile().clear();

            for (Card card : deck.getCardsDeck()) {
                if (card instanceof ValueCard) {
                    victoryPoints += ((ValueCard) card).getValue();
                }
            }
            resultsGame.put(player.getPlayerName(), victoryPoints);
        }

        int max = Collections.max(resultsGame.values());
        for (Map.Entry<String, Integer> entry : resultsGame.entrySet()) {
            if (entry.getValue() == max) {
                winners.add(entry.getKey());
            }
        }

        if (winners.size() > 1) {
            Map<String, Integer> winnerTurns = new HashMap<>();
            for (Map.Entry<Player, Integer> entry : playerTurns.entrySet()) {
                if (winners.contains(entry.getKey().getPlayerName())) {
                    winnerTurns.put(entry.getKey().getPlayerName(), entry.getValue());
                }
            }

            winners.clear();
            int minTurns = Collections.min(winnerTurns.values());
            for (Map.Entry<String, Integer> entry : winnerTurns.entrySet()) {
                if (entry.getValue() == minTurns) {
                    winners.add(entry.getKey());
                }
            }
        }

        return winners;
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

    /**
     * Getter und Setter um an die aktuelle Phase zu kommen
     *
     * @return aktuelle Phase
     * @author Paula
     * @version 1
     * @since Sprint5
     */
    public Phase.Type getActualPhase() {
        return actualPhase;
    }

    public void setActualPhase(Phase.Type actualPhase) {
        this.actualPhase = actualPhase;
    }

    /**
     * Gibt den Spieler zurück der als letztes Aufgegeben hat.
     *
     * @return s.o
     * @author Haschem, Ferit
     * @since Sprint5
     */
    public Player getLatestGavedUpPlayer() {
        return latestGavedUpPlayer;
    }

    public CardPack getCardsPackField() {
        return cardsPackField;
    }

    /**
     * Beendet den Timer, sofern innerhalb der 35 Sekunden eine ActionKarte Ausgewählt worden ist.
     */
    public void endTimer() {
        timer.cancel();
    }

    public CompositePhase getCompositePhase() {
        return compositePhase;
    }

    public Map<Player, Integer> getPlayerTurns() {
        return playerTurns;
    }

    /**
     * Es wird das Kartenfeld übergeben.
     *
     * @return Das Kartenfeld, also alle Karten die auf dem Playground initalisiert sind.
     */
    public static Map<Short, Integer> getCardField() {
        return cardField;
    }
}
