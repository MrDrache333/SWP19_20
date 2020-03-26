package de.uol.swp.common.game.request;

import de.uol.swp.common.game.response.AbstractGameMessage;


public class SelectCardRequest {
    private AbstractGameMessage message;

    /**
     * Erstellt neue SelectCardRequest
     *
     * @uthor Paula
     * @version 1
     * @since Sprint5
     */
    public SelectCardRequest(AbstractGameMessage message) {
        this.message = message;

    }

    public AbstractGameMessage getMessage() {
        return message;
    }
}