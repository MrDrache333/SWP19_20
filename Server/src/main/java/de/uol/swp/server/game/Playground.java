package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.ValueCard;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.exception.GamePhaseException;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.phase.CompositePhase;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Playground stellt das eigentliche Spielfeld dar
 */
@SuppressWarnings("UnstableApiUsage")
public class Playground extends AbstractPlayground {

    private static final Logger LOG = LogManager.getLogger(Playground.class);
    private final Map<Short, Integer> cardField = new TreeMap<>();
    /**
     * Die Spieler
     */
    private final List<Player> players = new ArrayList<>();
    private final Map<String, Integer> resultsGame = new TreeMap<>();
    private final Map<Player, Integer> playerTurns = new HashMap<>();
    private Player actualPlayer;
    private Player nextPlayer;
    private Player latestGavedUpPlayer;
    private Phase.Type actualPhase;
    private final GameService gameService;
    private final UUID theSpecificLobbyID;
    private final CompositePhase compositePhase;
    private final Timer timer = new Timer();
    private final short lobbySizeOnStart;
    private final CardPack cardsPackField;
    private final ArrayList<Short> chosenCards;
    private final ArrayList<Card> trash = new ArrayList<>();
    private final UserDTO infoUser = new UserDTO("infoUser", "", "");

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
            if (!user.getIsBot()) {
                Player player = new Player(user.getUsername());
                player.setTheUserInThePlayer(user);
                player.setBot(false);
                players.add(player);
                playerTurns.put(player, 0);
            } else {
                Player player = new Player(user.getUsername());
                player.setTheUserInThePlayer(user);
                player.setBot(true);
                players.add(player);
                playerTurns.put(player, 0);
            }
        }
        Collections.shuffle(players);
        this.gameService = gameService;
        this.theSpecificLobbyID = lobby.getLobbyID();
        this.compositePhase = new CompositePhase(this);
        this.lobbySizeOnStart = (short) lobby.getUsers().size();
        this.cardsPackField = new JsonCardParser().loadPack("Basispack");
        this.chosenCards = lobby.getChosenCards();
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
        // ChosenCard BasicCards
        if (chosenCards.isEmpty()) {
            chosenCards.add((short) 8);
            chosenCards.add((short) 9);
            chosenCards.add((short) 10);
            chosenCards.add((short) 11);
            chosenCards.add((short) 13);
            chosenCards.add((short) 14);
            chosenCards.add((short) 15);
            chosenCards.add((short) 16);
            chosenCards.add((short) 19);
            chosenCards.add((short) 21);
        }
        while (chosenCards.size() < 10) {
            short random = (short) (Math.random() * 31);
            if (!chosenCards.contains(random) && compositePhase.getImplementedActionCards().contains(random)) {
                chosenCards.add(random);
            }
        }
        for (Short chosenCard : chosenCards) {
            cardField.put(chosenCard, 10);
        }
        for (int i = 0; i < cardsPackField.getCards().getCurseCards().size(); i++) {
            Card card = cardsPackField.getCards().getCurseCards().get(i);
            cardField.put(card.getId(), 10);
        }
        for (int i = 0; i < cardsPackField.getCards().getMoneyCards().size(); i++) {
            Card card = cardsPackField.getCards().getMoneyCards().get(i);
            if (i == 0) cardField.put(card.getId(), 60);
            else if (i == 1) cardField.put(card.getId(), 40);
            else if (i == 2) cardField.put(card.getId(), 30);
        }
    }

    /**
     * Initialisiert actual- und nextPlayer und aktualisiert diese, wenn ein Spieler alle Phasen durchlaufen hat.
     * Dem neuen aktuellen Spieler wird seine Hand gesendet sowie eine StartActionPhaseMessage,
     * wenn er eine Aktionskarte auf der Hand hat bzw. eine StartBuyPhaseMessage wenn nicht.
     * Es wird zusätzlich der Timestamp (vom Server) mitgeschickt
     *
     * @author Julia, Ferit
     * @since Sprint 5
     */
    public void newTurn() {
        if (actualPlayer == null && nextPlayer == null) {
            gameService.sendCardField(theSpecificLobbyID, cardField);
            actualPlayer = players.get(0);
            nextPlayer = players.get(1);
            sendInitialCardsDeckSize();
            sendInitialHands();
        } else {
            //Spieler muss Clearphase durchlaufen haben
            if (actualPhase != Phase.Type.ClearPhase) return;
            if (actualPlayer != latestGavedUpPlayer) {
                sendPlayersHand();
                sendCardsDeckSize();
            }
            int index = players.indexOf(nextPlayer);
            actualPlayer = nextPlayer;
            nextPlayer = players.get(++index % players.size());
        }

        ChatMessage infoMessage = new ChatMessage(infoUser, getActualPlayer().getTheUserInThePlayer().getUsername() + " ist am Zug!");
        gameService.getBus().post(new NewChatMessageRequest(theSpecificLobbyID.toString(), infoMessage));
        int turns = playerTurns.get(actualPlayer);
        playerTurns.replace(actualPlayer, ++turns);
        actualPhase = Phase.Type.ActionPhase;
        if (checkForActionCard()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            gameService.sendToAllPlayers(theSpecificLobbyID, new StartActionPhaseMessage(actualPlayer.getTheUserInThePlayer(), theSpecificLobbyID, timestamp));
            //phaseTimer();
        } else {
            nextPhase();
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

    public void endGame(UUID lobbyID) {
        gameService.dropFinishedGame(lobbyID);
        gameService.getGameManagement().deleteLobbyWithOnlyBots(lobbyID);
    }

    /**
     * Startet innerhalb eines Spielzugs die nächste Phase.
     * Befindet sich der Spieler in der Clearphase, wird eine GamePhaseException geworfen.
     *
     * @author Julia
     * @since Sprint 5
     */
    public void nextPhase() {
        if (actualPhase == Phase.Type.ClearPhase) {
            throw new GamePhaseException("Du kannst die Clearphase nicht überspringen!");
        }
        if (actualPhase == Phase.Type.ActionPhase) {
            actualPhase = Phase.Type.BuyPhase;
            gameService.sendToAllPlayers(theSpecificLobbyID, new StartBuyPhaseMessage(actualPlayer.getTheUserInThePlayer(), theSpecificLobbyID));
            ChatMessage infoMessage = new ChatMessage(infoUser, getActualPlayer().getTheUserInThePlayer().getUsername() + " ist am Zug!");
            gameService.getBus().post(new NewChatMessageRequest(theSpecificLobbyID.toString(), infoMessage));
        } else {
            actualPhase = Phase.Type.ClearPhase;
            Player currentPlayer = actualPlayer;
            players.forEach(n -> {
                StartClearPhaseMessage msg = new StartClearPhaseMessage(currentPlayer.getTheUserInThePlayer(), theSpecificLobbyID, getIndexOfPlayer(n), getIndexOfPlayer(currentPlayer));
                gameService.sendToSpecificPlayer(n, msg);
            });
            compositePhase.executeClearPhase(actualPlayer);
        }
    }

    /**
     * Methode, welche vom aktuellen Player die Hand versendet. Holt sich von der aktuellen Hand des Spielers die Karten und speichert die IDs dieser in einer ArrayList.
     * sendet eine InfoPlayDisplayMessage zum aktualisieren der Anzeige von Aktion/Kauf/Geld
     *
     * @author Ferit, Rike
     * @since Sprint 5
     */
    public void sendPlayersHand() {
        ArrayList<Short> theIdsFromTheHand = actualPlayer.getPlayerDeck().getHand().stream().map(Card::getId).collect(Collectors.toCollection(() -> new ArrayList<>(5)));
        DrawHandMessage theHandMessage = new DrawHandMessage(theIdsFromTheHand, theSpecificLobbyID, (short) getPlayers().size(), false);
        gameService.sendToSpecificPlayer(actualPlayer, theHandMessage);
    }

    /**
     * Sendet dem aktuellen Spieler die Anzahl seiner Karten auf dem Nachziehstapel
     *
     * @author Julia
     * @since Sprint 7
     */
    public int sendCardsDeckSize() {
        int size = actualPlayer.getPlayerDeck().getCardsDeck().size();
        gameService.sendToAllPlayers(theSpecificLobbyID, new CardsDeckSizeMessage(theSpecificLobbyID, actualPlayer.getTheUserInThePlayer(), size, actualPlayer.getPlayerDeck().discardPileWasCleared()));
        return size;
    }

    /**
     * Sendet zu Spielbeginn jedem Spieler die Anzahl seiner Karten auf dem Nachziehstapel
     *
     * @author Julia
     * @since Sprint 7
     */
    public void sendInitialCardsDeckSize() {
        for (Player player : players) {
            int size = player.getPlayerDeck().getCardsDeck().size();
            gameService.sendToSpecificPlayer(player, new CardsDeckSizeMessage(theSpecificLobbyID, player.getTheUserInThePlayer(), size));
        }
    }

    /**
     * Die Methode kümmert sich um das Aufgeben des Spielers in dem spezifizierten Game/Playground.
     *
     * @param lobbyID         die Lobby-ID
     * @param theGivingUpUser der aufgebene User
     * @param wantsToGiveUp   ob der User aufgibt (Boolean)
     * @return Ob der Spieler erfolgreich entfernt worden ist oder nicht.
     * @author Haschem, Ferit
     */
    public Boolean playerGaveUp(UUID lobbyID, UserDTO theGivingUpUser, Boolean wantsToGiveUp) {
        int thePositionInList = IntStream.range(0, players.size()).filter(i -> players.get(i).getPlayerName().equals(theGivingUpUser.getUsername())).findFirst().orElse(-1);
        if (this.players.get(thePositionInList).getPlayerName().equals(theGivingUpUser.getUsername()) && wantsToGiveUp && lobbyID.equals(this.theSpecificLobbyID)) {
            latestGavedUpPlayer = this.players.get(thePositionInList);
            gameService.userGavesUpLeavesLobby(lobbyID, theGivingUpUser);

            if (this.players.size() == 2) {
                this.players.remove(thePositionInList);
                List<String> winners = calculateWinners();
                GameOverMessage gameOverByGaveUp = new GameOverMessage(lobbyID, winners, resultsGame);
                if (!this.players.get(0).isBot()) {
                    endGame(lobbyID, gameOverByGaveUp);
                } else {
                    endGame(lobbyID);
                }
            } else if (actualPlayer.equals(latestGavedUpPlayer) && !(onlyBotsLeft())) {
                this.players.remove(thePositionInList);
                actualPhase = Phase.Type.ClearPhase;
                newTurn();
            } else if (nextPlayer.equals(latestGavedUpPlayer) && !(onlyBotsLeft())) {
                if (thePositionInList < this.players.size() - 1) {
                    nextPlayer = this.players.get(++thePositionInList);
                    this.players.remove(--thePositionInList);
                } else {
                    this.players.remove(thePositionInList);
                    nextPlayer = this.players.get(0);
                }
            } else {
                this.players.remove(thePositionInList);
            }

            return true;
        } else {
            return false;
        }
    }

    public Boolean onlyBotsLeft() {
        for (Player player : players) {
            if (player.isBot()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sendet die letzte Karte vom Ablagestapel an den GameService
     *
     * @param gameID die Game-ID
     * @param cardID die Karten-ID
     * @param user   der User
     * @author Fenja
     * @since Sprint 6
     */
    public void sendLastCardOfDiscardPile(UUID gameID, short cardID, User user) {
        gameService.sendLastCardOfDiscardPile(gameID, cardID, user);
    }

    /**
     * Sendet die Initiale Hand an jeden Spieler spezifisch. Überprüfung via SessionID.
     * sendet eine InfoPlayDisplayMessage zum aktualisieren der Anzeige von Aktion/Kauf/Geld
     *
     * @author Ferit, Rike
     * @since Sprint 6
     */
    public void sendInitialHands() {
        for (Player playerhand : players) {
            ArrayList<Short> theIdsFromInitalPlayerDeck = playerhand.getPlayerDeck().getHand().stream().map(Card::getId).collect(Collectors.toCollection(() -> new ArrayList<>(5)));
            DrawHandMessage initialHandFromPlayer = new DrawHandMessage(theIdsFromInitalPlayerDeck, theSpecificLobbyID, (short) getPlayers().size(), true);
            gameService.sendToSpecificPlayer(playerhand, initialHandFromPlayer);
            int availableAction = playerhand.getAvailableActions();
            int availableBuy = playerhand.getAvailableBuys();
            int additionalMoney = playerhand.getAdditionalMoney();
            int moneyOnHand = playerhand.getPlayerDeck().actualMoneyFromPlayer();
            gameService.sendToSpecificPlayer(playerhand, new InfoPlayDisplayMessage(theSpecificLobbyID, playerhand.getTheUserInThePlayer(), availableAction, availableBuy, additionalMoney, moneyOnHand, actualPhase));
            // TODO: Bessere Logging Message irgendwann später implementieren..
            LOG.debug("All OK with sending initial Hands...");
        }
    }

    /**
     * Überprüft, ob der aktuelle Spieler eine Aktionskarte auf der Hand hat, die er spielen könnte.
     *
     * @return true, wenn er eine Aktionskarte auf der Hand hat, sonst false
     * @author Julia
     * @since Sprint 5
     */
    public boolean checkForActionCard() {
        return actualPlayer.getPlayerDeck().getHand().stream().anyMatch(card -> card instanceof ActionCard);
    }

    /**
     * Ermittelt den/die Gewinner des Spiels. Bei Gleichstand gewinnen der/die mit den wenigsten Zügen.
     *
     * @return Liste mit allen Gewinnern
     * @author Julia
     * @since Sprint 6
     */
    public List<String> calculateWinners() {
        List<String> winners = new ArrayList<>();
        for (Player player : players) {
            int victoryPoints;
            Deck deck = player.getPlayerDeck();
            deck.getCardsDeck().addAll(deck.getHand());
            deck.getCardsDeck().addAll(deck.getDiscardPile());
            deck.getHand().clear();
            deck.getDiscardPile().clear();

            victoryPoints = deck.getCardsDeck().stream().filter(card -> card instanceof ValueCard).mapToInt(card -> ((ValueCard) card).getValue()).sum();
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

    public Short getIndexOfPlayer(Player player) {
        return (short) players.indexOf(player);
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
     * @since Sprint 5
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
     * @since Sprint 5
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

    public Map<String, Integer> getResultsGame() {
        return resultsGame;
    }

    public UUID getID() {
        return theSpecificLobbyID;
    }

    /**
     * Es wird das Kartenfeld übergeben.
     *
     * @return Das Kartenfeld, also alle Karten die auf dem Playground initalisiert sind.
     */
    public Map<Short, Integer> getCardField() {
        return cardField;
    }

    public GameService getGameService() {
        return gameService;
    }

    public ArrayList<Card> getTrash() {
        return trash;
    }
}