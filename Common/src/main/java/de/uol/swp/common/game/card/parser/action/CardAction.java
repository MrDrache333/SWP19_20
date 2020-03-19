package de.uol.swp.common.game.card.parser.action;

/**
 * Abstrakte Kartenaktion.
 */
public abstract class CardAction implements Action {


    /**
     * Sagt aus, auf wen eine bestimmte Aktion ausgeführt wird.
     */
    public enum ExecuteType {
        /**
         * Auf niemanden anders(Nur mich selbst).
         */
        None,
        /**
         * Auf alle Spieler.
         */
        All,
        /**
         * Auf alle anderen Spieler(Nicht mich).
         */
        Others,
        /**
         * Auf den Spieler, der vor mir am Zug war.
         */
        Last,
        /**
         * Auf den Spieler, der nach mir am Zug ist.
         */
        Next
    }

}
