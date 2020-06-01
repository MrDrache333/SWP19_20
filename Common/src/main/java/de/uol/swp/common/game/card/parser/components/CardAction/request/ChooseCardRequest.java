package de.uol.swp.common.game.card.parser.components.CardAction.request;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;
import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Request, welcher einen Spieler um eine Auswahl an Karten bittet.
 */
public class ChooseCardRequest extends AbstractGameMessage {

    private static final long serialVersionUID = 4914619905908136079L;

    /**
     * Die Standardnachricht.
     */
    private static final String DEFAULT_MESSAGE = "Wähle aus folgenden Karten aus";

    /**
     * Die Karten-ID's, die zur Auswahl stehen.
     */
    private ArrayList<Short> cards;
    /**
     * Die Kartenquelle
     */
    private AbstractPlayground.ZoneType source;
    /**
     * Die anzuzeigende Nachricht
     */
    private String message;
    /**
     * Die Anzahl an wählbaren Karten
     */
    private Value countV;

    private int count;
    private boolean directHand;

    /**
     * Erstellt einen neuen Request
     *
     * @param id           Die ID des Spiels
     * @param player       Der Spieler, der die Auswahl treffen soll
     * @param cards        Die Karten, die zur Auswahl stehen
     * @param countV       Die Anzahl an wählbaren Karten
     * @param sourcePlayer Der Spieler, von dem die Karten ausgewählt werden
     * @param source       Die Zone, von welcher die Karten stammen
     * @param message      Die Nachricht, die dem Spieler gezeigt werden soll
     * @author KenoO
     * @since Sprint 7
     */
    public ChooseCardRequest(UUID id, User player, ArrayList<Short> cards, Value countV, AbstractPlayground.ZoneType source, String message, boolean directHand) {
        super(id, player);
        this.cards = cards;
        this.countV = countV;
        this.source = source;
        if (message.equals("")) message = DEFAULT_MESSAGE;
        this.message = message;
        this.directHand = directHand;
    }

    /**
     * Erstellt einen neuen Request
     *
     * @param id           Die ID des Spiels
     * @param player       Der Spieler, der die Auswahl treffen soll
     * @param cards        Die Karten, die zur Auswahl stehen
     * @param sourcePlayer Der Spieler, von dem die Karten ausgewählt werden
     * @param source       Die Zone, von welcher die Karten stammen
     * @param message      Die Nachricht, die dem Spieler gezeigt werden soll
     * @author KenoO
     * @since Sprint 7
     */
    public ChooseCardRequest(UUID id, User player, ArrayList<Short> cards, int count, User sourcePlayer, AbstractPlayground.ZoneType source, String message, boolean directHand) {
        super(id, player);
        this.cards = cards;
        this.count = count;
        this.source = source;
        if (message.equals("")) message = DEFAULT_MESSAGE;
        this.message = message;
        this.directHand = directHand;
    }

    //***********************
    //*  GETTER UND SETTER  *
    //***********************

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

    /**
     * Gets source.
     *
     * @return the source
     * @author KenoO
     * @since Sprint 7
     */
    public AbstractPlayground.ZoneType getSource() {
        return source;
    }

    /**
     * Gets message.
     *
     * @return the message
     * @author KenoO
     * @since Sprint 7
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets count.
     *
     * @return the count
     * @author KenoO
     * @since Sprint 7
     */
    public int getCount() {
        return count;
    }

    public boolean getDirectHand() {
        return directHand;
    }

    public Value getCountV() {
        return countV;
    }
}

}