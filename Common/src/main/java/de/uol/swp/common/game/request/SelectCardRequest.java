package de.uol.swp.common.game.request;

import de.uol.swp.common.game.response.GameMessage;


public class SelectCardRequest {
    private GameMessage Message;

    /**
     * Erstellt neue SelectCardRequest
     *
     * @uthor Paula
     * @version 1
     * @since Sprint5
     */
    public SelectCardRequest(GameMessage message) {
        this.Message = message;

    }
}