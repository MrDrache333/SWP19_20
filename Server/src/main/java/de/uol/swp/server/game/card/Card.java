package de.uol.swp.server.game.card;

/**
 * Die Spielkarte
 */
public abstract class Card {

    private String name;
    private short id;
    private Type cardtype;

    public short getCosts() {
        return costs;
    }

    private short costs;

    /**
     * Erstellt eine neue Spielkarte
     *
     * @param cardtype Der Kartentyp
     * @param name Der Kartenname
     * @param id   Die KartenID
     * @author KenoO
     * @since Sprint 5
     */
    Card(Type cardtype, String name, short id) {
        this.cardtype = cardtype;
        this.name = name;
        this.id = id;
    }

    /**
     * Gibt den namen der Karte zurück
     *
     * @return Der name der Karte
     * @author KenoO
     * @since Sprint 5
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die ID der Karte zurück
     *
     * @return Die ID der Karte
     * @author KenoO
     * @since Sprint 5
     */
    public short getId() {
        return id;
    }

    /**
     * Gibt den Typ einer Karte zurück
     *
     * @return Der Kartentyp
     * @author KenoO
     * @since Sprint 5
     */
    public Type getCardtype() {
        return cardtype;
    }

    /**
     * The enum Type.
     */
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
