package de.uol.swp.common.game.request;

import de.uol.swp.common.lobby.request.AbstractLobbyRequest;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Erstellt GameGiveUpRequest, welche eine Anfrage vom Client an den Server schickt das ein bestimmter Spieler in dem spezifischen Game aufgeben möchte.
 *
 * @version 1
 * @uthor Haschem, Ferit
 * @since Sprint6
 */

public class GameGiveUpRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = 7986167787009372236L;
    private UserDTO givingUpUSer;
    private UUID theSpecificLobbyID;

    private Boolean wantsToGiveUP;

    public GameGiveUpRequest(UserDTO givingUpUSer, UUID theSpecificLobbyID) {
        this.givingUpUSer = givingUpUSer;
        this.theSpecificLobbyID = theSpecificLobbyID;
        this.wantsToGiveUP = true;
    }

    public UserDTO getGivingUpUSer() {
        return givingUpUSer;
    }

    public UUID getTheSpecificLobbyID() {
        return theSpecificLobbyID;
    }

    public Boolean getWantsToGiveUP() {
        return wantsToGiveUP;
    }
}
