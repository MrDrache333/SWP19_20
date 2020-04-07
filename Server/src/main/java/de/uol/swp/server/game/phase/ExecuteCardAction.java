package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;
import de.uol.swp.common.game.messages.CardMovedMessage;
import de.uol.swp.common.game.messages.SelectCardsFromHandMessage;
import de.uol.swp.common.game.messages.ShowCardMessage;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class ExecuteCardAction {

    private short cardID;
    private Playground playground;
    private Player player;
    private Card theCard;
    private UUID gameID;

    public ExecuteCardAction(short cardID, Playground playground) {
        this.playground = playground;
        this.cardID = cardID;
        this.player = playground.getActualPlayer();
        getCard(cardID);
        this.gameID = playground.getID();
    }

    /**
     * Erhöht availableActions, availableBuys oder additionalMoney des Players um den angegebenen Wert.
     *
     * @param count    Wert, um den das jeweilige Attribut erhöht werden soll
     * @param activity Attribut, das erhöht werden soll
     * @return true
     * @author Julia
     * @since Sprint6
     */
    public boolean executeAddCapablePlayerActivity(short count, AbstractPlayground.PlayerActivityValue activity) {
        if (activity == AbstractPlayground.PlayerActivityValue.ACTION) {
            player.setAvailableActions(count + player.getAvailableActions());
        } else if (activity == AbstractPlayground.PlayerActivityValue.BUY) {
            player.setAvailableBuys(count + player.getAvailableBuys());
        } else {
            player.setAdditionalMoney(count + player.getAdditionalMoney());
        }
        return true;
    }

    /**
     * Sendet eine Message mit der ID der Karte, die angezeigt werden soll und der Zone,
     * in der sie angezeigt werden soll an den aktuellen Spieler.
     *
     * @param cardID die ID der Karte
     * @param zone   die Zone im Spielfeld, in der die Karte angezeigt werdem soll
     * @return true
     * @author Julia
     * @since Sprint6
     */
    public boolean executeShowCard(short cardID, AbstractPlayground.ZoneType zone) {
        playground.getGameService().sendToSpecificPlayer(playground.getActualPlayer(), new ShowCardMessage(cardID, zone, playground.getID()));
        return true;
    }

    /**
     * Zählt ein übergebenes KartenArray und übergibt die Größe.
     *
     * @param cards das Übergebene KartenArray
     * @return 0 = Übergebenes Array ist leer. | sonst die größe des KartenArrays
     */

    public int executeCount(ArrayList<Card> cards) {
        if (!cards.isEmpty()) {
            return cards.size();
        }
        return 0;
    }

    /**
     * Schickt dem Spieler eine Nachricht das er eine bestimmte Anzahl an Karten auswählen kann.
     *
     * @param theValue Die Anzahl der Karten
     * @param from     Woher
     * @param where    Wohin damit
     * @return true(? ? ?)
     */
    public boolean executeChooseCard(Value theValue, AbstractPlayground.ZoneType from, AbstractPlayground.ZoneType where) {
        playground.getGameService().sendToSpecificPlayer(playground.getActualPlayer(), new SelectCardsFromHandMessage(theValue, from, where));
        return true;
    }

    /**
     * Bewegt Karten von einem Ort zum anderen. ----------WIP----------
     *
     * @param cardsToMove
     * @param source      Woher
     * @param destination Wohin
     * @return true(? ? ?)
     */
    public boolean executeMoveAction(ArrayList<Card> cardsToMove, AbstractPlayground.ZoneType source, AbstractPlayground.ZoneType destination) {
        ArrayList<Short> theIds = new ArrayList<>();
        if (source.equals(AbstractPlayground.ZoneType.HAND) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                if (player.getPlayerDeck().getHand().contains(card)) {
                    player.getPlayerDeck().getHand().remove(card);
                    player.getPlayerDeck().getDiscardPile().add(card);
                    theIds.add(card.getId());
                }
            }
        } else if (source.equals(AbstractPlayground.ZoneType.HAND) && destination.equals(AbstractPlayground.ZoneType.TRASH)) {
            for (Card card : cardsToMove) {
                if (player.getPlayerDeck().getHand().contains(card)) {
                    player.getPlayerDeck().getHand().remove(card);
                    playground.getTrash().add(card);
                    theIds.add(card.getId());
                }
            }
        } else if (source.equals(AbstractPlayground.ZoneType.BUY) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                // TODO: BuyCard Implementierung von Paula hier einfügen.
                player.getPlayerDeck().getDiscardPile().add(card);
                theIds.add(card.getId());
            }
        } else if (source.equals(AbstractPlayground.ZoneType.DRAW) && destination.equals(AbstractPlayground.ZoneType.TEMP)) {
            // TODO: Implementierung im Playerdeck, dass der Spieler eine bestimmte Anzahl an Karten nachziehen kann.
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getCardsDeck().remove(card);
                player.getPlayerDeck().getTemp().add(card);
            }
        } else if (source.equals(AbstractPlayground.ZoneType.DRAW) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getCardsDeck().remove(card);
                player.getPlayerDeck().getDiscardPile().add(card);
            }
        } else if (source.equals(AbstractPlayground.ZoneType.DISCARD) && destination.equals(AbstractPlayground.ZoneType.DRAW)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getDiscardPile().remove(card);
                player.getPlayerDeck().getCardsDeck().add(card);
                Collections.shuffle(player.getPlayerDeck().getCardsDeck());
            }
        } else if (source.equals(AbstractPlayground.ZoneType.TEMP) && destination.equals(AbstractPlayground.ZoneType.HAND)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getTemp().remove(card);
                player.getPlayerDeck().getHand().add(card);
                theIds.add(card.getId());
            }
            if (true) {
                // TODO: Checken, ob nextAction gegeben und dann von Hand auf Discard.
            }
        } else if (source.equals(AbstractPlayground.ZoneType.TEMP) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getTemp().remove(card);
                player.getPlayerDeck().getDiscardPile().add(card);
                theIds.add(card.getId());
            }
        }
        if (!theIds.isEmpty()) {
            CardMovedMessage cardMovedMessage = new CardMovedMessage(theIds, source, destination, gameID);
            playground.getGameService().sendToSpecificPlayer(player, cardMovedMessage);
        }
        return true;
    }

    // vorläufige Hilfsmethode //
    private void getCard(short cardID) {

        for (int i = 0; i < player.getPlayerDeck().getHand().size(); i++) {

            if (player.getPlayerDeck().getHand().get(i).getId() == cardID) {
                theCard = player.getPlayerDeck().getHand().get(i);
                break;
            }

        }
    }
}
