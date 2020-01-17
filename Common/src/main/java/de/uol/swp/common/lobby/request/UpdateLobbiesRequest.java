package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

/**
 * ???
 */
public class UpdateLobbiesRequest extends AbstractLobbyRequest {

    private UserDTO oldUser;
    private UserDTO updatedUser;

    /**
     * Konstruktor des Requests.
     *
     * @param updatedUser neuer Benutzer
     * @param oldUser der Alte Benutzer
     * @author Julia
     */
    public UpdateLobbiesRequest(UserDTO updatedUser, UserDTO oldUser) {
        this.updatedUser = updatedUser;
        this.oldUser = oldUser;
    }

    /**
     * Gibt den neuen Benutzer zurück.
     *
     * @return UserDTO des neuen Benutzers
     */
    public UserDTO getUpdatedUser() {
        return updatedUser;
    }

    /**
     * Gibt den alten Benutzer zurück.
     *
     * @return UserDTO des alten Nutzers
     */
    public UserDTO getOldUser() {
        return oldUser;
    }
}
