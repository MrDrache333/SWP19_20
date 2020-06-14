package de.uol.swp.server.game.player.bot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.card.parser.components.CardAction.request.ChooseCardRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.request.OptionalActionRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.game.request.PlayCardRequest;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.lobby.response.SetChosenCardsResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BotPlayer extends Player {
    protected EventBus eventBus;

    private UUID gameId;
    final private boolean isBot = true;

    static final Logger LOG = LogManager.getLogger(BotPlayer.class);

    private ArrayList<Short> cardsOnHandIDs;
    private ArrayList<Short> cardsInPossessionIDs = new ArrayList<>();
    private int moneyOnTheHand;
    private int round = 0;
    private Map<Short, Integer> cardField;
    private final CardPack cardpack;

    /**
     * Erstellt einen neuen Bot.
     *
     * @param playerName Der Spielername
     * @author Fenja, Ferit
     * @since Sprint7
     */

    public BotPlayer(String playerName, EventBus bus, UUID gameID) {
        super(playerName);
        this.eventBus = bus;
        eventBus.register(this);
        this.gameId = gameID;
        User botFakeUser = new UserDTO(playerName, gameID.toString(), "", true);
        this.setTheUserInThePlayer(botFakeUser);
        this.cardpack = new JsonCardParser().loadPack("Basispack");
    }

    public boolean isBot() {
        return isBot;
    }

    /**
     * Skippt die BuyPhase, wenn der Bot dran ist.
     *
     * @param msg BuyPhase Mitteilung
     * @author Darian
     */
    @Subscribe
    public void onStartBuyPhaseMessage(StartBuyPhaseMessage msg) {
        if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            short buyCardID = 1;
            int actionCardCounter = 0;
            for (short cardID : cardsInPossessionIDs) {
                if (cardID > 6) {
                    actionCardCounter++;
                }
            }
            float actionCardPercentage = actionCardCounter / cardsInPossessionIDs.size();
            if (moneyOnTheHand >= 8)
                buyCardID = 6;
            else if (actionCardPercentage < 20 && moneyOnTheHand != 0) {
                short highestCost = 0;
                ArrayList<Short> cardsToBuyIDs = new ArrayList<>();
                for (short cardID : cardField.keySet()) {
                    if (cardID > 6 && cardID != 38 && getCardCosts(cardID) <= moneyOnTheHand) {
                        if (highestCost < getCardCosts(cardID)) {
                            highestCost = getCardCosts(cardID);
                            cardsToBuyIDs.clear();
                            cardsToBuyIDs.add(cardID);
                        } else if (highestCost == getCardCosts(cardID)) {
                            cardsToBuyIDs.add(cardID);
                        }
                    }
                }
                int lowestNumberOfActionCard = 10;
                for (short cardID : cardsToBuyIDs) {
                    if (lowestNumberOfActionCard > Collections.frequency(cardsInPossessionIDs, cardID)) {
                        lowestNumberOfActionCard = Collections.frequency(cardsInPossessionIDs, cardID);
                        buyCardID = cardID;
                    }
                }
                if (cardField.get(buyCardID) != 0) {

                }
                moneyOnTheHand = moneyOnTheHand - getCardCosts(buyCardID);
            } else if (moneyOnTheHand >= 6 && cardField.get(3) != 0) {
                buyCardID = 3;
                moneyOnTheHand = moneyOnTheHand - 6;
            }
            else if (moneyOnTheHand >= 3 && cardField.get(2) != 0){
                buyCardID = 2;
                moneyOnTheHand = moneyOnTheHand - 3;
            }
            BuyCardRequest req = new BuyCardRequest(gameId, this.getTheUserInThePlayer(), buyCardID);
            LOG.debug("Der Bot " + getTheUserInThePlayer().getUsername() + " will die Karte "+ getCardName(buyCardID) +" kaufen.");
            eventBus.post(req);
        }
    }

    /**
     * Skippt die ActionPhase, wenn der Bot dran ist.
     *
     * @param msg Actionphase Mitteilung
     * @author Darian
     */
    @Subscribe
    public void onStartActionPhaseMessage(StartActionPhaseMessage msg) {
        if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            PlayCardRequest req = null;
            for (short cardID : cardsOnHandIDs) {
                if (cardID > 6 && cardID != 38) {
                    req = new PlayCardRequest(gameId, this.getTheUserInThePlayer(), cardID);
                    cardsOnHandIDs.remove((Short) cardID);
                    LOG.debug("Der Bot " + getTheUserInThePlayer().getUsername() + " will die Karte "+ getCardName(cardID) +" auspielen.");
                    break;
                }
            }
            //PlayCardMessage req = new PlayCardRequest(this.getTheUserInThePlayer(), gameId, playCardID);
            if (req != null) {
                eventBus.post(req);
            }
            else{
                eventBus.post(new SkipPhaseRequest(this.getTheUserInThePlayer(),gameId));
            }
        }
    }

    @Subscribe
    public void onCardsDeckSizeMessage(CardsDeckSizeMessage msg) {
        //Wird wahrscheinlich nicht benötigt
    }

    @Subscribe
    public void onChooseNextActionMessage(ChooseNextActionMessage msg) {
//nicht abgefangen
    }

    @Subscribe
    public void onDiscardCardMessage(DiscardCardMessage msg) {
//nicht initialisiert noch drin lassen
    }

    @Subscribe
    public void onBuyCardMessage(BuyCardMessage msg) {
        if (msg.getCurrentUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            cardsInPossessionIDs.add(msg.getCardID());
            LOG.debug("Der Bot " + getTheUserInThePlayer().getUsername() + " hat die Karte " + getCardName(msg.getCardID()) + " bekommen.");
        }
    }

    @Subscribe
    public void onDiscardPileLastCardMessage(DiscardPileLastCardMessage msg) {

    }

    /**
     * Übernimmt die Karten die auf der Hand sind
     *
     * @param msg DrawHandMessage
     * @author Darian
     */
    @Subscribe
    public void onDrawHandMessage(DrawHandMessage msg) {
        if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getTheLobbyID().equals(gameId)) {
            round++;
            cardsOnHandIDs = msg.getCardsOnHand();
            if (round <= 2) {
                cardsInPossessionIDs.addAll(cardsOnHandIDs);
            }
            moneyOnTheHand = 0;
            for (short cardID : cardsOnHandIDs) {
                switch (cardID) {
                    case 1:
                        moneyOnTheHand += 1;
                        break;
                    case 2:
                        moneyOnTheHand += 2;
                        break;
                    case 3:
                        moneyOnTheHand += 3;
                        break;
                }
            }
        }
    }

    /**
     * Wenn es einen Fehler gibt wird die Phase geskipt
     *
     * @param msg Fehlernachricht
     * @author Darian
     */
    @Subscribe
    public void onGameExceptionMessage(GameExceptionMessage msg) {
        if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            LOG.debug(msg.getMessage());
            SkipPhaseRequest req = new SkipPhaseRequest(this.getTheUserInThePlayer(), gameId);
            eventBus.post(req);
        }
    }

    @Subscribe
    public void onGameOverMessage(GameOverMessage msg) {

    }

    @Subscribe
    public void onInfoPlayDisplayMessage(InfoPlayDisplayMessage msg) {

    }

    @Subscribe
    public void onMoveCardMessage(MoveCardMessage msg) {

    }

    @Subscribe
    public void onPlayCardMessage(PlayCardMessage msg) {
        if(msg.getCurrentUser().equals(getTheUserInThePlayer()) && msg.getGameID().equals(gameId) && msg.getIsPlayed()){
            LOG.debug("Der Bot " + getTheUserInThePlayer().getUsername() + " hat die Karte " + getCardName(msg.getHandCardID()) + " ausgespielt.");
            //cardpack.getCards().getCardForId(msg.getHandCardID()).
        }
    }

    @Subscribe
    public void onSendCardFieldMessage(SendCardFieldMessage msg) {
        if (msg.getGameID().equals(gameId)) {
            cardField = msg.getCardField();
        }
    }

    @Subscribe
    public void onShowCardMessage(ShowCardMessage msg) {
//nicht abgefangen
    }

    @Subscribe
    public void onStartClearPhaseMessage(StartClearPhaseMessage msg) {

    }

    @Subscribe
    public void onUpdateCardCounterMessage(UpdateCardCounterMessage msg) {
//nicht abgefangen wird bearbeitet
    }

    @Subscribe
    public void onUserGaveUpMessage(UserGaveUpMessage msg) {

    }

    /**
     * Der Bot anwortet immer mit Ja bei einer OptionalActionRequest.
     *
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onOptionalActionRequest(OptionalActionRequest msg) {
        if (msg.getPlayer().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            OptionalActionResponse res = new OptionalActionResponse(gameId, this.getTheUserInThePlayer(), true);
            eventBus.post(res);
        }
    }

    /**
     * Der Bot anwortet immer mit Ja bei einer OptionalActionRequest.
     *
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onChooseCardRequest(ChooseCardRequest msg) {
        if (msg.getPlayer().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            msg.getCards();
            short choosenCard = 1;
            ArrayList<Short> choosenCards = new ArrayList<>();
            ChooseCardResponse res = new ChooseCardResponse(gameId, this.getTheUserInThePlayer(), choosenCards, msg.getDirectHand());
            eventBus.post(res);
        }
    }

    /**
     * Es wird der gesetzte Eventbus gelöscht
     *
     * @author Marco
     * @since Start
     */
    public void clearEventBus() {
        this.eventBus.unregister(this);
        this.eventBus = null;
    }

    private short getCardCosts(short cardID) {
        return cardpack.getCards().getCardForId(cardID).getCosts();
    }
    private String getCardName(short cardID) {
        return cardpack.getCards().getCardForId(cardID).getName();
    }
}
