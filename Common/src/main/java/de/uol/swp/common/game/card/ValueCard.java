package de.uol.swp.common.game.card;

/**
 * Die Provinzkarte
 */
public class ValueCard extends Card {

    private short value;

    /**
     * Erstellt eine neue Provinzkarte
     *
     * @param name Der name der Karte
     * @param id   Die ID der Karte
     * @author KenoO
     * @since Sprint 5
     */
    public ValueCard(String name, short id, short costs, short value) {
        super(Type.VALUECARD, name, id, costs);
        this.value = value;
    }


    /**
     * Gibt den Anwesenheitswert der Karte zurück
     *
     * @return Der Anwesenheitswert
     * @author KenoO
     * @since Sprint 5
     */
    public short getValue() {
        return value;
    }

    /**
     * Setzt den Anwesenheitswert der Karte
     *
     * @param value Der Anwesenheitswert
     * @author KenoO
     * @since Sprint 5
     */
    public void setValue(short value) {
        this.value = value;
    }
}
