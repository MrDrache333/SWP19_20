package de.uol.swp.server.game.card;

/**
 * Die Spielkarte
 */
public abstract class Card {

    private String name;
    private short id;
    private Type type;

    /**
     * Erstellt eine neue Spielkarte
     *
     * @param type Der Kartentyp
     * @param name Der Kartenname
     * @param id   Die KartenID
     */
    public Card(Type type, String name, short id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    /**
     * Gibt den namen der Karte zurück
     *
     * @return Der name der Karte
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die ID der Karte zurück
     *
     * @return Die ID der Karte
     */
    public short getId() {
        return id;
    }

    /**
     * Gibt den Typ einer Karte zurück
     *
     * @return Der Kartentyp
     */
    public Type getType() {
        return type;
    }

    public enum Type {
        /**
         * Der Typ Aktionskarte
         */
        ActionCard,
        /**
         * Der Typ Reaktionskarte
         */
        ReactionCard,
        /**
         * Der Typ Provinzkarte
         */
        ValueCard,
        /**
         * Der Typ Geldkarte
         */
        MoneyCard
    }
}
