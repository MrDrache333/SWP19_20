package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.ArrayList;
import java.util.UUID;

public class DrawHandMessage extends AbstractServerMessage {
    private ArrayList<Short> cardsOnHand;
    private UUID theLobbyID;
    private int valueOfHand;

    /**
     * Konstruktor, welcher die ArrayList mit den IDs der Hand übergeben bekommt. Entweder die Standart-Hand mit Größe 5 oder wenn er Karten
     * aktiviert und somit mehr auf der Hand hat.
     *
     * @param theIDsFromTheCards
     * @author Ferit, Rike
     * @version 1.0
     * @since Sprint5
     */
    public DrawHandMessage(ArrayList<Short> theIDsFromTheCards, UUID specificLobbyID, int valueOfHand) {
        this.cardsOnHand = theIDsFromTheCards;
        this.theLobbyID = specificLobbyID;
        this.valueOfHand = valueOfHand;
    }

    // Getter
    public ArrayList<Short> getCardsOnHand() {
        return cardsOnHand;
    }

    public UUID getTheLobbyID() {
        return theLobbyID;
    }

    public int getValueOfHand() {
        return valueOfHand;
    }
}
