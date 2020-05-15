package de.uol.swp.client.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Die ImageView einer Karte.
 *
 * @author Devin
 * @since Sprint7
 */

public class Card extends ImageView {

    /**
     * Instanziiert eine ImageView einer Karte.
     *
     * @author Devin
     * @since Sprint7
     */
    public Card() {
    }

    /**
     * Instanziiert eine ImageView einer Karte.
     *
     * @author Devin
     * @since Sprint7
     */
    public Card(String id) {
        this.setImage(new Image("file:Client/src/main/resources/cards/images/" + id + ".png"));
    }

    public Card(String id, double layout_x, double layout_y, double height, double width) {
        this.setImage(new Image("file:Client/src/main/resources/cards/images/" + id + ".png"));
        this.setPreserveRatio(true);
        this.setLayoutX(layout_x);
        this.setLayoutY(layout_y);
        this.setFitHeight(height);
        this.setFitWidth(height);
        this.setFitWidth(Math.round(this.getBoundsInLocal().getWidth()));
        this.setId(id);
    }

}
