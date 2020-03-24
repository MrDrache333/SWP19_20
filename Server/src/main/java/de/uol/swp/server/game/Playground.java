package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.CardPack;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.exception.GamePhaseException;
import de.uol.swp.common.game.messages.DrawHandMessage;
import de.uol.swp.common.game.messages.StartActionPhaseMessage;
import de.uol.swp.common.game.messages.StartBuyPhaseMessage;
import de.uol.swp.common.game.messages.StartClearPhaseMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.phase.CompositePhase;
import de.uol.swp.server.game.phase.Phase;
import de.uol.swp.server.game.player.Player;
import de.uol.swp.server.lobby.LobbyManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.*;

/**
 * Playground stellt das eigentliche Spielfeld dar
 */
class Playground {

    private static final Logger LOG = LogManager.getLogger(Playground.class);

    /**
     * Die Spieler
     */
    private List<Player> players = new ArrayList<>();
    private static Map<Short, Integer> cardField = new TreeMap<>();
    private Player actualPlayer;
    private Player nextPlayer;
    private Player latestGivedUpPlayer;
    private Phase.Type actualPhase;
    private ArrayList<Short> theIdsFromTheHand = new ArrayList<>(5);
    private GameService gameService;
    private UUID theSpecificLobbyID;
    private CompositePhase compositePhase;
    private Timer timer = new Timer();
    private short lobbySizeOnStart;

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
        this.theSpecificLobbyID = lobby.getLobbyID();
        this.compositePhase = new CompositePhase();
        this.lobbySizeOnStart = (short) lobby.getUsers().size();
        initializeCardField();
    }

    /**
     * Methode initalisiert das Kartenfeld mit der richtigen Anzahl an Karten auf dem Feld.
     */
    private void initializeCardField() {
        CardPack cardsPackField = new JsonCardParser().loadPack("Basispack");
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
            sendPlayersHand();
            int index = players.indexOf(nextPlayer);
            actualPlayer = nextPlayer;
            nextPlayer = players.get(++index % players.size());
        }
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
            latestGivedUpPlayer = this.players.get(thePositionInList);
            if (actualPlayer.equals(this.players.get(thePositionInList)) && players.size() >= 3) {
                actualPhase = Phase.Type.Clearphase;
                newTurn();
            }
            //   if (this.players.size() == 2) { // Auskommentiert, weil das später implementiert wird. 
            this.players.remove(thePositionInList);
            gameService.userGavesUpLeavesLobby(lobbyID, theGivingUpUser);
            // TODO: Handling, dass das Spiel dann beendet wird, wenn das implementiert ist und Gewinner Benachrichtigen.
            // }
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

    /**
     * Gibt den Spieler zurück der als letztes Aufgegeben hat.
     *
     * @return s.o
     * @author Haschem, Ferit
     * @since Sprint5
     */
    public Player getLatestGavedUpPlayer() {
        return latestGivedUpPlayer;
    }

    public void setActualPhase(Phase.Type actualPhase) {
        this.actualPhase = actualPhase;
    }

    /**
     * Es wird das Kartenfeld übergeben.
     *
     * @return Das Kartenfeld, also alle Karten die auf dem Playground initalisiert sind.
     */
    public static Map<Short, Integer> getCardField() {
        return cardField;
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
}
