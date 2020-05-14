package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.ArrayList;
import java.util.UUID;

public class DrawHandMessage extends AbstractServerMessage {
    private ArrayList<Short> cardsOnHand;
    private UUID theLobbyID;
    private int availableActions;
    private int availableBuys;
    private int additionalMoney;

    /**
     * Konstruktor, welcher die ArrayList mit den IDs der Hand übergeben bekommt. Entweder die Standart-Hand mit Größe 5 oder wenn er Karten
     * aktiviert und somit mehr auf der Hand hat.
     *
     * @param theIDsFromTheCards
     * @author Ferit
     * @version 1.0
     * @since Sprint5
     */
    public DrawHandMessage(ArrayList<Short> theIDsFromTheCards, UUID specificLobbyID, int availableActions, int availableBuys, int additionalMoney) {
        this.cardsOnHand = theIDsFromTheCards;
        this.theLobbyID = specificLobbyID;
        this.availableActions = availableActions;
        this.availableBuys = availableBuys;
        this.additionalMoney = additionalMoney;
    }

    // Getter
    public ArrayList<Short> getCardsOnHand() {
        return cardsOnHand;
    }

    public UUID getTheLobbyID() {
        return theLobbyID;
    }

    public int getAdditionalMoney() {
        return additionalMoney;
    }

    public int getAvailableActions() {
        return availableActions;
    }

    public int getAvailableBuys() {
        return availableBuys;
    }
}
