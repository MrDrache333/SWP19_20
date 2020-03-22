package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Erstellt UserGivedUpMessage
 *
 * @uthor Haschem, Ferit
 * @version 1
 * @since Sprint6
 */


public class UserGivedUpMessage extends AbstractServerMessage {
    private static final long serialVersionUID = -6145124538556130864L;
    private UUID lobbyID;
    private UserDTO theUser;
    private Boolean userGivedUp;

    public UserGivedUpMessage(UUID lobbyID, UserDTO theUser, Boolean userGivedUp) {
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
