package de.uol.swp.server.game.phase;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.*;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.request.OptionalActionRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;
import de.uol.swp.common.game.messages.CardMovedMessage;
import de.uol.swp.common.game.messages.ChooseNextActionMessage;
import de.uol.swp.common.game.messages.SelectCardsFromHandMessage;
import de.uol.swp.common.game.messages.ShowCardMessage;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ActionCardExecution {

    private static final Logger LOG = LogManager.getLogger(ActionCardExecution.class);

    //TODO
    /*

    Was passiert, wenn das Attribut executeType andere Spieler involviert?


     */

    //TODO: Subscribe Methode -> waitedForPlayerInput prüfen -> auf false setzen -> benötigen Antwort vom Client
    // -> ChooseCard (IDs der gewählten Karten) und ChooseNextAction (gewählte Aktion)

    private short cardID;
    private Playground playground;
    private Player player;
    private ActionCard theCard;
    private UUID gameID;
    private List<Player> players;
    //Liste aller Unteraktionen einer Aktion
    private List<CardAction> nextActions = new ArrayList<>();

    //Ob auf eine Auswahl oder Reaktion des Spielers gewartet werden muss
    private boolean waitedForPlayerInput;

    //Index der aktuell auszuführenden Aktion
    private int actualStateIndex;

    //Ob alle Unteraktionen einer Aktion ausgeführt wurden
    private boolean finishedNextActions;

    //Index der aktuell auszuführenden Unteraktion
    private int nextActionIndex;
    //Ob eine optionale Aktion ausgeführt werden soll
    private boolean executeOptionalAction;

    public ActionCardExecution(short cardID, Playground playground) {
        this.waitedForPlayerInput = false;
        this.actualStateIndex = 0;
        this.playground = playground;
        this.cardID = cardID;
        this.player = playground.getActualPlayer();
        getCardFromHand(cardID);
        this.gameID = playground.getID();
        this.players = playground.getPlayers();
        this.nextActionIndex = 0;
        this.finishedNextActions = true;
        this.executeOptionalAction = false;
    }

    /**
     * Führt eine optionale Aktion oder überspringt diese (je nach Entscheidung des Spielers)
     *
     * @param response Die OptionalActionResponse
     * @author Julia
     * @since Sprint7
     */
    @Subscribe
    public void onOptionalActionResponse(OptionalActionResponse response) {
        if (response.getGameID().equals(gameID)) {
            List<Player> playerList = new ArrayList<>();
            playerList.add(player);

            if (!response.isExecute()) {
                if (!response.isSubAction()) {
                    actualStateIndex++;
                    execute();
                } else {
                    nextActionIndex++;
                    executeNextActions(playerList);
                }
            } else {
                executeOptionalAction = true;
                if (!response.isSubAction()) {
                    execute();
                } else {
                    executeNextActions(playerList);
                }
            }
        }
    }

    /**
     * Führt beim Aufruf alle enthaltenen Aktionen der Aktionskarte aus
     *
     * @return
     * @author KenoO, Julia
     */
    public boolean execute() {
        //????????????????????????????????????
        //TODO Some pretty nice and clean Code to Execute the Shit out of that Card
        while (actualStateIndex < theCard.getActions().size() && !waitedForPlayerInput && finishedNextActions) {
            CardAction action = theCard.getActions().get(actualStateIndex);
            getNextActions(action);
            if (action instanceof ComplexCardAction && ((ComplexCardAction) action).isExecutionOptional() && !executeOptionalAction) {
                playground.getGameService().sendToSpecificPlayer(player, new OptionalActionRequest(gameID, player.getTheUserInThePlayer(), action, false));
            } else {
                List<Player> playerList = getAffectedPlayers(action);
                if (!(action instanceof GetCard) && !(action instanceof Move)) {
                    if (!executeCardAction(action, playerList, false)) return false;
                } else {
                    //TODO
                }
                if (nextActions.isEmpty()) {
                    finishedNextActions = true;
                    nextActionIndex = 0;
                }

                //TODO: ggf. gegebene nextActions müssen wenn Spielerantwort da ist, ausgeführt werden
                // -> über finishedNextActions kann dies geprüft werden
                if (!nextActions.isEmpty() && !waitedForPlayerInput) {
                    if (!executeNextActions(playerList)) return false;
                }
            }
            executeOptionalAction = false;
        }

        checkIfComplete();
        return true;
    }

    //TODO: Was passiert, wenn eine Aktion false zurückgibt?

    /**
     * Überprüft ob die Karte vollständig abgearbeitet ist
     *
     * @author Julia
     * @since Sprint7
     */
    private boolean checkIfComplete() {
        if (actualStateIndex == theCard.getActions().size() && !waitedForPlayerInput && finishedNextActions) {
            playground.getCompositePhase().finishedActionCardExecution(player, theCard);
            return true;
        }
        return false;
    }

    //TODO: Was ist, wenn aus der vorherigen Aktion Karten übergeben werden müssen?
    // z.B.: GetCard liefert Karten, die danach mit Move irgendwo hin bewegt werden müssen

    /**
     * Führt alle Unteraktionen einer Action aus
     *
     * @return false sobald eine Aktion nicht erfolgreich ausgeführt werden konnte
     * @author Julia
     * @since Sprint7
     */
    private boolean executeNextActions(List<Player> playerList) {
        finishedNextActions = false;
        while (nextActionIndex < nextActions.size() && !waitedForPlayerInput) {
            CardAction action = nextActions.get(nextActionIndex);
            if (action instanceof ComplexCardAction && ((ComplexCardAction) action).isExecutionOptional() && !executeOptionalAction) {
                playground.getGameService().sendToSpecificPlayer(player, new OptionalActionRequest(gameID, player.getTheUserInThePlayer(), action, true));
            } else {
                if (!(action instanceof GetCard) && !(action instanceof Move)) {
                    if (!executeCardAction(action, playerList, true)) return false;
                } else {
                    //TODO
                }
            }
            executeOptionalAction = false;
        }

        //Alle Unteraktionen wurden vollständig ausgeführt
        if (nextActionIndex == nextActions.size() && !waitedForPlayerInput) {
            finishedNextActions = true;
            nextActions.clear();
            nextActionIndex = 0;
            if (!checkIfComplete()) {
                actualStateIndex++;
                execute();
            }
        }

        return true;
    }


    /**
     * Liefert eine Liste mit allen Unteraktionen einer Aktion
     *
     * @param action die Kartenaktion, deren Unteraktionen herausgefiltert werden sollen
     * @return Liste mit allen Unteraktionen der Aktion
     * @author Julia
     * @since Sprint7
     */
    private List<CardAction> getNextActions(CardAction action) {
        if (!(action instanceof ComplexCardAction)) return nextActions;
        ComplexCardAction complexCardAction = (ComplexCardAction) action;
        if (complexCardAction.getNextAction() == null) return nextActions;
        nextActions.add(complexCardAction.getNextAction());
        return getNextActions(complexCardAction.getNextAction());
    }


    /**
     * Ermittelt alle Spieler, auf die eine Aktion ausgeführt werden muss
     *
     * @param action Die CardAction
     * @return Spielerliste mit allen betroffenen Spielern
     * @author Julia
     * @since Sprint7
     */
    private List<Player> getAffectedPlayers(CardAction action) {
        List<Player> affectedPlayers = new ArrayList<>();
        if (!(action instanceof ComplexCardAction)) {
            affectedPlayers.add(player);
            return affectedPlayers;
        }

        CardAction.ExecuteType executeType = ((ComplexCardAction) action).getExecuteType();
        if (executeType == CardAction.ExecuteType.ALL) {
            affectedPlayers = players;
        } else if (executeType == CardAction.ExecuteType.OTHERS) {
            for (Player p : players) {
                if (!p.equals(player)) affectedPlayers.add(p);
            }
        } else if (executeType == CardAction.ExecuteType.NEXT) {
            int index = players.indexOf(player);
            affectedPlayers.add(players.get(++index % players.size()));
        } else if (executeType == CardAction.ExecuteType.LAST) {
            int index = players.indexOf(player);
            affectedPlayers.add(players.get(--index % players.size()));
        } else {
            affectedPlayers.add(player);
        }

        //Prüfe ob einer der betroffenen Spieler eine Reaktionskarte auf der Hand hat
        if (theCard.getType() == ActionCard.ActionType.Attack) {
            for (Player p : affectedPlayers) {
                if (!p.equals(this.player) && checkForReactionCard(p)) {
                    players.remove(p);
                    affectedPlayers.remove(p);
                }
            }

        }
        return affectedPlayers;
    }

    /**
     * Prüft, ob ein Spieler eine Reaktionskarte auf der Hand hat und spielt diese dann aus
     *
     * @param player Der Spieler, dessen Hand überprüft werden soll
     * @return true, wenn er eine Reaktionskarte auf der Hand hat, sonst false
     * @author Julia
     * @since Sprint7
     */
    private boolean checkForReactionCard(Player player) {
        for (Card card : player.getPlayerDeck().getHand()) {
            if (card instanceof ActionCard && ((ActionCard) card).getType() == ActionCard.ActionType.Reaction) {
                player.getPlayerDeck().getHand().remove(card);
                player.getPlayerDeck().getActionPile().add(card); //?
                //TODO: Message an Client
                return true;
            }
        }
        return false;
    }

    /**
     * Führt unabhängige Kartenaktionen aus. (NUR AKTIONEN, DIE KEINE RÜCKGABE BENÖTIGEN (ODER AKTIONEN, AUF OBERSTER EBENE))
     *
     * @param action     Die auszuführende Kartenaktion
     * @param thePlayers Liste aller Player, auf die die Aktion ausgeführt werden soll
     * @param subAction  Ob es sich um eine Unteraktion handelt
     * @return Ob die Ausführung erfolgreich war.
     * @author KenoO
     */
    private boolean executeCardAction(CardAction action, List<Player> thePlayers, boolean subAction) {
        if (!(action instanceof ChooseNextAction) && !(action instanceof ChooseCard)) {
            if (subAction) nextActionIndex++;
            else actualStateIndex++;
        }
        if (action instanceof AddCapablePlayerActivity)
            return executeAddCapablePlayerActivity((AddCapablePlayerActivity) action);
        if (action instanceof ChooseCard)
            return executeChooseCard((ChooseCard) action, thePlayers);
        if (action instanceof ChooseNextAction)
            return executeChooseNextAction((ChooseNextAction) action);
        if (action instanceof ForEach)
            return executeForEach((ForEach) action);
        if (action instanceof If)
            return executeIf((If) action, thePlayers);
        if (action instanceof ShowCard)
            return executeShowCard((ShowCard) action, thePlayers);
        if (action instanceof UseCard)
            return executeUseCard((UseCard) action);
        if (action instanceof While)
            return executeWhile((While) action, thePlayers);

        return false;
    }

    /**
     * Führt eine Kartenaktion mit Eingabe von Karten aus und gibt das Ergebnis zurück
     *
     * @param action Auszuführende Aktion
     * @param input  Eingabe an Karten
     * @return Ergebnis
     */
    //TODO: Die Aktion muss auch auf mehrere Spieler ausgeführt werden können
    // -> executeGetCard und executeMove müssen entsprechend angepasst werden
    private ArrayList<Card> executeCardAction(CardAction action, ArrayList<Card> input) throws NullPointerException {
        if (action == null || input == null) throw new NullPointerException("Actions nor input can't be null");
        //TODO
        return null;
    }

    /**
     * Filtert ein Array an Karten anhand der Eigenschaften in der übergebenen Aktion
     *
     * @param action Die Kartenaktion mit den Filtereigenschaften
     * @param cards  Die zu filternden Karten
     * @return Das gefilterte Kartenarray
     * @author Julia
     * @since Sprint6
     */
    private ArrayList<Card> filterCards(ComplexCardAction action, ArrayList<Card> cards) {
        Card.Type allowedType = action.getAllowedCardType();
        if (allowedType == Card.Type.ActionCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.ActionCard);
        } else if (allowedType == Card.Type.ReactionCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.ReactionCard);
        } else if (allowedType == Card.Type.MoneyCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.MoneyCard);
        } else if (allowedType == Card.Type.ValueCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.ValueCard);
        } else if (allowedType == Card.Type.Cursecard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.Cursecard);
        }

        if (action.getHasCost() != null) {
            cards.removeIf(c -> c.getCosts() < action.getHasCost().getMin() || c.getCosts() > action.getHasCost().getMax());
        }

        if (action.getHasWorth() != null) {
            cards.removeIf(c -> c.getCardType() == Card.Type.ActionCard || c.getCardType() == Card.Type.ReactionCard);
            List<Card> tmp = new ArrayList<>();
            cards.forEach(card -> {
                if (card.getCardType() == Card.Type.ValueCard) {
                    if (((ValueCard) card).getValue() < action.getHasWorth().getMin() || ((ValueCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                } else if (card.getCardType() == Card.Type.MoneyCard) {
                    if (((MoneyCard) card).getValue() < action.getHasWorth().getMin() || ((MoneyCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                } else {
                    if (((CurseCard) card).getValue() < action.getHasWorth().getMin() || ((CurseCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                }
            });

            cards.removeAll(tmp);
        }

        return cards;
    }

    private boolean executeWhile(While action, List<Player> thePlayers) {
        //TODO
        return false;
    }

    private boolean executeUseCard(UseCard action) {
        if (action.getCardId() == 0) {
            LOG.debug("No CardId specified! Using current Card(" + cardID + ")");
            action.setCardId(cardID);
        }
        ActionCardExecution execution = new ActionCardExecution(action.getCardId(), playground);

        for (int i = 0; i < action.getCount(); i++) {
            if (!execution.execute()) return false;
        }
        return true;
    }

    private boolean executeIf(If action, List<Player> thePlayers) {
        //TODO
        return false;
    }

    private ArrayList<Card> executeGetCard(GetCard action) {
        if (action.getCardSource() != AbstractPlayground.ZoneType.NONE) {
            switch (action.getCardSource()) {

                case TRASH:
                    action.setCards(playground.getTrash());
                    break;
                case HAND:
                    action.setCards(player.getPlayerDeck().getHand());
                    break;
                case BUY:
                    playground.getCardsPackField().getCards().getAllCards().forEach(card -> {
                        if (playground.getCardField().containsKey(card.getId())) {
                            if (playground.getCardField().get(card.getId()) > 0) {
                                action.getCards().add(card);
                            }
                        }
                    });
                    break;
                case DRAW:
                    action.setCards(player.getPlayerDeck().getCardsDeck());
                    break;
                case DISCARD:
                    action.setCards(player.getPlayerDeck().getDiscardPile());
                    break;
                case TEMP:
                    action.setCards(player.getPlayerDeck().getTemp());
                    break;
            }
            action.setCards(filterCards(action, action.getCards()));
        }
        return action.getCards();
    }

    /**
     * Führt eine Reihe von Aktionen auf eine beliebige Anzahl an Karten aus
     *
     * @param action Die ForEach-Aktion
     * @return
     */
    private boolean executeForEach(ForEach action) {
        try {
            for (Card card : action.getCards()) {
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card);
                for (CardAction cardAction : action.getActions()) {
                    cards = executeCardAction(cardAction, cards);
                }
            }
        } catch (NullPointerException e) {
            LOG.error(e.fillInStackTrace());
            return false;
        }
        return true;
    }

    /**
     * Erhöht availableActions, availableBuys oder additionalMoney des Players um den angegebenen Wert.
     *
     * @param activity Aktion
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeAddCapablePlayerActivity(AddCapablePlayerActivity activity) {
        if (activity.getActivity() == AbstractPlayground.PlayerActivityValue.ACTION) {
            player.setAvailableActions(activity.getCount() + player.getAvailableActions());
        } else if (activity.getActivity() == AbstractPlayground.PlayerActivityValue.BUY) {
            player.setAvailableBuys(activity.getCount() + player.getAvailableBuys());
        } else {
            player.setAdditionalMoney(activity.getCount() + player.getAdditionalMoney());
        }
        return true;
    }

    /**
     * Sendet eine Message mit der ID der Karte, die angezeigt werden soll und der Zone,
     * in der sie angezeigt werden soll an den aktuellen Spieler.
     *
     * @param showCard
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeShowCard(ShowCard showCard, List<Player> thePlayers) {
        for (Player player : thePlayers) {
            playground.getGameService().sendToSpecificPlayer(player, new ShowCardMessage(showCard.getCard().getId(), showCard.getCardSource(), gameID));
        }
        return true;
    }

    /**
     * Zählt ein übergebenes KartenArray und übergibt die Größe.
     *
     * @return 0 = Übergebenes Array ist leer. | sonst die größe des KartenArrays
     */

    private int executeCount(Count action) {
        if (!action.getCards().isEmpty()) {
            return action.getCards().size();
        }
        return 0;
    }

    /**
     * Schickt dem Spieler eine Nachricht das er eine bestimmte Anzahl an Karten auswählen kann.
     *
     * @param action Die Anzahl der Karten
     * @return true(? ? ?)
     */
    private boolean executeChooseCard(ChooseCard action, List<Player> thePlayers) {
        for (Player player : thePlayers) {
            waitedForPlayerInput = true;
            playground.getGameService().sendToSpecificPlayer(player, new SelectCardsFromHandMessage(action.getCount(), action.getCardSource(), action.getCardDestination()));
        }
        return true;

    }

    /**
     * Sendet dem Spieler eine Nachricht mit den Aktionen, aus denen er eine auswählen kann
     *
     * @param chooseNextAction
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeChooseNextAction(ChooseNextAction chooseNextAction) {
        waitedForPlayerInput = true;
        playground.getGameService().sendToSpecificPlayer(player, new ChooseNextActionMessage(gameID, chooseNextAction.getNextActions()));
        return true;
    }

    /**
     * Bewegt Karten von einem Ort zum anderen. ----------WIP----------
     *
     * @return true(? ? ?)
     */
    private boolean executeMoveAction(Move action, List<Player> thePlayers) {
        for (Player player : thePlayers) {
            ArrayList<Short> theIds = new ArrayList<>();
            if (action.getCardSource().equals(AbstractPlayground.ZoneType.HAND) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
                for (Card card : action.getCardsToMove()) {
                    if (player.getPlayerDeck().getHand().contains(card)) {
                        player.getPlayerDeck().getHand().remove(card);
                        player.getPlayerDeck().getDiscardPile().add(card);
                        theIds.add(card.getId());
                    }
                }
            } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.HAND) && action.getCardDestination().equals(AbstractPlayground.ZoneType.TRASH)) {
                for (Card card : action.getCardsToMove()) {
                    if (player.getPlayerDeck().getHand().contains(card)) {
                        player.getPlayerDeck().getHand().remove(card);
                        playground.getTrash().add(card);
                        theIds.add(card.getId());
                    }
                }
            } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.BUY) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
                for (Card card : action.getCardsToMove()) {
                    // TODO: BuyCard Implementierung von Paula hier einfügen.
                    player.getPlayerDeck().getDiscardPile().add(card);
                    theIds.add(card.getId());
                }
            } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.DRAW) && action.getCardDestination().equals(AbstractPlayground.ZoneType.TEMP)) {
                // TODO: Implementierung im Playerdeck, dass der Spieler eine bestimmte Anzahl an Karten nachziehen kann.
                for (Card card : action.getCardsToMove()) {
                    player.getPlayerDeck().getCardsDeck().remove(card);
                    player.getPlayerDeck().getTemp().add(card);
                }
            } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.DRAW) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
                for (Card card : action.getCardsToMove()) {
                    player.getPlayerDeck().getCardsDeck().remove(card);
                    player.getPlayerDeck().getDiscardPile().add(card);
                }
            } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.DISCARD) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DRAW)) {
                for (Card card : action.getCardsToMove()) {
                    player.getPlayerDeck().getDiscardPile().remove(card);
                    player.getPlayerDeck().getCardsDeck().add(card);
                    Collections.shuffle(player.getPlayerDeck().getCardsDeck());
                }
            } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.TEMP) && action.getCardDestination().equals(AbstractPlayground.ZoneType.HAND)) {
                for (Card card : action.getCardsToMove()) {
                    player.getPlayerDeck().getTemp().remove(card);
                    player.getPlayerDeck().getHand().add(card);
                    theIds.add(card.getId());
                }
                if (true) {
                    // TODO: Checken, ob nextAction gegeben und dann von Hand auf Discard.
                }
            } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.TEMP) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
                for (Card card : action.getCardsToMove()) {
                    player.getPlayerDeck().getTemp().remove(card);
                    player.getPlayerDeck().getDiscardPile().add(card);
                    theIds.add(card.getId());
                }
            }
            if (!theIds.isEmpty()) {
                CardMovedMessage cardMovedMessage = new CardMovedMessage(theIds, action.getCardSource(), action.getCardDestination(), gameID);
                playground.getGameService().sendToSpecificPlayer(player, cardMovedMessage);
            }
        }
        return true;
    }

    // vorläufige Hilfsmethode //
    private void getCardFromHand(short cardID) {

        for (int i = 0; i < player.getPlayerDeck().getHand().size(); i++) {

            if (player.getPlayerDeck().getHand().get(i).getId() == cardID) {
                theCard = (ActionCard) player.getPlayerDeck().getHand().get(i);
                break;
            }
        }
    }
}
