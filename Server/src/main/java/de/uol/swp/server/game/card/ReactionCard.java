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
     */
    public ReactionCard(String name, short id) {
        super(Type.ReactionCard, name, id);
    }
}
