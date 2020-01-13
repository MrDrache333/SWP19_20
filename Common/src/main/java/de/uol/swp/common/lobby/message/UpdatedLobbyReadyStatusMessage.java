package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Die Klasse der Updated lobby ready status message.
 *
 * @author Keno O.
 * @since Sprint 3
 */
public class UpdatedLobbyReadyStatusMessage extends AbstractLobbyMessage {

    private boolean Ready;

    /**
     * Konstruktor der Updated lobby ready status message.
     *
     * @author Keno O., Keno S., Darian
     * @since Sprint 3
     * @param lobbyName Die Lobby
     * @param user      Der User
     * @param ready     Der Lobbystatus
     */
    public UpdatedLobbyReadyStatusMessage(UUID LobbyID, String lobbyName, UserDTO user, boolean ready) {
        super(lobbyName, user, LobbyID);
        this.Ready = ready;
    }

    /**
     * Gibt den Status zurück.
     *
     * @author Keno O.
     * @since Sprint 3
     * @return Ready Der Lobbystatus
     */
    public boolean isReady() {
        return Ready;
    }
}
