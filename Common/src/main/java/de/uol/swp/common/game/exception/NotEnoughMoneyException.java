package de.uol.swp.common.game.exception;

/**
 * @author Paula
 * @since Sprint 6
 */
public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException(String message) {
        super(message);
    }

    public NotEnoughMoneyException() {

    }
}
