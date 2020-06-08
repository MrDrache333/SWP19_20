package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Erstellt UserGivedUpMessage, welche vom Server an den Client verschickt wird um zu Signalisieren das der User erfolgreich im Spiel aufgegeben hat.
 *
 * @author Haschem, Ferit
 * @since Sprint 6
 */
public class UserGaveUpMessage extends AbstractServerMessage {
    private static final long serialVersionUID = -6145124538556130864L;
    private UUID lobbyID;
    private UserDTO theUser;
    private Boolean userGivedUp;

    /**
     * Der Konstruktor der UserGameUpMessage
     *
     * @param lobbyID     die Lobby-ID
     * @param theUser     der User
     * @param userGivedUp ob der User aufgegeben hat
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public UserGaveUpMessage(UUID lobbyID, UserDTO theUser, Boolean userGivedUp) {
        this.lobbyID = lobbyID;
        this.theUser = theUser;
        this.userGivedUp = userGivedUp;
    }

    /**
     * Gibt die Lobby-ID zurück
     *
     * @return lobbyID die Lobby-ID
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Gibt den User zurück
     *
     * @return theUser der User
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public UserDTO getTheUser() {
        return theUser;
    }

    /**
     * Gibt zurück, ob der User aufgegeben hat oder nicht
     *
     * @return userGivedUp ob der User aufgegeben hat (Boolean)
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public Boolean getUserGivedUp() {
        return userGivedUp;
    }
}
