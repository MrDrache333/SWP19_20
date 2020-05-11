package de.uol.swp.common.lobby.request;

import java.util.ArrayList;
import java.util.UUID;

public class SendChosenCardsRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = -7885275563836769973L;
    private ArrayList<Short> chosencards;

    public SendChosenCardsRequest(UUID lobbyID, ArrayList<Short> chosencards) {
        super(lobbyID);
        this.chosencards = chosencards;
    }

    public ArrayList<Short> getChosenCards() {
        return chosencards;
    }
}
