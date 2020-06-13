package de.uol.swp.common.game.card.parser.components.CardAction.types.subtypes;

import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;

/**
 * Bedingung, welche erfüllt sein muss
 *
 * @param <T> Der verwendete Typ
 */
public class Condition<T> {

    /**
     * Linker Parameter
     */
    private final T left;
    /**
     * Logischer Operator
     */
    private final Operator operator;
    /**
     * Der Rechte Parameter
     */
    private final T right;


    /**
     * Erstellt eine neue Bedingung
     *
     * @param left     Linker Parameter
     * @param operator Logischer Operator
     * @param right    Der Rechte Parameter
     * @author KenoO, Timo
     * @since Sprint 6
     */
    public Condition(T left, Operator operator, T right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /**
     * Erstellt eine neue Bedingung
     *
     * @param leftparameter Linker Parameter
     * @author KenoO, Timo
     * @since Sprint 6
     */
    public Condition(CardAction leftparameter) {
        this.left = (T) leftparameter;
        this.operator = Operator.EQUALS;
        this.right = (T) Boolean.TRUE;
    }

    /**
     * Gibt den linken Parameter zurück
     *
     * @return Linker Parameter
     * @author KenoO, Timo
     * @since Sprint 6
     */
    public T getLeft() {
        return left;
    }

    /**
     * Gibt den Vergleichsoperator zurück
     *
     * @return Der Vergleichsoperator
     * @author KenoO, Timo
     * @since Sprint 6
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Gibt den rechten Parameter zurück
     *
     * @return rechter Parameter
     * @author KenoO, Timo
     * @since Sprint 6
     */
    public T getRight() {
        return right;
    }

    /**
     * Überprüft die Bedingung anhand der festgelegten Parameter
     *
     * @return Das Ergebnis
     * @author KenoO, Timo
     * @since Sprint 6
     */
    public boolean check() {
        if (left != null && operator != null && right != null) {
            //TODO Mach was
        }
        return false;
    }

    /**
     * Die Vergleichsoperatoren
     */
    public enum Operator {
        /**
         * Gleich
         */
        EQUALS,
        /**
         * Größer
         */
        HIGHER,
        /**
         * Kleiner
         */
        LOWER,
        /**
         * Ungleich
         */
        DIFFERENT,
        /**
         * Größer oder gleich
         */
        HIGHEREQUALS,
        /**
         * Kleiner oder gleich
         */
        LOWEREQUALS
    }
}
