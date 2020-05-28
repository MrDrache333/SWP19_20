package de.uol.swp.common.game.card.parser.components.CardAction.response;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.message.Message;
import de.uol.swp.common.message.MessageContext;
import de.uol.swp.common.message.RequestMessage;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Die Antwortnachricht auf ein ChooseCardRequest
 */
public class ChooseCardResponse implements RequestMessage {

    private static final long serialVersionUID = -3212704229461733936L;

    /**
     * Die ausgewählten Karten
     */
    private ArrayList<Short> cards;

    private AbstractPlayground.ZoneType from;
    private boolean directHand;
    private UUID gameID;
    private User player;

    /**
     * Erstellt eine neue Antwortnachricht
     *
     * @param gameID      Die ID des Games
     * @param player      Der Spieler
     * @param choosenCard Die ausgewählte Karte
     * @author KenoO
     * @since Sprint7
     */
    public ChooseCardResponse(UUID gameID, User player, short choosenCard, boolean directHand) {
        this.gameID = gameID;
        this.player = player;
        cards = new ArrayList<>();
        cards.add(choosenCard);
        this.directHand = directHand;
    }

    /**
     * Erstellt eine neue Antwortnachricht
     *
     * @param gameID       Die ID des Games
     * @param player       Der Spieler
     * @param choosenCards Die ausgewählten Karten
     * @author KenoO
     * @since Sprint7
     */
    public ChooseCardResponse(UUID gameID, User player, ArrayList<Short> choosenCards, boolean directHand) {
        this.gameID = gameID;
        this.player = player;
        cards = choosenCards;
        this.directHand = directHand;
    }

    public ChooseCardResponse(UUID gameID, User loggedInUser, ArrayList<Short> chosenCards) {
        this.gameID = gameID;
        this.player = loggedInUser;
        this.cards = chosenCards;
    }

    /**
     * Gets cards.
     *
     * @return the cards
     * @author KenoO
     * @since Sprint 7
     */
    public ArrayList<Short> getCards() {
        return cards;
    }

    public AbstractPlayground.ZoneType getFrom() {
        return from;
    }

    public boolean getDirectHand() {
        return directHand;
    }

    public void setCards(ArrayList<Short> cards) {
        this.cards = cards;
    }

    public boolean isDirectHand() {
        return directHand;
    }

    public void setDirectHand(boolean directHand) {
        this.directHand = directHand;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    @Override
    public boolean authorizationNeeded() {
        return false;
    }

    @Override
    public Optional<MessageContext> getMessageContext() {
        return Optional.empty();
    }

    @Override
    public void setMessageContext(MessageContext messageContext) {

    }

    @Override
    public Optional<Session> getSession() {
        return Optional.empty();
    }

    @Override
    public void setSession(Session session) {

    }

    @Override
    public void initWithMessage(Message otherMessage) {

    }
}
