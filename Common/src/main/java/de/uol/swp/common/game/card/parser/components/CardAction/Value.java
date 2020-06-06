package de.uol.swp.common.game.card.parser.components.CardAction;

import java.io.Serializable;

/**
 * Beschreibt einen Bereich mittels eines minimalen und maximalen Wert
 */
public class Value implements Serializable {

    /**
     * Minimaler Wert
     */
    private short min;
    /**
     * Maximaler Wert
     */
    private short max;

    /**
     * Erstellt einen neuen Wertebereich
     *
     * @param min Minimaler Wert
     * @author KenoO
     * @since Sprint 6
     */
    public Value(short min) {
        this.min = min;
        this.max = min;
    }

    /**
     * Erstellt einen neuen Wertebereich
     *
     * @param min Minimaler Wert
     * @param max Maximaler Wert
     * @author KenoO
     * @since Sprint 6
     */
    public Value(short min, short max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Gibt den minimalen Wert zurück
     *
     * @return Minimaler Wert
     * @author KenoO
     * @since Sprint 6
     */
    public short getMin() {
        return min;
    }

    /**
     * Setzt eienn neuen Minimalen Wert
     *
     * @param min Minimaler Wert
     * @author KenoO
     * @since Sprint 6
     */
    public void setMin(short min) {
        this.min = min;
    }

    /**
     * Gibt den maximalen Wert zurück
     *
     * @return Maximaler Wert
     * @author KenoO
     * @since Sprint 6
     */
    public short getMax() {
        return max;
    }

    /**
     * Setzt einen neuen Maximalen Wert
     *
     * @param max Maximaler Wert
     * @author KenoO
     * @since Sprint 6
     */
    public void setMax(short max) {
        this.max = max;
    }

    /**
     * Gibt zurück, ob einer der Werte bereits gesetzt wurde
     *
     * @return Wert, ob bereits ein Wert gesetzt wurde
     * @author KenoO
     * @since Sprint 6
     */
    public boolean isSet() {
        return min != 0 || max != 0;
    }
}
