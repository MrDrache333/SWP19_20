package de.uol.swp.common.game.card.parser.components.CardAction;

/**
 * Interface einer Kartenaktion
 */
public interface Action {

    /**
     * Ausführen einer Aktion
     *
     * @return Ob die Aktion erfolgreich war
     * @author KenoO
     * @since Sprint 6
     */
    boolean execute();
}
