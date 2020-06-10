package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.ArrayList;
import java.util.UUID;

public class DrawHandMessage extends AbstractServerMessage {
    private final ArrayList<Short> cardsOnHand;
    private final UUID theLobbyID;
    private final Short numberOfPlayers;
    private final boolean initialHand;

    /**
     * Konstruktor, welcher die ArrayList mit den IDs der Hand übergeben bekommt. Entweder die Standart-Hand mit Größe 5 oder wenn er Karten
     * aktiviert und somit mehr auf der Hand hat.
     *
     * @param theIDsFromTheCards die KartenIDs
     * @author Ferit
     * @since Sprint 5
     */
    public DrawHandMessage(ArrayList<Short> theIDsFromTheCards, UUID specificLobbyID, Short numberOfPlayers, boolean initialHand) {
        this.cardsOnHand = theIDsFromTheCards;
        this.theLobbyID = specificLobbyID;
        this.numberOfPlayers = numberOfPlayers;
        this.initialHand = initialHand;
    }

    /**
     * Gibt die Karten auf der Hand zurück
     *
     * @return cardsOnHand die Karten auf der Hand
     * @author Ferit
     * @since Sprint 5
     */
    public ArrayList<Short> getCardsOnHand() {
        return cardsOnHand;
    }

    /**
     * Gibt die LobbyID zurück
     *
     * @return theLobbyID die LobbyID
     * @author Ferit
     * @since Sprint 5
     */
    public UUID getTheLobbyID() {
        return theLobbyID;
    }

    public Short getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Gibt die Initialhand zurück
     *
     * @return initialHand die Initialhand
     * @author Rike
     * @since Sprint 8
     */
    public boolean isInitialHand() {
        return initialHand;
    }
}
