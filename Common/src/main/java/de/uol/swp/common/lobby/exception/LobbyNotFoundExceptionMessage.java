package de.uol.swp.common.lobby.exception;

import de.uol.swp.common.lobby.message.AbstractLobbyMessage;
import de.uol.swp.common.user.User;

/**
 * @author Marvin
 * @version 0.1
 * Exception falls keine Lobby mit dem Namen gefunden wird
 */

public class LobbyNotFoundExceptionMessage extends AbstractLobbyMessage {

    private final String message;
    private final User user;

    public LobbyNotFoundExceptionMessage(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return message;
    }
}
