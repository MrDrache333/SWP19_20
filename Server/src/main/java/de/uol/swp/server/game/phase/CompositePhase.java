package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.card.parser.components.CardStack;
import de.uol.swp.common.game.messages.GameOverMessage;
import de.uol.swp.common.game.messages.InfoPlayDisplayMessage;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Die Funktionsklasse aller Phasen
 */
public class CompositePhase implements ActionPhase, BuyPhase, ClearPhase {

    private final Playground playground;
    private final List<Short> implementedActionCards;
    private ActionCardExecution executeAction;

    /**
     * Der Konstruktor
     *
     * @param playground das Spielfeld
     * @author Fenja
     * @since Sprint 6
     */
    public CompositePhase(Playground playground) {
        this.playground = playground;
        Short[] actioncards = {(short) 22, (short) 8, (short) 9, (short) 21, (short) 14, (short) 23, (short) 11, (short) 27, (short) 10, (short) 16, (short) 19, (short) 15, (short) 13};
        implementedActionCards = Arrays.asList(actioncards);
    }

    @Override
    public void executeActionPhase(Player player, short cardId) {
        CardPack cardsPackField = playground.getCardsPackField();
        Card currentCard = getCardFromId(cardsPackField.getCards(), cardId);
        // 1. Verifiziere, dass Karte existiert

        if (currentCard == null) {
            throw new IllegalArgumentException("CardID wurde nicht gefunden");
        }
        // 2. Überprüfe, ob Spieler diese Karte in der Hand hat
        if (player.getPlayerDeck().getHand().stream().noneMatch(c -> c.getId() == currentCard.getId())) {
            throw new IllegalArgumentException("Die Hand enthält die gesuchte Karte nicht");
        }
        if (!implementedActionCards.contains(currentCard.getId())) {
            throw new IllegalArgumentException("Diese Aktionskarte ist leider noch nicht implementiert.");
        }
        /*
        3. Führe die auf der Karte befindlichen Aktionen aus
        3.1 Die Karte wird auf den ActionPile gelegt und aus der Hand entfernt.
         */
        executeAction = new ActionCardExecution(cardId, playground);
        playground.getGameService().getBus().register(executeAction);
        player.getPlayerDeck().getHand().remove(currentCard);
        executeAction.execute();
    }

    /**
     * Wenn eine Aktionskarte vollständig ausgeführt wurde, wird sie in die Aktionszone des Spielers oder den Müll gelegt.
     * Die letzte Karte auf dem Ablagestapel und Müll, Anzahl der Karten auf dem Nachziehstapel
     * werden gesendet.
     * Der Aktionencounter wird um 1 verringert, dem Spieler wird eine InfoPlayDisplayMessage gesendet und die Phase
     * wird gewechselt, falls der Spieler keine Aktionen oder Aktionskarten mehr hat.
     *
     * @param player Der Spieler
     * @param card   Gespielte Karte
     * @author Julia, KenoO
     * @since Sprint 7
     */
    public void finishedActionCardExecution(Player player, Card card) {
        if (!executeAction.isRemoveCardAfter()) {
            player.getPlayerDeck().getActionPile().add(card);
        } else {
            playground.getTrash().add(card);
        }
        playground.sendCardsDeckSize();
        if (player.getAvailableActions() > 0) {
            player.setAvailableActions(player.getAvailableActions() - 1);
        }
        //Sende alle neuen Informationen bezüglich seiner möglichen Aktioen des Spielers an den Spieler zurück
        playground.getGameService().sendToSpecificPlayer(player,
                new InfoPlayDisplayMessage(
                        playground.getID(), player.getTheUserInThePlayer(),
                        player.getAvailableActions(),
                        player.getAvailableBuys(),
                        player.getAdditionalMoney(),
                        player.getPlayerDeck().actualMoneyFromPlayer(),
                        Phase.Type.ActionPhase));
        if ((player.getAvailableActions() == 0 || !playground.checkForActionCard()) && playground.getActualPhase() == Type.ActionPhase) {
            playground.nextPhase();
        }
    }


