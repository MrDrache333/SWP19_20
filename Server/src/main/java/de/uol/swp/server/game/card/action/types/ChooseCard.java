package de.uol.swp.server.game.card.action.types;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.action.ComplexCardAction;

import java.util.ArrayList;

public class ChooseCard extends ComplexCardAction {

    private short count;//Anzahl der Karten
    private ArrayList<Card> choosenCards;   //Ausgewählte Karten

    public ChooseCard(short count) {
        this.count = count;
    }


    @Override
    public boolean execute() {
        return false;
    }

    public ArrayList<Card> getChoosenCards() {
        return choosenCards;
    }
}
