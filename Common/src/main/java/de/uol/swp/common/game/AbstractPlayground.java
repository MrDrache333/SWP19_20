package de.uol.swp.common.game;

/**
 * The type AbstractPlayground.
 */
public abstract class AbstractPlayground {


    /**
     * Repräsentiert die verschiedenen Zonen auf dem Spielfeld.
     */
    public enum ZoneType {
        /**
         * Müll
         */
        Trash,
        /**
         * Hand
         */
        Hand,
        /**
         * Kaufzone
         */
        Buy,
        /**
         * Nachziehstapel
         */
        Draw,
        /**
         * Ablegestapel
         */
        Discard,
        /**
         * Zwischenspeicher
         */
        Temp,
        /**
         * Keine festgelegte Zone
         */
        None
    }

    /**
     * Repräsentiert mögliche Typen von Spielerspezifischen Eigenschaften.
     */
    public enum PlayerActivityValue {
        /**
         * Buy player activity value.
         */
        Buy,
        /**
         * Action player activity value.
         */
        Action,
        /**
         * Money player activity value.
         */
        Money
    }
}
