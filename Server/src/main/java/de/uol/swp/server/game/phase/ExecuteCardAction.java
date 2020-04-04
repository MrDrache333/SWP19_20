package de.uol.swp.server.game.phase;

import de.uol.swp.server.game.player.Player;

public class ExecuteCardAction {
    private short cardID;
    private Player player;
    // Eventbus übergeben fehlt

    public ExecuteCardAction(short cardID, Player thePlayer) {
        this.player = thePlayer;
        this.cardID = cardID;
    }

}
