package de.uol.swp.common.game.exception;

/**
 * Exception, die geworfen wird, wenn versucht wird, eine in der aktuellen Phase nicht erlaubte Aktion auszuführen
 *
 * @author Julia
 * @since Sprint 5
 */
public class GamePhaseException extends RuntimeException {

    public GamePhaseException(String message) {
        super(message);
    }
}
