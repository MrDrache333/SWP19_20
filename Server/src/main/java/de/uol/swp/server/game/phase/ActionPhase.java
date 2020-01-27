package de.uol.swp.server.game.phase;

import de.uol.swp.server.game.player.Player;

/**
 * Das Interface der Aktionsphase
 */
interface ActionPhase extends Phase {

    /**
     * Führt die Aktionsphase auf einem Spieler aus
     *
     * @param player Der Spieler
     * @author KenoO
     * @Version 1.0
     * @since Sprint 5
     */
    void executeActionPhase(Player player, short cardId);
}
