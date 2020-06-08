package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Request um die maixmale Spieleranzahl festzulegen
 *
 * @author Timo, Rike
 * @since Sprint 3
 */
public class SetMaxPlayerRequest extends AbstractLobbyRequest {
    private static final long serialVersionUID = -8642526060768371983L;
    private final Integer maxPlayerValue;

    /**
     * Initialisiert eine neue setMaxPlayerRequest
     *
     * @param maxPlayerValue maximale Spielranzahl
     * @param lobbyID        die LobbyID
     * @param user           Der Benutzer
     * @author Timo, Marvin
     * @since Sprint 3
     */
    public SetMaxPlayerRequest(UUID lobbyID, UserDTO user, Integer maxPlayerValue) {
        super(lobbyID, user);
        this.maxPlayerValue = maxPlayerValue;
    }

    /**
     * Gibt die maximale Spieleranzahl zurück
     *
     * @return maximale Spieleranzahl
     * @author Timo
     * @since Sprint 3
     */
    public Integer getMaxPlayerValue() {
        return maxPlayerValue;
    }

}

