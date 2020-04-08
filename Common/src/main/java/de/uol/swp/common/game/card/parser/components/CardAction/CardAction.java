package de.uol.swp.common.game.card.parser.components.CardAction;

/**
 * Abstrakte Kartenaktion.
 */
public abstract class CardAction {

    public CardAction() {

    }
    /**
     * Sagt aus, auf wen eine bestimmte Aktion ausgeführt wird.
     */
    public enum ExecuteType {
        /**
         * Auf niemanden anders(Nur mich selbst).
         */
        NONE,
        /**
         * Auf alle Spieler.
         */
        ALL,
        /**
         * Auf alle anderen Spieler(Nicht mich).
         */
        OTHERS,
        /**
         * Auf den Spieler, der vor mir am Zug war.
         */
        LAST,
        /**
         * Auf den Spieler, der nach mir am Zug ist.
         */
        NEXT
    }

}
