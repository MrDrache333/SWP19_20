package de.uol.swp.server.game.phase;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.*;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.request.ChooseCardRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.request.OptionalActionRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;
import de.uol.swp.common.game.messages.ChooseNextActionMessage;
import de.uol.swp.common.game.messages.MoveCardMessage;
import de.uol.swp.common.game.messages.ShowCardMessage;
import de.uol.swp.common.game.messages.UpdateCardCounterMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ActionCardExecution {

    private static final Logger LOG = LogManager.getLogger(ActionCardExecution.class);

    private short cardID;
    private Playground playground;
    private Player player;
    private ActionCard theCard;
    private UUID gameID;
    private List<Player> players;
    private List<User> chooseCardPlayers = new ArrayList<>();
    //Liste neuer Handkarten
    private ArrayList<Short> newHandCards = new ArrayList<>();
    //Liste aller Unteraktionen einer Aktion
    private List<CardAction> nextActions = new ArrayList<>();
    private Card inputCard;
    private AbstractPlayground.ZoneType chooseCardSource;
    //

    //Ob auf eine Auswahl oder Reaktion des Spielers gewartet werden muss
    private boolean waitedForPlayerInput;

    //Index der aktuell auszuführenden Aktion
    private int actualStateIndex;

    private boolean startedNextActions;
    //Ob alle Unteraktionen einer Aktion ausgeführt wurden
    private boolean finishedNextActions;

    //Index der aktuell auszuführenden Unteraktion
    private int nextActionIndex;
    //Ob eine optionale Aktion ausgeführt werden soll
    private boolean executeOptionalAction;

    //Ob die Aktionskarte entsorgt werden soll
    private boolean removeCardAfter;

    //Wieviele Karten wurden

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
        this.startedNextActions = false;
    }

    /**
     * Führt eine optionale Aktion oder überspringt diese (je nach Entscheidung des Spielers)
     *
     * @param response Die OptionalActionResponse
     * @author Julia
     * @since Sprint 7
     */
    @Subscribe
    public void onOptionalActionResponse(OptionalActionResponse response) {
        LOG.debug("OptionalActionResponse von " + response.getPlayer().getUsername() + " erhalten");
        if (response.getGameID().equals(gameID)) {
            if (!response.isExecute()) actualStateIndex++;
            else executeOptionalAction = true;
            waitedForPlayerInput = false;
            execute();
        }
    }

    /**
     * Reaktion auf eine ChooseCardResponse
     *
     * @param response Die ChooseCardResponse
     * @author Ferit, Julia
     * @since Sprint 7
     */
    @Subscribe
    public void onChooseCardResponse(ChooseCardResponse response) {
        LOG.debug("ChooseCardResponse von " + response.getPlayer().getUsername() + " erhalten");
        List<Player> p = new ArrayList<>();
        Player helpPlayer = helpMethodToGetThePlayerFromUser(response.getPlayer());
        p.add(helpPlayer);
        if (response.getGameID().equals(gameID) && waitedForPlayerInput && chooseCardPlayers.contains(response.getPlayer())) {
            waitedForPlayerInput = false;
            if (!startedNextActions) actualStateIndex++;
            if (response.getCards().size() == 1) {
                inputCard = helpMethod2GetTheCard(response.getCards().get(0));
            }

            ArrayList<Card> cards = new ArrayList<>();
            response.getCards().forEach(c -> cards.add(helpMethod2GetTheCard(c)));

            if (response.getDirectHand()) {
                helpPlayer.getPlayerDeck().getHand().addAll(cards);
                newHandCards.addAll(response.getCards());
                if (chooseCardSource == AbstractPlayground.ZoneType.BUY) {
                    Map<Short, Integer> newCount = new HashMap<>();
                    int size = cards.size();
                    for (int i = 0; i < size; i++) {
                        Card card = cards.get(i);
                        short id = cards.get(i).getId();
                        int count = playground.getCardField().get(id);
                        if (count > 0) {
                            playground.getCardField().replace(id, --count);
                            newCount.put(id, count);
                        }
                        if (cards.size() != size) {
                            cards.add(i, card);
                        }
                    }
                    UpdateCardCounterMessage message = new UpdateCardCounterMessage(gameID, player.getTheUserInThePlayer(), newCount);
                    playground.getGameService().sendToAllPlayers(gameID, message);
                }
            } else if (nextActions.get(nextActionIndex) instanceof Move) {
                ((Move) nextActions.get(nextActionIndex)).setCardsToMove(cards);
            } else if (nextActions.get(nextActionIndex) instanceof ForEach) {
                ((ForEach) nextActions.get(nextActionIndex)).setCards(cards);
            }

            executeNextActions(p);
        }
    }

    /**
     * Führt beim Aufruf alle enthaltenen Aktionen der Aktionskarte aus
     *
     * @return
     * @author KenoO, Julia
     * @since Sprint 6
     */
    public boolean execute() {
        while (actualStateIndex < theCard.getActions().size() && !waitedForPlayerInput && finishedNextActions) {
            startedNextActions = false;
            CardAction action = theCard.getActions().get(actualStateIndex);
            getNextActions(action);
            if (action instanceof ComplexCardAction && ((ComplexCardAction) action).isExecutionOptional() && !executeOptionalAction) {
                playground.getGameService().sendToSpecificPlayer(player, new OptionalActionRequest(gameID, player.getTheUserInThePlayer(), ((ComplexCardAction) action).getExecutionOptionalMessage()));
                waitedForPlayerInput = true;
            } else {
                if (!(action instanceof ChooseNextAction) && !(action instanceof ChooseCard)) {
                    actualStateIndex++;
                }
                List<Player> playerList = getAffectedPlayers(action);
                if (!(action instanceof GetCard) && !(action instanceof Move)) {
                    if (!executeCardAction(action, playerList)) return false;
                } else {
                    executeCardAction(action, null, playerList, false);
                }
                if (nextActions.isEmpty()) {
                    finishedNextActions = true;
                    nextActionIndex = 0;
                } else {
                    finishedNextActions = false;
                }

                if (!nextActions.isEmpty() && !waitedForPlayerInput) {
                    if (!executeNextActions(playerList)) return false;
                }
            }
            if (action instanceof ComplexCardAction && ((ComplexCardAction) action).isRemoveCardAfter()) {
                removeCardAfter = true;
            }
            executeOptionalAction = false;
        }

        checkIfComplete();
        return true;
    }

    /**
     * Führt alle Unteraktionen einer Action aus
     *
     * @return false sobald eine Aktion nicht erfolgreich ausgeführt werden konnte
     * @author Julia
     * @since Sprint 7
     */
    private boolean executeNextActions(List<Player> playerList) {
        startedNextActions = true;
        while (nextActionIndex < nextActions.size() && !waitedForPlayerInput) {
            CardAction action = nextActions.get(nextActionIndex);
            if (!(action instanceof ChooseNextAction) && !(action instanceof ChooseCard)) {
                nextActionIndex++;
            }
            if (!(action instanceof GetCard) && !(action instanceof Move)) {
                if (!executeCardAction(action, playerList)) return false;
            } else {
                executeCardAction(action, null, playerList, false);
            }
        }

        //Alle Unteraktionen wurden vollständig ausgeführt
        if (nextActionIndex == nextActions.size() && !waitedForPlayerInput) {
            finishedNextActions = true;
            nextActions.clear();
            nextActionIndex = 0;
            if (!checkIfComplete()) {
                execute();
            }
        }

        return true;
    }

    /**
     * Führt unabhängige Kartenaktionen aus. (NUR AKTIONEN, DIE KEINE RÜCKGABE BENÖTIGEN (ODER AKTIONEN, AUF OBERSTER EBENE))
     *
     * @param action     Die auszuführende Kartenaktion
     * @param thePlayers Liste aller Player, auf die die Aktion ausgeführt werden soll
     * @return Ob die Ausführung erfolgreich war.
     * @author KenoO
     * @since Sprint 6
     */
    private boolean executeCardAction(CardAction action, List<Player> thePlayers) {
        if (action == null) throw new NullPointerException("Action can't be null");
        if (action instanceof AddCapablePlayerActivity)
            return executeAddCapablePlayerActivity((AddCapablePlayerActivity) action);
        if (action instanceof ChooseCard)
            return executeChooseCard((ChooseCard) action, thePlayers);
        if (action instanceof ChooseNextAction)
            return executeChooseNextAction((ChooseNextAction) action);
        if (action instanceof ForEach)
            return executeForEach((ForEach) action, thePlayers);
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
     * @author KenoO, Julia
     * @since Sprint 6
     */
    private ArrayList<Card> executeCardAction(CardAction action, ArrayList<Card> input, List<Player> playerList, boolean foreach) throws NullPointerException {
        if (action == null) throw new NullPointerException("Action can't be null");
        for (Player player : playerList) {
            if (action instanceof Move) {
                if (input == null && ((Move) action).getCardsToMove() == null)
                    throw new NullPointerException("No cards to move!");
                if (input != null) ((Move) action).setCardsToMove(input);
                executeMoveAction((Move) action, player);
            } else if (action instanceof GetCard) {
                GetCard getAction = (GetCard) action;
                ArrayList<Card> c = executeGetCard(getAction, player);
                if (getAction.isDirectHand()) {
                    int size = c.size();
                    for (int i = 0; i < size; i++) {
                        Card card = c.get(i);
                        player.getPlayerDeck().getHand().add(card);
                        newHandCards.add(card.getId());
                    }
                    List<Card> remove = new ArrayList<>();
                    if (getAction.getCardSource() == AbstractPlayground.ZoneType.DRAW) {
                        for (int i = 0; i < size; i++) {
                            Card card = c.get(i);
                            player.getPlayerDeck().getCardsDeck().stream().filter(a -> a.getId() == card.getId()).findFirst().ifPresent(remove::add);
                            if (c.size() != size) {
                                c.add(i, card);
                            }
                        }
                        remove.forEach(t -> player.getPlayerDeck().getCardsDeck().remove(t));
                    } else if (getAction.getCardSource() == AbstractPlayground.ZoneType.DISCARD) {
                        for (int i = 0; i < size; i++) {
                            Card card = c.get(i);
                            player.getPlayerDeck().getDiscardPile().stream().filter(a -> a.getId() == card.getId()).findFirst().ifPresent(remove::add);
                            if (c.size() != size) {
                                c.add(i, card);
                            }
                        }
                        remove.forEach(t -> player.getPlayerDeck().getDiscardPile().remove(t));
                    } else if (getAction.getCardSource() == AbstractPlayground.ZoneType.TEMP) {
                        for (int i = 0; i < size; i++) {
                            Card card = c.get(i);
                            player.getPlayerDeck().getTemp().stream().filter(a -> a.getId() == card.getId()).findFirst().ifPresent(remove::add);
                            if (c.size() != size) {
                                c.add(i, card);
                            }
                        }
                        remove.forEach(t -> player.getPlayerDeck().getTemp().remove(t));
                    } else if (getAction.getCardSource() == AbstractPlayground.ZoneType.TRASH) {
                        for (int i = 0; i < size; i++) {
                            Card card = c.get(i);
                            playground.getTrash().stream().filter(a -> a.getId() == card.getId()).findFirst().ifPresent(remove::add);
                            if (c.size() != size) {
                                c.add(i, card);
                            }
                        }
                        remove.forEach(t -> playground.getTrash().remove(t));
                    }

                } else if (!foreach) {
                    CardAction nextAction = nextActions.get(nextActionIndex);
                    if (nextAction instanceof Move) {
                        ((Move) nextAction).setCardsToMove(c);
                    } else if (nextAction instanceof ShowCard && c.size() == 1) {
                        ((ShowCard) nextAction).setCard(c.get(0));
                    } else if (nextAction instanceof ForEach) {
                        ((ForEach) nextAction).setCards(c);
                    }
                }
                return c;
            }
        }

        return null;
    }

    /**
     * Filtert ein Array an Karten anhand der Eigenschaften in der übergebenen Aktion
     *
     * @param action Die Kartenaktion mit den Filtereigenschaften
     * @param cards  Die zu filternden Karten
     * @return Das gefilterte Kartenarray
     * @author Julia
     * @since Sprint 6
     */
    private ArrayList<Card> filterCards(ComplexCardAction action, ArrayList<Card> cards) {
        Card.Type allowedType = action.getAllowedCardType();
        if (allowedType == Card.Type.ActionCard) {
            cards.removeIf(c -> c == null || c.getCardType() != Card.Type.ActionCard);
        } else if (allowedType == Card.Type.ReactionCard) {
            cards.removeIf(c -> c == null || c.getCardType() != Card.Type.ReactionCard);
        } else if (allowedType == Card.Type.MoneyCard) {
            cards.removeIf(c -> c == null || c.getCardType() != Card.Type.MoneyCard);
        } else if (allowedType == Card.Type.ValueCard) {
            cards.removeIf(c -> c == null || c.getCardType() != Card.Type.ValueCard);
        } else if (allowedType == Card.Type.Cursecard) {
            cards.removeIf(c -> c == null || c.getCardType() != Card.Type.Cursecard);
        }

        if (action.getHasCost() != null) {
            cards.removeIf(c -> c == null || c.getCosts() < action.getHasCost().getMin() || c.getCosts() > action.getHasCost().getMax());
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

    /**
     * Methode, um eine bestimmte Anzahl Karten aus einer Zone zu bekommen
     *
     * @param action
     * @param player
     * @return Liste der Karten
     * @author KenoO, Julia
     * @since Sprint 6
     */
    private ArrayList<Card> executeGetCard(GetCard action, Player player) {
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
                    if(action.getCards().size() < action.getCount() && action.getCount() < 254) {
                        int missingCards = action.getCount() - action.getCards().size();
                        List<Card> discard = player.getPlayerDeck().getDiscardPile();
                        Collections.shuffle(discard);
                        int i = 0;
                        while(i < missingCards && i < discard.size()) {
                            action.getCards().add(discard.get(i));
                            i++;
                        }
                        player.getPlayerDeck().getDiscardPile().clear();
                        player.getPlayerDeck().getCardsDeck().addAll(0, discard);
                    }
                    break;
                case DISCARD:
                    action.setCards(player.getPlayerDeck().getDiscardPile());
                    break;
                case TEMP:
                    action.setCards(player.getPlayerDeck().getTemp());
                    break;
            }
            ArrayList<Card> cards = filterCards(action, action.getCards());
            if (cards.size() > action.getCount()) {
                ArrayList<Card> tmp = new ArrayList<>();
                for (int i = 0; i < action.getCount(); i++) {
                    tmp.add(cards.get(i));
                }
                action.setCards(tmp);
            } else action.setCards(cards);

        }
        return action.getCards();
    }

    /**
     * Führt eine Reihe von Aktionen auf eine beliebige Anzahl an Karten aus
     *
     * @param action Die ForEach-Aktion
     * @return
     * @author KenoO, Julia
     * @since Sprint 6
     */
    private boolean executeForEach(ForEach action, List<Player> playerList) {
        try {
            int size = action.getCards().size();
            for (int i = 0; i < size; i++) {
                Card card = action.getCards().get(i);
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card);
                for (CardAction cardAction : action.getActions()) {
                    if (cardAction instanceof Move || cardAction instanceof GetCard) {
                        ArrayList<Card> c = executeCardAction(cardAction, cards, playerList, true);
                        if (c != null) {
                            cards = c;
                        }
                    } else executeCardAction(cardAction, playerList);
                }
                if (action.getCards().size() != size) {
                    action.getCards().add(i, card);
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
     * @since Sprint 6
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
     * @since Sprint 6
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
     * Schickt den ausgewählten Spielern eine Nachricht, dass sie eine Anzahl an Karten auswählen können.
     *
     * @param action     Die Anzahl der Karten
     * @param thePlayers Die Player, die Karten auswählen dürfen.
     * @return
     * @author Ferit, Julia
     * @since Sprint 7
     */

    private boolean executeChooseCard(ChooseCard action, List<Player> thePlayers) {
        waitedForPlayerInput = true;
        chooseCardSource = action.getCardSource();
        for (Player playerChooseCard : thePlayers) {
            this.chooseCardPlayers.add(playerChooseCard.getTheUserInThePlayer());
            ArrayList<Card> tmp = new ArrayList<>();
            ArrayList<Short> theSelectableCards = new ArrayList<>();
            if (action.getCardSource() == AbstractPlayground.ZoneType.HAND) {
                tmp.addAll(playerChooseCard.getPlayerDeck().getHand());
            } else if (action.getCardSource() == AbstractPlayground.ZoneType.BUY) {
                List<Short> l = new ArrayList<>(playground.getCardField().keySet());
                for (Short card : l) {
                    Card c = playground.getCompositePhase().getCardFromId(playground.getCardsPackField().getCards(), card);
                    if (playground.getCardField().get(card) > 0) {
                        tmp.add(c);
                    }
                }
            }
            tmp = filterCards(action, tmp);
            if (action.getHasMoreCostThanInput() != null) {
                if (inputCard == null) return false;
                tmp.forEach(card -> {
                    if (card != null && card.getCosts() - inputCard.getCosts() <= action.getHasMoreCostThanInput().getMax()) {
                        theSelectableCards.add(card.getId());
                    }
                });
            } else {
                tmp.forEach(card -> theSelectableCards.add(card.getId()));
            }
            ChooseCardRequest request;
            if (action.getCount().getMin() == action.getCount().getMax()) {
                request = new ChooseCardRequest(this.gameID, playerChooseCard.getTheUserInThePlayer(), theSelectableCards, action.getCount().getMin(), playerChooseCard.getTheUserInThePlayer(), action.getCardSource(), "Bitte die Anzahl der Karten auswählen von dem Bereich der dir angezeigt wird!", action.isDirectHand());
            } else if (action.getCount().getMin() == 0) {
                request = new ChooseCardRequest(this.gameID, playerChooseCard.getTheUserInThePlayer(), theSelectableCards, action.getCount().getMax(), playerChooseCard.getTheUserInThePlayer(), action.getCardSource(), "Bitte die Anzahl der Karten auswählen von dem Bereich der dir angezeigt wird!", action.isDirectHand());
            } else {
                request = new ChooseCardRequest(this.gameID, playerChooseCard.getTheUserInThePlayer(), theSelectableCards, action.getCount(), playerChooseCard.getTheUserInThePlayer(), action.getCardSource(), "Bitte die Anzahl der Karten auswählen von dem Bereich der dir angezeigt wird!", action.isDirectHand());
            }
            playground.getGameService().sendToSpecificPlayer(player, request);
        }
        return true;

    }

    /**
     * Sendet dem Spieler eine Nachricht mit den Aktionen, aus denen er eine auswählen kann
     *
     * @param chooseNextAction
     * @return true
     * @author Julia
     * @since Sprint 6
     */
    private boolean executeChooseNextAction(ChooseNextAction chooseNextAction) {
        waitedForPlayerInput = true;
        playground.getGameService().sendToSpecificPlayer(player, new ChooseNextActionMessage(gameID, chooseNextAction.getNextActions()));
        return true;
    }

    /**
     * Bewegt Karten von einem Ort zum anderen
     *
     * @return true
     * @author Julia
     * @since Sprint 8
     */
    private boolean executeMoveAction(Move action, Player player) {
        ArrayList<Short> theIds = new ArrayList<>();
        action.getCardsToMove().forEach(c -> theIds.add(c.getId()));
        switch (action.getCardDestination()) {
            case DISCARD:
                player.getPlayerDeck().getDiscardPile().addAll(action.getCardsToMove());
                break;
            case TRASH:
                playground.getTrash().addAll(action.getCardsToMove());
                break;
            case DRAW:
                player.getPlayerDeck().getCardsDeck().addAll(action.getCardsToMove());
                break;
            case TEMP:
                player.getPlayerDeck().getTemp().addAll(action.getCardsToMove());
                break;
            case HAND:
                player.getPlayerDeck().getHand().addAll(action.getCardsToMove());
                break;
        }

        int size = action.getCardsToMove().size();
        List<Card> toRemove = new ArrayList<>();
        switch (action.getCardSource()) {
            case HAND:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getHand().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getHand().remove(c));
                break;
            case TEMP:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getTemp().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getTemp().remove(c));
                break;
            case DISCARD:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getDiscardPile().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getDiscardPile().remove(c));
                break;
            case DRAW:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getCardsDeck().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getCardsDeck().remove(c));
                break;
            case BUY:
                Map<Short, Integer> newCount = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    short id = card.getId();
                    int count = playground.getCardField().get(id);
                    if (count > 0) {
                        playground.getCardField().replace(id, --count);
                        newCount.put(id, count);
                    }
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                UpdateCardCounterMessage message = new UpdateCardCounterMessage(gameID, player.getTheUserInThePlayer(), newCount);
                playground.getGameService().sendToAllPlayers(gameID, message);
                break;
        }
        MoveCardMessage msg = new MoveCardMessage(gameID, player.getTheUserInThePlayer(), action);
        //TODO Evtl. Daten, die andere Spieler nicht erhalten dürfen irgendwie unkenntlich machen? (Karten anderer Spieler)
        playground.getGameService().sendToAllPlayers(gameID, msg);

        return true;
    }

    /**
     * Liefert eine Liste mit allen Unteraktionen einer Aktion
     *
     * @param action die Kartenaktion, deren Unteraktionen herausgefiltert werden sollen
     * @return Liste mit allen Unteraktionen der Aktion
     * @author Julia
     * @since Sprint 7
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
     * @since Sprint 7
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
     * @since Sprint 7
     */
    private boolean checkForReactionCard(Player player) {
        for (Card card : player.getPlayerDeck().getHand()) {
            if (card instanceof ActionCard && ((ActionCard) card).getType() == ActionCard.ActionType.Reaction) {
                player.getPlayerDeck().getHand().remove(card);
                player.getPlayerDeck().getActionPile().add(card);
                //TODO: Message an Client
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob die Karte vollständig abgearbeitet ist
     *
     * @author Julia
     * @since Sprint 7
     */
    private boolean checkIfComplete() {
        if (actualStateIndex == theCard.getActions().size() && !waitedForPlayerInput && finishedNextActions) {
            playground.getCompositePhase().finishedActionCardExecution(player, newHandCards, theCard);
            return true;
        }
        return false;
    }


    /**
     * Hilfsmethode, um die gespielte Aktionskarte aus der Hand des Spielers zu erhalten
     *
     * @param cardID ID der Aktionskarte
     * @author KenoO
     * @since Sprint 6
     */
    private void getCardFromHand(short cardID) {
        theCard = player.getPlayerDeck().getHand().stream().filter(card -> card.getId() == cardID).findFirst().map(card -> (ActionCard) card).orElse(theCard);
    }

    /**
     * Hilsmethode, um den zu einem User gehörigen Player zu bekommmen
     *
     * @param user Der User
     * @return Player
     * @author Ferit
     * @since Sprint 7
     */
    private Player helpMethodToGetThePlayerFromUser(User user) {
        return players.stream().filter(p -> user.equals(p.getTheUserInThePlayer())).findFirst().orElse(null);
    }

    /**
     * Hilfsmethode, um die zu einer ID gehörige Karte zu bekommen
     *
     * @param id ID der Karte
     * @return die Karte, wenn sie gefunden wurde, sonst null
     * @author Ferit
     * @since Sprint 7
     */
    private Card helpMethod2GetTheCard(Short id) {
        return playground.getCardsPackField().getCards().getAllCards().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public boolean isRemoveCardAfter() {
        return removeCardAfter;
    }
}