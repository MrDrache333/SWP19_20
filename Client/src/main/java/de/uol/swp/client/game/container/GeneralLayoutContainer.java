package de.uol.swp.client.game.container;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 * Genereller Layout-Container für Karten als zusammenfassender Container für den DeckLayoutContainer, DiscardPileLayoutContainer und den HandcardsLayoutContainer
 *
 * @author Timo
 * @since Sprint 9
 */
public class GeneralLayoutContainer extends Region {

    /**
     * Instanziiert einen neuen GeneralLayoutContainer
     *
     * @author Timo
     * @since Sprint 9
     */
    public GeneralLayoutContainer() {

    }

    /**
     * Überladener Konstruktur
     * Instanziiert einen neuen GeneralLayoutContainer
     *
     * @param id      ID des Containers
     * @param height  Höhe des Containers
     * @param width   Breite des Containers
     * @param layoutX X-Koordinate des Containers
     * @param layoutY Y-Koordinate des Containers
     * @author Timo
     * @since Sprint 9
     */
    public GeneralLayoutContainer(double layoutX, double layoutY, double height, double width, String id) {
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        this.setId(id);
        if (this.getId().equals("1.DPLC") || this.getId().equals("1.DLC") || this.getId().equals("1.HCLC")) {
            this.setRotate(180);
        } else {
            if (this.getId().equals("2.DPLC") || this.getId().equals("2.DLC") || this.getId().equals("2.HCLC")) {
                this.setRotate(90);
            } else if (this.getId().equals("3.DPLC") || this.getId().equals("3.DLC") || this.getId().equals("3.HCLC")) {
                this.setRotate(270);
            }
        }
    }

    /**
     * Hier wird festgelegt, wie sich die Kinder anordnen sollen.
     * Wenn die Pane breit genug ordnen sie sich, von der Mitte ausgehend, direkt nebeneinander an.
     * Ansonsten überlappen sie sich, aber immer nur so viel wie nötig.
     *
     * @author Anna, Devin, Timo
     * @since Sprint 9
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
     * @author Anna, Devin, Timo
     * @since Sprint 9
     */
    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }
}