    /**
     * Methode um eine Karte zu kaufen. Diese wird dem Ablagestapel des Spielers hinzugefügt, wenn er genug Geld hat
     *
     * @param player Der Spieler
     * @param cardId Die Karten-ID
     * @author Paula
     * @since Sprint 6
     */
    @Override
    public int executeBuyPhase(Player player, short cardId) {
        CardPack cardsPackField = playground.getCardsPackField();
        Card currentCard = getCardFromId(cardsPackField.getCards(), cardId);

        // Karten und deren Anzahl werden aus dem Spielfeld geladen.
        int count = playground.getCardField().get(cardId);
        if (count > 0) {
            // Falls die ID der Karte nicht vorhanden ist, wird eine Exception geworfen
            if (currentCard == null) {
                throw new IllegalArgumentException("CardID wurde nicht gefunden");
            }
            /*Falls die ID vorhanden ist wird der Geldwert des Spielers berechnet, hat er
              genug Geld, wird die Karte seinem Ablagestapel hinzugefügt, das Geld wird ihm entzogen
              und die Anzahl der Karte auf dem Spielfeld verringert sich um eins
            */
            int moneyValuePlayer = player.getPlayerDeck().actualMoneyFromPlayer();
            int additionalMoney = player.getAdditionalMoney();
            if (moneyValuePlayer < currentCard.getCosts()) {
                int diff = currentCard.getCosts() - moneyValuePlayer;
                player.setAdditionalMoney(additionalMoney - diff);
            }
            player.getPlayerDeck().getDiscardPile().add(currentCard);
            player.getPlayerDeck().discardMoneyCardsForValue(currentCard.getCosts());
            playground.getCardField().put(cardId, --count);
        }
        player.setAvailableBuys(player.getAvailableBuys() - 1);
        int availableAction = player.getAvailableActions();
        int availableBuy = player.getAvailableBuys();
        int additionalMoney = player.getAdditionalMoney();
        int moneyOnHand = player.getPlayerDeck().actualMoneyFromPlayer();
        playground.getGameService().sendToSpecificPlayer(player, new InfoPlayDisplayMessage(playground.getID(), player.getTheUserInThePlayer(), availableAction, availableBuy, additionalMoney, moneyOnHand, playground.getActualPhase()));

        if (player.getAvailableBuys() == 0) {
            playground.nextPhase();
        }
        return count;
    }

    /**
     * Alle Karten auf der Hand des Spielers werden auf den Ablagestapel verschoben
     * und eine neue Hand gezogen und die letzte Karte, die auf den Ablagestapel gelegt wird, wird übergeben
     *
     * @param player Der aktuelle Spieler
     * @author Julia, Fenja
     * @since Sprint 6
     */
    @Override
    public void executeClearPhase(Player player) {
        Deck deck = player.getPlayerDeck();
        deck.getDiscardPile().addAll(deck.getHand());
        deck.getDiscardPile().addAll(deck.getActionPile());
        playground.sendLastCardOfDiscardPile(playground.getID(), deck.getDiscardPile().get(deck.getDiscardPile().size() - 1).getId(), player.getTheUserInThePlayer());
        deck.getHand().clear();
        deck.getActionPile().clear();
        deck.drawHand();
        player.setAdditionalMoney(0);
        player.setAvailableBuys(1);
        player.setAvailableActions(1);
        int availableAction = player.getAvailableActions();
        int availableBuy = player.getAvailableBuys();
        int additionalMoney = player.getAdditionalMoney();
        int moneyOnHand = player.getPlayerDeck().actualMoneyFromPlayer();
        playground.getGameService().sendToSpecificPlayer(player, new InfoPlayDisplayMessage(playground.getID(), player.getTheUserInThePlayer(), availableAction, availableBuy, additionalMoney, moneyOnHand, playground.getActualPhase()));

        if (checkIfGameIsFinished()) {
            List<String> winners = playground.calculateWinners();
            playground.endGame(playground.getID(), new GameOverMessage(playground.getID(), winners, playground.getResultsGame()));
        } else {
            playground.newTurn();
        }
    }

    /**
     * Hilfsmethode um an die Daten über die ID zu kommen
     *
     * @param cardStack Der Kartenstapel
     * @param cardId Die KartenID
     * @return card Karte, zu der die ID gehört
     * @author Paula
     * @since Sprint 6
     */

    public Card getCardFromId(CardStack cardStack, short cardId) {
        for (Card card : cardStack.getActionCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        for (Card card : cardStack.getMoneyCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }

        for (Card card : cardStack.getValueCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        return null;
    }

    /**
     * Überprüft, ob das Spiel in der Clearphase beendet ist
     *
     * @return false, wenn das Spiel nicht vorbei ist
     * @author Fenja, Keno0
     * @since Sprint 7
     */
    public boolean checkIfGameIsFinished() {
        if (playground.getCardField().get((short) 6) == 0) {
            return true;
        }
        //Prüfen ob min. 3 andere Stapel leer sind
        return playground.getCardsPackField().getCards().getAllCards().stream().filter(c -> playground.getCardField().containsKey(c.getId()) && playground.getCardField().get(c.getId()) == 0).count() >= 3;
    }

    public ActionCardExecution getExecuteAction() {
        return executeAction;
    }

    public List<Short> getImplementedActionCards() {
        return implementedActionCards;
    }
}
