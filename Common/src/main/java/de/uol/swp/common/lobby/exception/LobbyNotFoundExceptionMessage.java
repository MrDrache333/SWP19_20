package de.uol.swp.common.lobby.exception;

import de.uol.swp.common.lobby.message.AbstractLobbyMessage;

/**
 * @author Marvin
 * @version 0.1
 * Exception falls keine Lobby mit dem Namen gefunden wird
 */

public class LobbyNotFoundExceptionMessage extends AbstractLobbyMessage {

    private final String message;

    public LobbyNotFoundExceptionMessage(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return "LobbyNotFoundExceptionMessage "+message;
    }
}
