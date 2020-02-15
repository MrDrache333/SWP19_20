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
     * @author KenoO
     * @since Sprint 5
     */
    public ValueCard(String name, short id) {
        super(Type.ValueCard, name, id);
    }

    /**
     * Gibt den Anwesenheitswert der Karte zurück
     *
     * @return Der Anwesenheitswert
     * @author KenoO
     * @since Sprint 5
     */
    public short getValue() {
        return Value;
    }

    /**
     * Setzt den Anwesenheitswert der Karte
     *
     * @param value Der Anwesenheitswert
     * @author KenoO
     * @since Sprint 5
     */
    public void setValue(short value) {
        Value = value;
    }
}
