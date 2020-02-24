package de.uol.swp.client.game;

import de.uol.swp.client.game.AnimationManagement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

        //Karte die der Spieler ausspielt
        ImageView c1 = new ImageView();
        c1.setImage(image);
        c1.setFitHeight(110);
        c1.setFitWidth(60);
        c1.setX(150);
        c1.setY(538);
        //Karte die der Spieler ausspielt
        ImageView c2 = new ImageView();
        c2.setImage(image);
        c2.setFitHeight(110);
        c2.setFitWidth(60);
        c2.setX(150);
        c2.setY(538);
        //Karte die der Spieler ausspielt
        ImageView c3 = new ImageView();
        c3.setImage(image);
        c3.setFitHeight(110);
        c3.setFitWidth(60);
        c3.setX(150);
        c3.setY(538);
        //Karte die der Spieler ausspielt
        ImageView c4 = new ImageView();
        c4.setImage(image);
        c4.setFitHeight(110);
        c4.setFitWidth(60);
        c4.setX(150);
        c4.setY(538);

        Pane bg = new Pane(c1, c2, c3, c4, cardToPlayByOpponent, cardToPlay, cardToBuy, cardToDelete, hand, actionZone, trash, deck, ablage, c);

        primaryStage.setScene(new Scene(bg, 1280, 750));
        primaryStage.show();

        //Beim Start werden die Karten zur Hand hinzugefügt
        AnimationManagement.addToHand(cardToPlay, 0, false);
        AnimationManagement.addToHand(cardToDelete, 1, false);
        AnimationManagement.addToHand(c1, 2, false);
        AnimationManagement.addToHand(c2, 3, false);
        AnimationManagement.addToHand(c3, 4, false);
        AnimationManagement.addToHand(c4, 5, false);
        List<ImageView> cards = new ArrayList<>();
        cards.add(cardToPlay);
        cards.add(cardToDelete);
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);


        //Wenn auf die Karte geklickt wird, spielt der Gegenspieler eine Karte aus
        cardToPlayByOpponent.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.opponentPlaysCard(cardToPlayByOpponent, 0);
        });

        //Wenn auf die Karte geklickt wird, spielt der Spieler sie aus
        cardToPlay.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.playCard(cardToPlay, 0);
            if (cards.contains(cardToPlay)){
                cards.remove(cardToPlay);
                AnimationManagement.refactorHand(cards, false);
            }
        });

        //Wenn auf die Karte geklickt wird, wird sie vom Spieler gekauft
        cardToBuy.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            AnimationManagement.buyCard(cardToBuy);
        });

        //Wenn auf die Karte geklickt wird, wird sie auf den Müll gelegt
        cardToDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.deleteCard(cardToDelete);
            if (cards.contains(cardToDelete)) {
                cards.remove(cardToDelete);
                AnimationManagement.refactorHand(cards, false);
            }
        });

        //Abstände der Karten auf der Hand werden verringert
        c1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.refactorHand(cards, true);
        });

        //Abstände der Karten auf der Hand werden vergrößert
        c2.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            AnimationManagement.refactorHand(cards, false);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
