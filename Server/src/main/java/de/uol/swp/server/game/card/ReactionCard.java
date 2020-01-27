package de.uol.swp.server.game.card;

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
    public ReactionCard(String name, short id) {
        super(Type.ReactionCard, name, id);
    }
}
