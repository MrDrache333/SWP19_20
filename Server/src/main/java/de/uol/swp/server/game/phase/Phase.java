package de.uol.swp.server.game.phase;

/**
 * Das interface einer InGame Phase
 */
interface Phase {

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
