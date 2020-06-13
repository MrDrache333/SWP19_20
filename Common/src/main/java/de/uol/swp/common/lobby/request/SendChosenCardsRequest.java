package de.uol.swp.common.lobby.request;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Request, in welchem die vom Owner ausgewählten Aktionskarten geschickt werden.
 *
 * @author Fenja, Anna
 * @since Sprint 7
 */
public class SendChosenCardsRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = -7885275563836769973L;
    private final ArrayList<Short> chosenCards;

    public SendChosenCardsRequest(UUID lobbyID, ArrayList<Short> chosenCards) {
        super(lobbyID);
        this.chosenCards = chosenCards;
    }

    public ArrayList<Short> getChosenCards() {
        return chosenCards;
    }
}
