package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.exception.NotEnoughMoneyException;
import de.uol.swp.server.game.player.Player;

/**
 * Das Interface der Kaufphase
 */
interface BuyPhase extends Phase {

    /**
     * Führt die Phase auf einem Spieler aus
     *
     * @param player Der Spieler
     * @param cardId Die Karten-ID
     * @author KenoO
     * @Version 1.0
     * @since Sprint 5
     */
    void executeBuyPhase(Player player, short cardId) throws IllegalArgumentException, NotEnoughMoneyException;

}
