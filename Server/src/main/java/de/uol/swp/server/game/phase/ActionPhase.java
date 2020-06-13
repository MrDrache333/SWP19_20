package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.phase.Phase;
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
     * @since Sprint 5
     */
    void executeActionPhase(Player player, short cardId);
}
