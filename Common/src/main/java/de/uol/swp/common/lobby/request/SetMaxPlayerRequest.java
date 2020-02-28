package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Request um die maixmale Spieleranzahl festzulegen
 *
 * @author Timo, Rike
 * @since Sprint 3
 */
public class SetMaxPlayerRequest extends AbstractRequestMessage {
    private static final long serialVersionUID = -8642526060768371983L;
    private Integer maxPlayerValue;
    private UUID lobbyID;
    private User loggedInUser;

    /**
     * Initialisiert eine neue setMaxPlayerRequest
     *
     * @param maxPlayerValue maximale Spielranzahl
     * @param lobbyID        die LobbyID
     * @param loggedInUser   Der eingeloggte User
     * @author Timo
     * @since Sprint3
     */
    public SetMaxPlayerRequest(Integer maxPlayerValue, UUID lobbyID, User loggedInUser) {
        this.maxPlayerValue = maxPlayerValue;
        this.lobbyID = lobbyID;
        this.loggedInUser = loggedInUser;
    }

    /**
     * Gibt die maximale Spieleranzahl zurück
     *
     * @return maximale Spieleranzahl
     * @author Timo
     * @since Sprint3
     */
    public Integer getMaxPlayerValue() {
        return maxPlayerValue;
    }

    public void setMaxPlayerValue(Integer maxPlayerValue) {
        this.maxPlayerValue = maxPlayerValue;
    }


    /**
     * Gibt die LobbyID zurück
     *
     * @return LobbyID
     * @author Timo
     * @since Sprint3
     */

    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Setzt die LobbyID
     *
     * @param lobbyID Die LobbyID
     * @author Timo
     * @since Sprint3
     */
    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    /**
     * Gibt den eingeloggten User zurück
     *
     * @return den eingeloggten User
     * @author Timo
     * @since Sprint3
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Setzt den eingeloggten User
     *
     * @param loggedInUser der eingeloggte User
     * @author Timo
     * @since Sprint3
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

}

