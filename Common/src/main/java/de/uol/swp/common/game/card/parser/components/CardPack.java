package de.uol.swp.common.game.card.parser.components;

import com.google.gson.annotations.SerializedName;

/**
 * Das CardPack representiert ein Kartenpaket.
 */
public class CardPack {

    @SerializedName("packname")
    private String name;
    @SerializedName("_description")
    private String description;

    @SerializedName("cards")
    private CardStack cards;

    @Override
    public String toString() {
        return "Name: " + name + "\nDescription: " + description + "\n" + cards.toString();
    }

    /**
     * Gibt den Paketnamen zurück
     *
     * @return Der Paketname
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Paketbeschreibung zurück
     *
     * @return Die Beschreibung
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gibt alle im Pack enthaltenen Karten zurück
     *
     * @return Die Karten
     */
    public CardStack getCards() {
        return cards;
    }
}
