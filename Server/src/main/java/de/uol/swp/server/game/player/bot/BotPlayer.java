package de.uol.swp.server.game.player.bot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.request.ChooseCardRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.request.OptionalActionRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.game.request.PlayCardRequest;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class BotPlayer extends Player {
    protected EventBus eventBus;

    private UUID gameId;
    final private boolean isBot = true;

    private ArrayList<Short> cardsOnHandIDs;
    private ArrayList<Short> cardsInPossessionIDs;
    private int moneyOnTheHand;
    private int round = 0;

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
        if(msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            short buyCardID=1;
            int actionCardCounter = 0;
            for(short cardID : cardsInPossessionIDs){
                if(cardID > 6){
                    actionCardCounter++;
                }
            }
            float actionCardPercentage = actionCardCounter/cardsInPossessionIDs.size();
            if(actionCardPercentage < 20){
                //Aktionskarte kaufen
            }
            else if(moneyOnTheHand>=6){
                buyCardID = 3;
            }
            else if(moneyOnTheHand>=3){
                buyCardID = 2;
            }
            BuyCardRequest req = new BuyCardRequest(gameId, this.getTheUserInThePlayer(), buyCardID);
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
        if(msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            PlayCardRequest req = null;
            for(short cardID : cardsOnHandIDs){
                if(cardID > 6 && cardID != 38){
                    req = new PlayCardRequest(gameId, this.getTheUserInThePlayer(), cardID);
                }
            }
            //PlayCardMessage req = new PlayCardRequest(this.getTheUserInThePlayer(), gameId, playCardID);
            if(req != null){
                eventBus.post(req);
            }
        }
    }

    @Subscribe
    public void onCardsDeckSizeMessage(CardsDeckSizeMessage msg) {
        //Wird wahrscheinlich nicht benötigt
    }

    @Subscribe
    public void onChooseNextActionMessage(ChooseNextActionMessage msg) {

    }

    @Subscribe
    public void onDiscardCardMessage(DiscardCardMessage msg) {

    }

    @Subscribe
    public void onBuyCardMessage(BuyCardMessage msg) {
        if (msg.getCurrentUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
            cardsInPossessionIDs.add(msg.getCardID());
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
        if(msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getTheLobbyID().equals(gameId)) {
            round++;
            cardsOnHandIDs = msg.getCardsOnHand();
            if(round <= 2){
                cardsInPossessionIDs.addAll(cardsOnHandIDs);
            }
            moneyOnTheHand = 0;
            for(short cardID : cardsOnHandIDs){
                switch(cardID) {
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
        if(msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername()) && msg.getGameID().equals(gameId)) {
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

    }

    @Subscribe
    public void onSendCardFieldMessage(SendCardFieldMessage msg) {

    }

    @Subscribe
    public void onShowCardMessage(ShowCardMessage msg) {

    }

    @Subscribe
    public void onStartClearPhaseMessage(StartClearPhaseMessage msg) {

    }

    @Subscribe
    public void onUpdateCardCounterMessage(UpdateCardCounterMessage msg) {

    }

    @Subscribe
    public void onUserGaveUpMessage(UserGaveUpMessage msg) {

    }

    @Subscribe
    public void onUserGaveUpMessage(ChooseCardRequest msg) {

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
            OptionalActionResponse res = new OptionalActionResponse(gameId, this.getTheUserInThePlayer(),true);
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
            //ChooseCardResponse res = new ChooseCardResponse();
            //eventBus.post(res);
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
}
