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
        ImageView cardToPlayByOpponent = new ImageView();
        cardToPlayByOpponent.setImage(image);
        cardToPlayByOpponent.setFitHeight(110);
        cardToPlayByOpponent.setFitWidth(60);

        //Fixpunkt, zeigt an wo die Hand des Spielers beginnt
        Pane hand = new Pane();
        hand.setLayoutX(284);
        hand.setLayoutY(541);
        hand.setPrefSize(10, 10);
        hand.setStyle("-fx-background-color: blue");

        //Fixpunkt, zeigt an wo die Aktionszone des Spielers beginnt
        Pane actionZone = new Pane();
        actionZone.setLayoutX(356);
        actionZone.setLayoutY(415);
        actionZone.setPrefSize(10, 10);
        actionZone.setStyle("-fx-background-color: green");

        //Karte die der Spieler ausspielt
        ImageView cardToPlay = new ImageView();
        cardToPlay.setImage(image);
        cardToPlay.setFitHeight(110);
        cardToPlay.setFitWidth(60);
        cardToPlay.setX(150);
        cardToPlay.setY(538);

        //Fixpunkt, zeigt an wo der Müll beginnt
        Pane trash = new Pane();
        trash.setLayoutX(100);
        trash.setLayoutY(233);
        trash.setPrefSize(10, 10);
        trash.setStyle("-fx-background-color: purple");

        //Karte die der Spieler kauft
        ImageView cardToBuy = new ImageView();
        cardToBuy.setImage(image);
        cardToBuy.setFitHeight(110);
        cardToBuy.setFitWidth(60);
        cardToBuy.setX(500);
        cardToBuy.setY(300);

        //Karte die der Spieler auf den Müll legt
        ImageView cardToDelete = new ImageView();
        cardToDelete.setImage(image);
        cardToDelete.setFitHeight(110);
        cardToDelete.setFitWidth(60);
        cardToDelete.setX(150);
        cardToDelete.setY(538);

        //Fixpunkt, zeigt an wo das Deck beginnt
        Pane deck = new Pane();
        deck.setLayoutX(150);
        deck.setLayoutY(538);
        deck.setPrefSize(10, 10);
        deck.setStyle("-fx-background-color: black");

        //Fixpunkt, zeigt an wo der Ablagestapel beginnt
        Pane ablage = new Pane();
        ablage.setLayoutX(733);
        ablage.setLayoutY(538);
        ablage.setPrefSize(10, 10);
        ablage.setStyle("-fx-background-color: skyblue");

        //Fixpunkt, zeigt an wo die zu kaufende Karte beginnt
        Pane c = new Pane();
        c.setLayoutX(500);
        c.setLayoutY(300);
        c.setPrefSize(10, 10);
        c.setStyle("-fx-background-color: magenta");

        Pane bg = new Pane(cardToPlayByOpponent, cardToPlay, cardToBuy, cardToDelete, hand, actionZone, trash, deck, ablage, c);

        primaryStage.setScene(new Scene(bg, 1280, 750));
        primaryStage.show();

        //Beim Start werden die Karten zur Hand hinzugefügt
        AnimationManagement.addToHand(cardToPlay, 0, false);
        AnimationManagement.addToHand(cardToDelete, 1, false);

        //Wenn auf die Karte geklickt wird, spielt der Gegenspieler eine Karte aus
        cardToPlayByOpponent.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.opponentPlaysCard(cardToPlayByOpponent, 0);
        });

        //Wenn auf die Karte geklickt wird, spielt der Spieler sie aus
        cardToPlay.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.playCard(cardToPlay, 0);
        });

        //Wenn auf die Karte geklickt wird, wird sie vom Spieler gekauft
        cardToBuy.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            AnimationManagement.buyCard(cardToBuy);
        });

        //Wenn auf die Karte geklickt wird, wird sie auf den Müll gelegt
        cardToDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.deleteCard(cardToDelete);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
