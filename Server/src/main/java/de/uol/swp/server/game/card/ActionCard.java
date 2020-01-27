package de.uol.swp.server.game.card;

/**
 * Die AKtionkarte
 */
public class ActionCard extends Card {

    /**
     * Erstellt eine neue Aktionskarte
     *
     * @param name Der name der Karte
     * @param id   Die ID der Karte
     */
    public ActionCard(String name, short id) {
        super(Type.ActionCard, name, id);
    }
}
