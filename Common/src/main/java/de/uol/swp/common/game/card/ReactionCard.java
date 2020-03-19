package de.uol.swp.common.game.card;

/**
 * Die Reaktionkarte
 */
public class ReactionCard extends Card {

    /**
     * Erstellt eine neue Reaktionkarte
     *
     * @param name Der name der Karte
     * @param id   Die ID der Karte
     * @author KenoO
     * @since Sprint 5
     */
    public ReactionCard(String name, short id, short costs) {
        super(Type.ReactionCard, name, id, costs);
    }
}
