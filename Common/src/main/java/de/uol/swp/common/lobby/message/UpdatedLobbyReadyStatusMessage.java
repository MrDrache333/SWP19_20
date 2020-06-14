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

    private static final long serialVersionUID = 4060747266476002643L;
    private final boolean Ready;

    /**
     * Konstruktor der Updated lobby ready status message.
     *
     * @param lobbyID Die LobbyID
     * @param user    Der User
     * @param ready   Der Lobbystatus
     * @author Keno O., Keno S., Darian, Marvin
     * @since Sprint 3
     */
    public UpdatedLobbyReadyStatusMessage(UUID lobbyID, UserDTO user, boolean ready) {
        super(lobbyID, user);
        this.Ready = ready;
    }

    /**
     * Gibt den Status zurück.
     *
     * @return Ready Der Lobbystatus
     * @author Keno O.
     * @since Sprint 3
     */
    public boolean isReady() {
        return Ready;
    }
}
