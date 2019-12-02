package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * The type Updated lobby ready status message.
 */
public class UpdatedLobbyReadyStatusMessage extends AbstractLobbyMessage {

    private boolean Ready;

    /**
     * Instantiates a new Updated lobby ready status message.
     *
     * @param lobbyname the lobby
     * @param user      the user
     * @param ready     the Status
     */
    public UpdatedLobbyReadyStatusMessage(UUID LobbyID, String lobbyname, User user, boolean ready) {
        super(lobbyname, user, LobbyID);
        this.Ready = ready;
    }


    /**
     * Gets Status.
     *
     * @return Value of Status.
     */
    public boolean isReady() {
        return Ready;
    }
}
