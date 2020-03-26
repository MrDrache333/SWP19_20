package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Erstellt UserGivedUpMessage, welche vom Server an den Client verschickt wird um zu Signalisieren das der User erfolgreich im Spiel aufgegeben hat.
 *
 * @version 1
 * @uthor Haschem, Ferit
 * @since Sprint6
 */


public class UserGaveUpMessage extends AbstractServerMessage {
    private static final long serialVersionUID = -6145124538556130864L;
    private UUID lobbyID;
    private UserDTO theUser;
    private Boolean userGivedUp;

    public UserGaveUpMessage(UUID lobbyID, UserDTO theUser, Boolean userGivedUp) {
        this.lobbyID = lobbyID;
        this.theUser = theUser;
        this.userGivedUp = userGivedUp;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public UserDTO getTheUser() {
        return theUser;
    }

    public Boolean getUserGivedUp() {
        return userGivedUp;
    }
}
