package de.uol.swp.common.game.card;

import com.google.gson.annotations.SerializedName;

/**
 * Die Spielkarte
 */
public abstract class Card {

    /**
     * The Name.
     */
    private String name;
    /**
     * The Id.
     */
    private short id;
    /**
     * The Cardtype.
     */
    private Type cardType;
    /**
     * The Costs.
     */
    @SerializedName("cost")
    private short costs;

    /**
     * Erstellt eine neue Spielkarte
     *
     * @param cardType Der Kartentyp
     * @param name     Der Kartenname
     * @param id       Die KartenID
     * @author KenoO
     * @since Sprint 5
     */
    public Card(Type cardType, String name, short id, short costs) {
        this.cardType = cardType;
        this.name = name;
        this.id = id;
        this.costs = costs;
    }


    /**
     * Gets costs.
     *
     * @return the costs
     * @author KenoO
     * @since Sprint 5
     */
    public short getCosts() {
        return costs;
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
    public Type getCardType() {
        return cardType;
    }

    /**
     * The enum Type.
     */
    public enum Type {
        /**
         * Kein Festgelegter Kartentyp
         */
        NONE,
        /**
         * Der Typ Aktionskarte
         */
        ACTIONCARD,
        /**
         * Der Typ Reaktionskarte
         */
        REACTIONCARD,
        /**
         * Der Typ Provinzkarte
         */
        VALUECARD,
        /**
         * Der Typ Geldkarte
         */
        MONEYCARD,
        /**
         * Der Typ Fluchkarte.
         */
        CURSECARD
    }
}