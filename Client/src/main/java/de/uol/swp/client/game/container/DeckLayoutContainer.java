package de.uol.swp.client.game.container;

import de.uol.swp.client.game.Card;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Layoutcontainer für das Deck.
 *
 * @author Devin
 * @since Sprint 7
 */

public class DeckLayoutContainer extends StackPane {

    /**
     * Instanziiert einen neuen DeckLayoutContainer.
     *
     * @author Devin
     * @since Sprint 7
     */
    public DeckLayoutContainer() {
    }

    /**
     * Instanziiert einen neuen DeckLayoutContainer.
     *
     * @param layoutX x-Koordinate des Containers
     * @param layoutY y-Koordinate des Containers
     * @param height  Höhe des Containers
     * @param width   Breite des Containers
     * @param id   ID des Containers
     * @author Devin
     * @since Sprint 7
     */
    public DeckLayoutContainer(double layoutX, double layoutY, double height, double width, String id) {
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        this.setId(id);
        if (this.getId().equals("1.DLC")) {
            this.setRotate(180);
        } else {
            if (this.getId().equals("2.DLC")) {
                this.setRotate(90);
            } else if (this.getId().equals("3.DLC")) {
                this.setRotate(270);
            }
        }

        // this.getChildren().add(new Card("card_back",this.getLayoutX(), this.getLayoutY(), 110, 60));
    }

    /**
     * Hier wird festgelegt, wie sich die Kinder anordnen sollen.
     * Wenn die Pane breit genug ordnen sie sich, von der Mitte ausgehend, direkt nebeneinander an.
     * Ansonsten überlappen sie sich, aber immer nur so viel wie nötig.
     *
     * @author Devin
     * @since Sprint 6
     */
    @Override
    protected void layoutChildren() {
        ObservableList<Node> children = getChildren();
        double sum = 0;
        double size = children.size();
        for (Node child : children) {
            sum += Math.round(child.getBoundsInLocal().getWidth());
        }
        double diff = this.getWidth() - sum;
        if (diff > 0) {
            double counter = size;
            double width = sum / size;
            for (Node child : children) {
                child.relocate(this.getWidth() / 2 + width * (size / 2) - width * counter, 0);
                counter--;
            }
        } else {
            double change = (-diff) / (size - 1);
            int i = 0;
            for (Node child : children) {
                child.relocate(i, 0);
                i += Math.round(child.getBoundsInLocal().getWidth() - change);
            }
        }
    }

    /**
     * Getter für fie Liste der Kinderknoten
     *
     * @return Kinder
     * @author Devin
     * @since Sprint 6
     */
    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

}
