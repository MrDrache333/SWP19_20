package de.uol.swp.client.game;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimationManagement {

    private static int handX = 284;
    private static int handY = 541;

    private static int deckX = 150;
    private static int deckY = 538;

    private static int ablageX = 733;
    private static int ablageY = 538;

    private static int buyableCardsX = 640;
    private static int buyableCardsY = 350;

    private static int actionZoneX = 356;
    private static int actionZoneY = 415;

    private static int actionZoneOpponentX = actionZoneX;
    private static int actionZoneOpponentY = 415;

    private static int trashX = 100;
    private static int trashY = 233;

    public static void PlayCard(Pane card, int count){
        Platform.runLater(() -> {
            Path path = new Path();
            double x = card.getLayoutX();
            double y = card.getLayoutY();
            path.getElements().add(new MoveTo(x, y));
            path.getElements().add(new ArcTo(30, 30, 0, actionZoneX + count*10, actionZoneY, true, false));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    public static void BuyCard(Pane card){
        Platform.runLater(() -> {
            Path path = new Path();
            path.getElements().add(new MoveTo(buyableCardsX, buyableCardsY));
            path.getElements().add(new ArcTo(30, 0, 0, ablageX, ablageY, true, false));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    public static void OpponentBuysCard(Pane card){
        Platform.runLater(() -> {
            Path path = new Path();
            path.getElements().add(new MoveTo(buyableCardsX, buyableCardsY));
            path.getElements().add(new ArcTo(30, 0, 0, 334, -100, true, false));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    public static void OpponentPlaysCard(Pane card, int count){
        Platform.runLater(() -> {
            Path path = new Path();
            double x = card.getLayoutX();
            double y = card.getLayoutY();
            path.getElements().add(new MoveTo(x, y));
            path.getElements().add(new ArcTo(30, 30, 0, actionZoneOpponentX + count*10, actionZoneOpponentY, true, false));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    public static void DeleteCard(Pane card){
        Platform.runLater(() -> {
            Path path = new Path();
            double x = card.getLayoutX();
            double y = card.getLayoutY();
            path.getElements().add(new MoveTo(x, y));
            path.getElements().add(new ArcTo(30, 30, 0, trashX, trashY, true, false));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    public static void addToHand(Pane[] hand, Pane card){

    }
}
