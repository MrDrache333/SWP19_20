package de.uol.swp.client.game.container;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 * Layoutcontainer für die Karten auf der Hand.
 *
 * @author Anna
 * @since Sprint6
 */
public class HandcardsLayoutContainer extends Region {

    /**
     * Instantiiert einen neuen HandcardsLayoutContainer.
     *
     * @author Anna
     * @since Sprint6
     */
    public HandcardsLayoutContainer() {
    }

    /**
     * Instantiiert einen neuen HandcardsLayoutContainer.
     *
     * @param layoutX x-Koordinate
     * @param layoutY y-Koordinate
     * @param height  Höhe
     * @param width   Breite
     * @author Anna
     * @since Sprint6
     */
    public HandcardsLayoutContainer(double layoutX, double layoutY, double height, double width, String id) {
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        super.setId(id);
        if(super.getId() == "2.HCLC" || super.getId() == "3.HCLC") {
            super.setRotate(90);
        }
    }

    /**
     * Hier wird festgelegt, wie sich die Kinder anordnen sollen.
     * Wenn die Pane breit genug ordnen sie sich, von der Mitte ausgehend, direkt nebeneinander an.
     * Ansonsten überlappen sie sich, aber immer nur so viel wie nötig.
     *
     * @author Anna
     * @since Sprint6
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
     * @author Anna
     * @since Sprint6
     */
    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }
}

