package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

/**
 * The type Updated lobby ready status message.
 */
public class UpdatedLobbyReadyStatusMessage extends AbstractLobbyMessage {

    private boolean Ready;

    /**
     * Instantiates a new Updated lobby ready status message.
     *
     * @param lobby the lobby
     * @param user  the user
     * @param ready the Status
     */
    public UpdatedLobbyReadyStatusMessage(String lobby, User user, boolean ready) {
        super(lobby, user);
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
