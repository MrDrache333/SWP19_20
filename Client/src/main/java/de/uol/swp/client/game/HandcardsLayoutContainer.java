package de.uol.swp.client.game;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 * Layoutcontainer für die Karten auf der Hand.
 * Die Kinderknoten werden hier immer mittig angeordnet.
 * Würde die Breite des Containers überschritten werden, fangen die Knoten an sich zu überlappen, soviel wie nötig ist.
 *
 * @author Anna
 * @since Sprint6
 */
public class HandcardsLayoutContainer extends Region {

    public HandcardsLayoutContainer() {
    }

    public HandcardsLayoutContainer(double layoutX, double layoutY, double height, double width) {
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        this.setPrefHeight(height);
        this.setPrefWidth(width);
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        ObservableList<Node> children = getChildren();
        double sum = 0;
        double size = this.getChildren().size();
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
}

