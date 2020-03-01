package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.ArrayList;
import java.util.UUID;

public class DrawHandMessage extends AbstractServerMessage {
    private static ArrayList<Short> cardsOnHand;
    private static UUID theLobbyID;

    /**
     * Konstruktor, welcher die ArrayList mit den IDs der Hand übergeben bekommt. Entweder die Standart-Hand mit Größe 5 oder wenn er Karten
     * aktiviert und somit mehr auf der Hand hat.
     *
     * @param theIDsFromTheCards
     * @author Ferit
     * @version 1.0
     * @since Sprint5
     */
    public DrawHandMessage(ArrayList<Short> theIDsFromTheCards, UUID specificLobbyID) {
        this.cardsOnHand = theIDsFromTheCards;
        this.theLobbyID = specificLobbyID;
    }

    // Getter
    public static ArrayList<Short> getCardsOnHand() {
        return cardsOnHand;
    }

    public static UUID getTheLobbyID() {
        return theLobbyID;
    }
}
