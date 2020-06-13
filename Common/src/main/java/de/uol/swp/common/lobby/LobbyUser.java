package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

/**
 * The type Lobby user.
 *
 * @author Keno O.
 * @version 1.0
 * @since Sprint 3
 */
public class LobbyUser extends UserDTO {

    //Current Status
    private boolean Ready;

    /**
     * Ein neuer Lobby-User wird erstellt der den normalen User erweitert mit einem Bereit-Status.
     *
     * @param user Der Benutzer der erweitert werden soll
     * @author Keno O.
     * @since Sprint 3
     */
    public LobbyUser(User user) {
        super(user.getUsername(), user.getPassword(), user.getEMail(), user.getIsBot());
    }

    /**
     * Der Bereit-Stautus des Spielers wird zurückgegeben
     *
     * @return Bereit-Status des Spielers
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    public boolean isReady() {
        return Ready;
    }

    /**
     * Der Bereit-Status von einem User in der Lobby wird geändert.
     *
     * @param ready der neue Bereit-Status
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    public void setReady(boolean ready) {
        Ready = ready;
    }


}
