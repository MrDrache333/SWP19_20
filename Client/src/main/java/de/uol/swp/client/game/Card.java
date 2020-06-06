package de.uol.swp.client.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Die ImageView einer Karte.
 *
 * @author Devin
 * @since Sprint 7
 */

public class Card extends ImageView {

    /**
     * Basiskonstruktor
     * Instanziiert eine ImageView einer Karte.
     *
     * @author Devin
     * @since Sprint 7
     */
    public Card() {
    }

    /**
     * Instanziiert eine ImageView einer Karte.
     *
     * @author Devin
     * @since Sprint 7
     */
    public Card(String id) {
        this.setImage(new Image("file:Client/src/main/resources/cards/images/" + id + ".png"));
        this.setPreserveRatio(true);
        this.setFitHeight(107);
        this.setFitWidth(this.getBoundsInLocal().getWidth());
        this.setId(id);
    }

    /**
     * Überladener Konstruktor
     * Instanziiert eine ImageView einer Karte.
     *
     * @param height   die Höhe
     * @param id       die Karten-ID
     * @param layout_x der x-Wert
     * @param layout_y der y-Wert
     * @author Devin
     * @since Sprint 7
     */
    public Card(String id, double layout_x, double layout_y, double height) {
        this.setImage(new Image("file:Client/src/main/resources/cards/images/" + id + ".png"));
        this.setPreserveRatio(true);
        this.setLayoutX(layout_x);
        this.setLayoutY(layout_y);
        this.setFitHeight(height);
        this.setFitWidth(Math.round(this.getBoundsInLocal().getWidth()));
        this.setId(id);
    }

}
