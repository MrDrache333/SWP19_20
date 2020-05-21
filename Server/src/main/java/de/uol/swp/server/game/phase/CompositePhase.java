package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.card.parser.components.CardStack;
import de.uol.swp.common.game.exception.NotEnoughMoneyException;
import de.uol.swp.common.game.messages.DrawHandMessage;
import de.uol.swp.common.game.messages.GameOverMessage;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Funktionsklasse aller Phasen
 */
public class CompositePhase implements ActionPhase, BuyPhase, ClearPhase {

    private final Playground playground;
    private static final Logger LOG = LogManager.getLogger(GameService.class);

    /**
     * Der Konstruktor
     *
     * @param playground das Spielfeld
     * @author Fenja
     * @since Sprint6
     */
    public CompositePhase(Playground playground) {
        this.playground = playground;
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
        if (!player.getPlayerDeck().getHand().contains(currentCard)) {
            throw new IllegalArgumentException("Die Hand enthält die gesuchte Karte nicht");
        }
        /*
        3. Führe die auf der Karte befindlichen Aktionen aus
        3.1 Die Karte wird auf den ActionPile gelegt und aus der Hand entfernt.
         */
        ActionCardExecution executeAction = new ActionCardExecution(cardId, playground);
        executeAction.execute();
        player.getPlayerDeck().getActionPile().add(currentCard);
        player.getPlayerDeck().getHand().remove(currentCard);
    }

    public void finishedActionCardExecution(Player player, ArrayList<Short> newHandCards) {
        if (!newHandCards.isEmpty()) {
            playground.getGameService().sendToSpecificPlayer(player, new DrawHandMessage(newHandCards, playground.getID(), (short) playground.getPlayers().size()));
        }
        playground.sendCardsDeckSize();
        player.setAvailableActions(player.getAvailableActions() - 1);
        if (player.getAvailableActions() == 0) {
            playground.nextPhase();
        }
    }


    /**
     * Methode um eine Karte zu kaufen. Diese wird dem Ablagestapel des Spielers hinzugefügt, wenn er genug Geld hat
     *
     * @param player Der Spieler
     * @param cardId Die Karten-ID
     * @author Paula
     * @since Sprint6
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
            if (moneyValuePlayer + additionalMoney < currentCard.getCosts()) {
                LOG.error("Nicht genug Geld");
                throw new NotEnoughMoneyException("Nicht genug Geld vorhanden");
            }
            if (moneyValuePlayer < currentCard.getCosts()) {
                int diff = currentCard.getCosts() - moneyValuePlayer;
                player.setAdditionalMoney(additionalMoney - diff);
            }
            player.getPlayerDeck().getDiscardPile().add(currentCard);
            player.getPlayerDeck().discardMoneyCardsForValue(currentCard.getCosts());
            playground.getCardField().put(cardId, --count);
        }
        player.setAvailableBuys(player.getAvailableBuys() - 1);
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
     * @since Sprint6
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
     * @param cardStack
     * @param cardId
     * @return card Karte, zu der die ID gehört
     * @author Paula
     * @since Sprint6
     */

    private Card getCardFromId(CardStack cardStack, short cardId) {
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
     * @author Fenja
     * @since Sprint6
     */
    public boolean checkIfGameIsFinished() {
        if (playground.getCardField().get((short) 6) == 0) {
            return true;
        }
        int counter = 0;
        for (Card card : playground.getCardsPackField().getCards().getActionCards()) {
            if (playground.getCardField().containsKey(card.getId()) && playground.getCardField().get(card.getId()) == 0) {
                counter++;
                if (counter >= 3) {
                    return true;
                }
            }
        }
        return false;
    }
}
