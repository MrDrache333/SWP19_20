package de.uol.swp.server.game.card.action.types;

import de.uol.swp.common.game.card.parser.action.ComplexCardAction;

public class ChooseCard extends ComplexCardAction {

    private short count;    //Anzahl der Karten

    public ChooseCard(short count) {
        this.count = count;
    }


    @Override
    public boolean execute() {
        return false;
    }
}
