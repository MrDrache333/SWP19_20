package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.messages.ShowCardMessage;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Player;

public class ExecuteCardAction {

    private short cardID;
    private Playground playground;
    private Player player;

    public ExecuteCardAction(short cardID, Playground playground) {
        this.playground = playground;
        this.cardID = cardID;
        player = playground.getActualPlayer();
    }

    /**
     * Erhöht availableActions, availableBuys oder additionalMoney des Players um den angegebenen Wert.
     *
     * @param count    Wert, um den das jeweilige Attribut erhöht werden soll
     * @param activity Attribut, das erhöht werden soll
     * @return true
     * @author Julia
     * @since Sprint6
     */
    public boolean executeAddCapablePlayerActivity(short count, AbstractPlayground.PlayerActivityValue activity) {
        if (activity == AbstractPlayground.PlayerActivityValue.ACTION) {
            player.setAvailableActions(count + player.getAvailableActions());
        } else if (activity == AbstractPlayground.PlayerActivityValue.BUY) {
            player.setAvailableBuys(count + player.getAvailableBuys());
        } else {
            player.setAdditionalMoney(count + player.getAdditionalMoney());
        }
        return true;
    }

    /**
     * Sendet eine Message mit der ID der Karte, die angezeigt werden soll und der Zone,
     * in der sie angezeigt werden soll an den aktuellen Spieler.
     *
     * @param cardID die ID der Karte
     * @param zone   die Zone im Spielfeld, in der die Karte angezeigt werdem soll
     * @return true
     * @author Julia
     * @since Sprint6
     */
    public boolean executeShowCard(short cardID, AbstractPlayground.ZoneType zone) {
        playground.getGameService().sendToSpecificPlayer(playground.getActualPlayer(), new ShowCardMessage(cardID, zone, playground.getID()));
        return true;
    }

}
