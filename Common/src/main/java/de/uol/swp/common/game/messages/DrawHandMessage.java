package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.ArrayList;

public class DrawHandMessage extends AbstractServerMessage {
    // Die ArrayList enthält die IDs welche der Spieler auf seiner aktuellen Hand hat, wenn er als nächstes dran ist.
    private static ArrayList<Short> cardsOnHand;

    // Standart Konstruktor, welcher eine ArrayList mit den IDs übergeben bekommt.
    public DrawHandMessage(ArrayList<Short> theIDsFromTheCards) {
        cardsOnHand = theIDsFromTheCards;
    }

    // Getter
    public static ArrayList<Short> getCardsOnHand() {
        return cardsOnHand;
    }
}
