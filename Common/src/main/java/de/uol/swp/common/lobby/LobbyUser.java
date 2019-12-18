package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

/**
 * The type Lobby user.
 * @author Keno Oelrichs Garcia
 * @Version 1.0
 * @since Sprint3
 */
public class LobbyUser extends UserDTO {

    //Currents Status
    private boolean Ready;

    /**
     * Instantiates a new Lobby user.
     *
     * @param user the user
     * @author Keno Oelrichs Garcia
     * @Version 1.0
     * @since Sprint3
     */
    public LobbyUser(User user) {
        super(user.getUsername(), user.getPassword(), user.getEMail());
    }

    /**
     * Is ready boolean.
     *
     * @return the boolean
     * @author Keno Oelrichs Garcia
     * @Version 1.0
     * @since Sprint3
     */
    public boolean isReady() {
        return Ready;
    }

    /**
     * Sets ready.
     *
     * @param ready the Status
     * @author Keno Oelrichs Garcia
     * @Version 1.0
     * @since Sprint3
     */
    public void setReady(boolean ready) {
        Ready = ready;
    }
}
