package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;

/**
 * The type Lobby user.
 */
public class LobbyUser extends UserDTO {

    //Currents Status
    private boolean Ready;

    /**
     * Instantiates a new Lobby user.
     *
     * @param user the user
     */
    public LobbyUser(User user) {
        super(user.getUsername(), user.getPassword(), user.getEMail());
    }

    /**
     * Is ready boolean.
     *
     * @return the boolean
     */
    public boolean isReady() {
        return Ready;
    }

    /**
     * Sets ready.
     *
     * @param ready the Status
     */
    public void setReady(boolean ready) {
        Ready = ready;
    }
}
