package de.uol.swp.client;

import de.uol.swp.client.game.AnimationManagement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.InputStream;

/**
 * Klasse zum Testen der Kartenanimationen.
 */

public class AnimationTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/images/karte_gross.png");
        Image image = new javafx.scene.image.Image(input);

        //Karte die der Gegenspieler spielt
        ImageView c1 = new ImageView();
        c1.setImage(image);
        c1.setFitHeight(59);
        c1.setFitWidth(43);
        /*Pane c1 = new Pane();
        c1.setPrefSize(43, 59);
        c1.setStyle("-fx-background-color: purple");*/

        //Fixpunkt, zeigt an wo die Hand des Spielers beginnt
        Pane c2 = new Pane();
        c2.setLayoutX(284);
        c2.setLayoutY(541);
        c2.setPrefSize(10, 10);
        c2.setStyle("-fx-background-color: blue");

        //Fixpunkt, zeigt an wo die AKtionszone des Spielers beginnt
        Pane c3 = new Pane();
        c3.setLayoutX(356);
        c3.setLayoutY(415);
        c3.setPrefSize(10, 10);
        c3.setStyle("-fx-background-color: green");

        //Karte die der Spieler ausspielt
        ImageView c4 = new ImageView();
        c4.setImage(image);
        c4.setFitHeight(59);
        c4.setFitWidth(43);
        c4.setX(284);
        c4.setY(541);
        /*Pane c4 = new Pane();
        c4.setLayoutX(284);
        c4.setLayoutY(541);
        c4.setPrefSize(43, 59);
        c4.setStyle("-fx-background-color: pink");*/


        //Karte die der Spieler kauft
        ImageView c6 = new ImageView();
        c6.setImage(image);
        c6.setFitHeight(59);
        c6.setFitWidth(43);
        c6.setX(500);
        c6.setY(300);
        /*Pane c5 = new Pane();
        c5.setLayoutX(500);
        c5.setLayoutY(300);
        c5.setPrefSize(43, 59);
        c5.setStyle("-fx-background-color: turquoise");*/

        Pane bg = new Pane(c1, c2, c3, c4, c6);

        primaryStage.setScene(new Scene(bg, 1280, 750));
        primaryStage.show();

        //Wenn die Maus gedrückt wird, spielt der Spieler eine Karte
        bg.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            AnimationManagement.playCard(c4, 0);
        });

        //Wenn sie wieder losgelassen wird, kauft der Spieler eine Karte
        bg.addEventHandler(MouseEvent.MOUSE_RELEASED, e-> {
            AnimationManagement.buyCard(c6);
        });

        //beim Starten der Anwendung spielt der Gegenspieler eine Karte
        AnimationManagement.opponentPlaysCard(c1, 0);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
