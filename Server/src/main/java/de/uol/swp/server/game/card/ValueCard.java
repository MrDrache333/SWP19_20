package de.uol.swp.server.game.card;

/**
 * Die Provinzkarte
 */
public class ValueCard extends Card {

    private short Value;

    /**
     * Erstellt eine neue Provinzkarte
     *
     * @param name Der name der Karte
     * @param id   Die ID der Karte
     */
    public ValueCard(String name, short id) {
        super(Type.MoneyCard, name, id);
    }

    /**
     * Gibt den Anwesenheitswert der Karte zurück
     *
     * @return Der Anwesenheitswert
     */
    public short getValue() {
        return Value;
    }

    /**
     * Setzt den Anwesenheitswert der Karte
     *
     * @param value Der Anwesenheitswert
     */
    public void setValue(short value) {
        Value = value;
    }
}
