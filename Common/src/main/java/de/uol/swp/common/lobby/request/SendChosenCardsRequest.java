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
    private final ArrayList<Short> chosencards;

    public SendChosenCardsRequest(UUID lobbyID, ArrayList<Short> chosencards) {
        super(lobbyID);
        this.chosencards = chosencards;
    }

    public ArrayList<Short> getChosenCards() {
        return chosencards;
    }
}
