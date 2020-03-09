package de.uol.swp.server.game.card;

/**
 * Die Geldkarte
 */
public class MoneyCard extends Card {

    private short value;


    /**
     * Erstellt eine neue Geldkarte
     *
     * @param name Der name der Karte
     * @param id   Die ID der Karte
     * @author KenoO
     * @since Sprint 5
     */
    public MoneyCard(String name, short id) {
        super(Type.MoneyCard, name, id);
    }

    /**
     * Gibt die Kaufkraft der Karte zurück
     *
     * @return Der Kaufkraft
     * @author KenoO
     * @since Sprint 5
     */
    public short getValue() {
        return value;
    }

    /**
     * Setzt die Kaufkraft der Karte
     *
     * @param value Die Kaufkraft
     * @author KenoO
     * @since Sprint 5
     */
    public void setValue(short value) {
        this.value = value;
    }
}
