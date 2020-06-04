package de.uol.swp.client.game;

import de.uol.swp.client.game.container.GeneralLayoutContainer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zum Testen der Kartenanimationen.
 */

public class AnimationTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/images/karte_gross.png");
        Image image = new Image(input);

        //Karte die der Gegenspieler spielt
        ImageView cardToPlayByOpponent = new ImageView();
        cardToPlayByOpponent.setImage(image);
        cardToPlayByOpponent.setFitHeight(110);
        cardToPlayByOpponent.setFitWidth(60);

        //Fixpunkt, zeigt an wo die Hand des Spielers beginnt
        Pane hand = new Pane();
        hand.setLayoutX(284);
        hand.setLayoutY(598);
        hand.setPrefSize(10, 10);
        hand.setStyle("-fx-background-color: blue");

        //Fixpunkt, zeigt an wo die Aktionszone des Spielers beginnt
        Pane actionZone = new Pane();
        actionZone.setLayoutX(370);
        actionZone.setLayoutY(421);
        actionZone.setPrefSize(10, 10);
        actionZone.setStyle("-fx-background-color: green");

        //Karte die der Spieler ausspielt
        ImageView cardToPlay = new ImageView();
        cardToPlay.setImage(image);
        cardToPlay.setFitHeight(110);
        cardToPlay.setFitWidth(60);

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
        cardToBuy.setLayoutX(500);
        cardToBuy.setLayoutY(300);

        //Karte die der Spieler auf den Müll legt
        ImageView cardToDelete = new ImageView();
        cardToDelete.setImage(image);
        cardToDelete.setFitHeight(110);
        cardToDelete.setFitWidth(60);

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

        //Karte die der Spieler auf der Hand hat
        ImageView c1 = new ImageView();
        c1.setImage(image);
        c1.setFitHeight(110);
        c1.setFitWidth(60);

        //Karte die der Spieler auf der Hand hat
        ImageView c2 = new ImageView();
        c2.setImage(image);
        c2.setFitHeight(110);
        c2.setFitWidth(60);

        //Karte die der Spieler auf der Hand hat
        ImageView c3 = new ImageView();
        c3.setImage(image);
        c3.setFitHeight(110);
        c3.setFitWidth(60);

        //Karte die der Spieler auf der Hand hat
        ImageView c4 = new ImageView();
        c4.setImage(image);
        c4.setFitHeight(110);
        c4.setFitWidth(60);

        StackPane deckPane = new StackPane();
        deckPane.setLayoutX(160);
        deckPane.setLayoutY(598);
        deckPane.setPrefWidth(120);
        deckPane.setPrefHeight(130);
        deckPane.setAlignment(Pos.CENTER);
        deckPane.setStyle("-fx-background-color: lightblue");

        Pane bg = new Pane(cardToPlayByOpponent, cardToBuy, deckPane, hand, actionZone, trash, deck, ablage, c);

        primaryStage.setScene(new Scene(bg, 1280, 750));
        primaryStage.show();

        GeneralLayoutContainer handcards = new GeneralLayoutContainer();
        handcards.setLayoutX(284);
        handcards.setLayoutY(598);
        handcards.setPrefHeight(130);
        handcards.setPrefWidth(430);
        handcards.setStyle("-fx-background-color: pink");

        bg.getChildren().add(handcards);
        handcards.toBack();

        List<ImageView> cards = new ArrayList<>();
        cards.add(cardToPlay);
        cards.add(cardToDelete);
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);

        for (ImageView card : cards) {
            deckPane.getChildren().add(card);
        }

        for (int i = 0; i < cards.size(); i++) {
            AnimationManagement.addToHand(cards.get(i), i);
            deckPane.getChildren().remove(cards.get(i));
            handcards.getChildren().add(cards.get(i));
        }


        //Wenn auf die Karte geklickt wird, spielt der Gegenspieler eine Karte aus
        cardToPlayByOpponent.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.opponentPlaysCard(cardToPlayByOpponent, 0);
        });

        //Wenn auf die Karte geklickt wird, spielt der Spieler sie aus

        GeneralLayoutContainer pclc = new GeneralLayoutContainer(700, 500, 100, 200, "My.PCLC");

        cardToPlay.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.playCard(cardToPlay, 0, pclc );
            if (cards.contains(cardToPlay)) {
                cards.remove(cardToPlay);
                handcards.getChildren().remove(cardToPlay);
                bg.getChildren().add(cardToPlay);
            }
        });

        //Wenn auf die Karte geklickt wird, wird sie vom Spieler gekauft
        cardToBuy.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.buyCard(cardToBuy);
        });

        //Wenn auf die Karte geklickt wird, wird sie auf den Müll gelegt
        cardToDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            AnimationManagement.deleteCard(cardToDelete);
            if (cards.contains(cardToDelete)) {
                cards.remove(cardToDelete);
                handcards.getChildren().remove(cardToDelete);
                bg.getChildren().add(cardToDelete);
            }
        });
    }
}
