package de.uol.swp.server.game.player.bot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@SuppressWarnings("UnstableApiUsage, unused")
public class BotPlayer extends Player {
    protected EventBus eventBus;

    private final UUID gameID;

    static final Logger LOG = LogManager.getLogger(BotPlayer.class);

    private ArrayList<Short> cardsOnHandIDs;
    private ArrayList<Short> cardsInPossessionIDs = new ArrayList<>();
    private int moneyOnTheHand;
    private int round = 0;
    private Map<Short, Integer> cardField;
    private final CardPack cardpack;
    private int takeCardWithSpecificValue = 0;
    private boolean isBot = true;
    private boolean buyOnlyMoneyCard =false;
    private int actionCount=0;
    private int buyCount=0;

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
        this.gameID = gameID;
        User botFakeUser = new UserDTO(playerName, gameID.toString(), "", true);
        this.setTheUserInThePlayer(botFakeUser);
        this.cardpack = new JsonCardParser().loadPack("Basispack");
    }

    /**
     * Wenn die BuyPhase gestartet wird, kauft der Bot eine Karte.
     *
     * @param msg BuyPhase Mitteilung
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onStartBuyPhaseMessage(StartBuyPhaseMessage msg) {
        LOG.debug("1");
        Thread t = new Thread(() -> {
            LOG.debug("2");
            if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameID)) {
                LOG.debug("3");
                try {
                    buyCard();
                } catch (InterruptedException e) {
                    LOG.error(e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();
    }

    /**
     * Der Bot spielt die teuerste Aktionskarte in seiner Hand aus.
     *
     * @param msg Actionphase Mitteilung
     * @author Darian
     */
    @Subscribe
    public void onStartActionPhaseMessage(StartActionPhaseMessage msg){
        new Thread(() -> {
            if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameID)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error(e);
                    Thread.currentThread().interrupt();
                }
                actionCount++;
                while (actionCount > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        LOG.error(e);
                        Thread.currentThread().interrupt();
                    }
                    short maxCardValue = 0;
                    short playCardID = 0;
                    for (short cardID : cardsOnHandIDs) {
                        if (cardID > 6 && cardID != 38 && getCardCosts(cardID) > maxCardValue) {
                            maxCardValue = getCardCosts(cardID);
                            playCardID = cardID;
                        }
                    }
                    if (playCardID != 0) {
                        cardsOnHandIDs.remove((Short)playCardID);
                        PlayCardRequest req = new PlayCardRequest(gameID, this.getTheUserInThePlayer(), playCardID);
                        String logMsg = "Der Bot " + getTheUserInThePlayer().getUsername() + " will die Karte " + getCardName(playCardID) + " auspielen.";
                        LOG.debug(logMsg);
                        eventBus.post(req);
                        if(playCardID == 8 || playCardID == 27){
                            actionCount += 2;
                        } else if(playCardID == 11 || playCardID == 23|| playCardID == 29 || playCardID == 10){
                            actionCount += 1;
                        }
                    } else {
                        eventBus.post(new SkipPhaseRequest(this.getTheUserInThePlayer(), gameID));
                    }
                    actionCount--;
                }
            }
        }).start();
    }

    /**
     * Der Bot fügt sobald er eine Karte gekauft hat diese zu seinem Array hinzu, von den Karten die er besitzt.
     *
     * @param msg BuyCardMessage
     * @author Darian
     */
    @Subscribe
    public void onBuyCardMessage(BuyCardMessage msg) {
        new Thread(() -> {
            if (msg.getCurrentUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameID)) {
                cardsInPossessionIDs.add(msg.getCardID());
                String logMsg ="Der Bot " + getTheUserInThePlayer().getUsername() + " hat die Karte " + getCardName(msg.getCardID()) + " bekommen.";
                LOG.debug(logMsg);
            }
        }).start();
    }

    /**
     * Der Bot übernimmt die Karten die auf der Hand sind und errechnet sich das Geld dass er auf der Hand hat
     *
     * @param msg DrawHandMessage
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onDrawHandMessage(DrawHandMessage msg) {
        new Thread(() -> {
            if (msg.getPlayer().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getTheLobbyID().equals(gameID)) {
                round++;
                cardsOnHandIDs = msg.getCardsOnHand();
                if (round <= 2) {
                    cardsInPossessionIDs.addAll(cardsOnHandIDs);
                }
            }
        }).start();
    }

    /**
     * Wenn es einen Fehler gibt wird die Phase geskipt.
     *
     * @param msg Fehlernachricht
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onGameExceptionMessage(GameExceptionMessage msg) {
        new Thread(() -> {
            if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameID)) {
                LOG.debug(msg.getMessage());
                SkipPhaseRequest req = new SkipPhaseRequest(this.getTheUserInThePlayer(), gameID);
                eventBus.post(req);
            }
        }).start();
    }

    /**
     * Der Bot bekommt mit ob eine Karte auch ausgespielt wird und verarbeitet es, soweit der Bot es wissen muss.
     *
     * @param msg PlayCardMessage
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onPlayCardMessage(PlayCardMessage msg)  {
        new Thread(() -> {
            if (msg.getCurrentUser().equals(getTheUserInThePlayer()) && msg.getGameID().equals(gameID) && msg.getIsPlayed()) {
                if (!msg.getIsPlayed()) {
                    String logMsg ="Der Bot " + getTheUserInThePlayer().getUsername() + " konnte die Karte " + getCardName(msg.getHandCardID()) + " nicht ausgespielt.";
                    LOG.debug(logMsg);
                    return;
                }
                String logMsg = "Der Bot " + getTheUserInThePlayer().getUsername() + " hat die Karte " + getCardName(msg.getHandCardID()) + " ausgespielt.";
                LOG.debug(logMsg);
                switch (msg.getHandCardID()) {
                    case 9:
                        moneyOnTheHand += 2;
                        buyCount++;
                        break;
                    case 21:
                    case 12:
                        moneyOnTheHand += 2;
                        break;
                    case 11:
                        moneyOnTheHand += 1;
                        buyCount++;
                        break;
                    case 16:
                        takeCardWithSpecificValue = 4;
                        try {
                            buyCard();
                        } catch (InterruptedException e) {
                            LOG.error(e);
                            Thread.currentThread().interrupt();
                        }
                        break;
                    case 19:
                        cardsInPossessionIDs.remove(msg.getHandCardID());
                        break;
                    case 20:
                        cardsInPossessionIDs.remove((Short) (short) 1);
                        cardsOnHandIDs.remove((Short) (short) 1);
                        moneyOnTheHand += 2;
                        break;
                    case 27:
                        moneyOnTheHand += 2;
                        buyCount++;
                        break;
                    case 28:
                        buyCount++;
                        break;
                    default:
                }
            }
        }).start();
    }

    /**
     * Der Bot übernimmt das Kartenfeld
     *
     * @param msg PlayCardMessage
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onSendCardFieldMessage(SendCardFieldMessage msg) {
        new Thread(()->{
            if (msg.getGameID().equals(gameID)) {
                cardField = msg.getCardField();
            }
        }).start();
    }

    /**
     * Der Bot anwortet immer mit "Ja" bei einer OptionalActionRequest.
     *
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onOptionalActionRequest(OptionalActionRequest msg)  {
        new Thread(()-> {
            if (msg.getPlayer().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameID)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error(e);
                    Thread.currentThread().interrupt();
                }
                OptionalActionResponse res = new OptionalActionResponse(gameID, this.getTheUserInThePlayer(), true, msg.getActionExecutionID());
                eventBus.post(res);
            }
        }).start();
    }

    /**
     * Wenn die ChooseCardRequest kommt wird eine oder mehrere Karten ausgwählt. Bei:
     * Keller: Es werden alle Kupfer- und alle Punktekarten abgelegt
     * Mine: Es wird die Geldkarte mit dem niedrigsten Wert abgelegt
     * Umbau: Es wird die Karte mit dem niedrigsten Wert entsorgt
     * Kapelle: Es werden alle Kupferkarten und Fluchkarten entsorgt.
     *
     *
     * @author Darian
     * @since Sprint 9
     */
    @Subscribe
    public void onChooseCardRequest(ChooseCardRequest msg)  {
        new Thread(()-> {
            if (msg.getPlayer().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameID)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error(e);
                    Thread.currentThread().interrupt();
                }
                ArrayList<Short> choosenCards = new ArrayList<>();
                short choosenCard = 0;
                if (msg.getActionExecutionID() <= 6 || msg.getActionExecutionID() == 38) {
                    LOG.debug("Es wurde eine Karte ausgespielt die keine Aktions Karte ist.");
                } else {
                    switch (msg.getActionExecutionID()) {
                        case 10:
                            for (short cardID : msg.getCards()) {
                                if ((cardID == 0 || (cardID >= 4 && cardID <= 6))) {
                                    choosenCards.add(cardID);
                                }
                            }
                            break;
                        case 13:
                            short lowestMoneyValueCardID = 0;
                            for (short cardID : msg.getCards()) {
                                if (lowestMoneyValueCardID == 0 && (cardID >= 1 && cardID <= 3)) {
                                    lowestMoneyValueCardID = cardID;
                                    buyOnlyMoneyCard = true;
                                } else if (getCardCosts(cardID) < getCardCosts(lowestMoneyValueCardID) && (cardID >= 1 && cardID <= 3)) {
                                    lowestMoneyValueCardID = cardID;
                                }
                            }
                            if (lowestMoneyValueCardID == 0) {
                                eventBus.post(new SkipPhaseRequest(this.getTheUserInThePlayer(), gameID));
                                return;
                            }
                            choosenCard = lowestMoneyValueCardID;
                            takeCardWithSpecificValue = getCardCosts(lowestMoneyValueCardID) + 3;
                            cardsInPossessionIDs.remove((Short) lowestMoneyValueCardID);
                            cardsOnHandIDs.remove((Short) lowestMoneyValueCardID);
                            break;
                        case 15:
                            short lowestValueCardID = msg.getCards().get(0);
                            for (short cardID : msg.getCards()) {
                                if (getCardCosts(cardID) < getCardCosts(lowestValueCardID)) {
                                    lowestValueCardID = cardID;
                                }
                            }
                            choosenCard = lowestValueCardID;
                            takeCardWithSpecificValue = getCardCosts(lowestValueCardID) + 2;
                            cardsInPossessionIDs.remove((Short) lowestValueCardID);
                            cardsOnHandIDs.remove((Short) lowestValueCardID);
                            break;
                        case 22:
                            for (Short cardID : msg.getCards()) {
                                if ((cardID == 0 || cardID == 38)) {
                                    choosenCards.add(cardID);
                                    cardsInPossessionIDs.remove(cardID);
                                    cardsOnHandIDs.remove(cardID);
                                }
                            }
                            break;
                        default:
                    }
                }
                ChooseCardResponse res;
                if (choosenCard == 0) {
                    res = new ChooseCardResponse(gameID, this.getTheUserInThePlayer(), choosenCards, msg.getActionExecutionID());
                } else {
                    res = new ChooseCardResponse(gameID, this.getTheUserInThePlayer(), choosenCard, msg.getActionExecutionID());
                }
                eventBus.post(res);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    LOG.error(e);
                    Thread.currentThread().interrupt();
                }
                if (takeCardWithSpecificValue != 0) {
                    try {
                        buyCard();
                    } catch (InterruptedException e) {
                        LOG.error(e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
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

    /**
     * Anhand der Karten-ID wirden die Kostern der Karte ermittelt
     *
     * @author Darian
     * @param cardID    Die Karten-ID
     * @since Sprint10
     * @return costs  Kosten der Karte
     */
    private short getCardCosts(short cardID) {
        return cardpack.getCards().getCardForId(cardID).getCosts();
    }

    /**
     * Anhand der Karten-ID wird der Kartenname herausgefunden
     *
     * @author Darian
     * @param cardID    Die Karten-ID
     * @since Sprint10
     * @return cardName  Der Kartenname
     */
    private String getCardName(short cardID) {
        return cardpack.getCards().getCardForId(cardID).getName();
    }

    /**
     * Wenn etwas mit dem Geld auf der Hand gekauft wurde wird das Geld auf der Hand aktualisiert.
     *
     * @author Darian
     * @param cardID Karten-ID von der gekauften Karte
     * @since Sprint10
     */
    private void calculateNewMoneyOnTheHand(short cardID) {
        if(takeCardWithSpecificValue == 0) {
            moneyOnTheHand = moneyOnTheHand - getCardCosts(cardID);
        }
    }

    /**
     * Berechnet das Geld auf der Hand
     *
     * @author Darian
     * @since Sprint10
     */
    private void calculateMoneyOnTheHand() {
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
                default:
            }
        }
    }

    /**
     * Der Bot entscheidet, welche Karte er kaufen möchte. Wenn der Bot genug Geld hat kauft er eine Provinz.
     * Bei unter 20% von Aktionskarten wird eine neue Aktionskarte gekauft. Dabei wird geguckt, dass er die teuereste
     * möglichste Karte kauft. Ansonsten wird geguckt wird geguckt ob der Bot genug Geld hat um Silber oder Gold zu
     * kaufen.
     *
     * @author Darian
     * @since Sprint10
     */
    private void buyCard() throws InterruptedException {
        Thread.sleep(1000);
        calculateMoneyOnTheHand();
        short buyCardID = 1;
        int actionCardCounter = 0;
        int usingMoney = 0;
        LOG.debug(buyCount);
        buyCount++;
        if (takeCardWithSpecificValue != 0) {
            usingMoney = takeCardWithSpecificValue;
        } else {
            usingMoney = moneyOnTheHand;
        }
        while(buyCount != 0) {
            for (short cardID : cardsInPossessionIDs) {
                if (cardID > 6) {
                    actionCardCounter++;
                }
            }
            float actionCardPercentage = (float) actionCardCounter / (float) cardsInPossessionIDs.size();
            String logMsg = "Der Bot hat " + (actionCardPercentage * 100) + "% an Aktionskarten und " + usingMoney + " Geld.";
            LOG.debug(logMsg);
            if (usingMoney >= 8 && !buyOnlyMoneyCard) {
                buyCardID = 6;
            } else if (usingMoney >= 5 && round >= 25 && !buyOnlyMoneyCard) {
                buyCardID = 5;
            } else if (actionCardPercentage < 0.2 && usingMoney != 0 && !buyOnlyMoneyCard) {
                short highestCost = 0;
                ArrayList<Short> cardsToBuyIDs = new ArrayList<>();
                for (short cardID : cardField.keySet()) {
                    if (cardID > 6 && cardID != 38 && implementedCardID(cardID) && getCardCosts(cardID) <= usingMoney) {
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
                if (cardField.get(buyCardID) == 0) {
                    buyCardID = 0;
                }
            } else if (usingMoney >= 6 && cardField.get((short) 3) != 0) {
                buyCardID = 3;
            } else if (usingMoney >= 3 && cardField.get((short) 2) != 0) {
                buyCardID = 2;
            } else if (usingMoney == 0) {
                buyCardID = 1;
            }
            logMsg = "Der Bot " + getTheUserInThePlayer().getUsername() + " will die Karte " + getCardName(buyCardID) + " kaufen.";
            LOG.debug(logMsg);
            BuyCardRequest req = new BuyCardRequest(gameID, this.getTheUserInThePlayer(), buyCardID);
            eventBus.post(req);
            buyCount--;
            Thread.sleep(1000);
            if (takeCardWithSpecificValue == 0) {
                calculateNewMoneyOnTheHand(buyCardID);
            }
            else{
                break;
            }
        }
        buyOnlyMoneyCard = false;
        takeCardWithSpecificValue = 0;
        Thread.sleep(500);
    }

    /**
     * Die Karten auf der Hand werden zurückgegeben.
     *
     * @author Darian
     * @since Sprint10
     * @return Die Karten auf der Hand
     */
    public List<Short> getCardsOnHandIDs(){
        return cardsOnHandIDs;
    }

    /**
     * Das Geld auf der Hand wird zurückgegeben.
     *
     * @author Darian
     * @since Sprint10
     * @return Geld auf der Hand
     */
    public int getMoneyOnTheHand(){
        return moneyOnTheHand;
    }

    /**
     * Der Wert mit der eine kostenlose Karte auf dem Deck geholt werden darf wird wiedergegeben.
     *
     * @author Darian
     * @since Sprint10
     * @return Karte die gespielt wird
     */
    public int getTakeCardWithSpecificValue() {
        return takeCardWithSpecificValue;
    }

    /**
     * Es wird überprüft ob die Karten-ID gültig ist
     *
     * @author Darian
     * @since Sprint10
     * @return ob die Karte implementiert ist
     */
    public boolean implementedCardID(short cardID) {
        if (cardID != 12 || cardID != 17 || cardID != 18 || cardID != 25 || cardID != 26 || (cardID >= 29 && cardID <=37)){
            return false;
        }
        return true;
    }

    /**
     * Die Karten die er besitzt werden zurückgegeben.
     *
     * @author Darian
     * @since Sprint10
     * @return Die Karten die der Bot besitzt (soweit er davon weiß)
     */
    public List<Short> getCardsInPossessionIDs(){
        return cardsInPossessionIDs;
    }
}
