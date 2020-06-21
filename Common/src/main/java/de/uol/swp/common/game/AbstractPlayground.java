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
        TRASH,
        /**
         * Hand
         */
        HAND,
        /**
         * Kaufzone
         */
        BUY,
        /**
         * Nachziehstapel
         */
        DRAW,
        /**
         * Ablegestapel
         */
        DISCARD,
        /**
         * Aktionszone
         */
        PLAY,
        /**
         * Zwischenspeicher
         */
        TEMP,
        /**
         * Keine festgelegte Zone
         */
        NONE
    }

    /**
     * Repräsentiert mögliche Typen von Spielerspezifischen Eigenschaften.
     */
    public enum PlayerActivityValue {
        /**
         * Buy player activity value.
         */
        BUY,
        /**
         * Action player activity value.
         */
        ACTION,
        /**
         * Money player activity value.
         */
        MONEY
    }
}
