package de.uol.swp.common.game.phase;

/**
 * Das Interface einer InGame-Phase
 *
 * @author Keno O, Paula
 * @since Sprint 4
 */
public interface Phase {

    /**
     * Die verschiedenen Phasen
     */
    enum Type {
        /**
         * Die Aktionsphase
         */
        ActionPhase,
        /**
         * Die Kaufphase
         */
        Buyphase,
        /**
         * Die Aufräumphase
         */
        Clearphase
    }
}
