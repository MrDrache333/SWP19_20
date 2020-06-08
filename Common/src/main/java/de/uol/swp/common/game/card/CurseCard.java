package de.uol.swp.common.game.card;

/**
 * Die Fluchkarte
 */
public class CurseCard extends Card {

    private short value;

    /**
     * Erstellt eine neue Fluchkarte
     *
     * @param name Der name der Karte
     * @param id   Die ID der Karte
     * @author KenoO
     * @since Sprint 6
     */
    public CurseCard(String name, short id, short value) {
        super(Type.CURSECARD, name, id, (short) 0);
        this.value = value;
    }

    /**
     * Gibt den Fluchwert der Karte zurück
     *
     * @return Der Fluchwert
     * @author KenoO
     * @since Sprint 6
     */
    public short getValue() {
        return value;
    }

    /**
     * Setzt den Fluchwert der Karte
     *
     * @param value Der Fluchwert
     * @author KenoO
     * @since Sprint 6
     */
    public void setValue(short value) {
        this.value = value;
    }
}
