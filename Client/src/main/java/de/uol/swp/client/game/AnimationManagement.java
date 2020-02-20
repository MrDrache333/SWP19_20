package de.uol.swp.client.game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class AnimationManagement {

    private static int handX = 284;
    private static int handY = 541;

    private static int deckX = 150;
    private static int deckY = 538;

    private static double ablageX = 733;
    private static double ablageY = 538;

    private static int buyableCardsX = 640;
    private static int buyableCardsY = 350;

    private static int actionZoneX = 356;
    private static int actionZoneY = 415;

    private static int actionZoneOpponentX = actionZoneX;
    private static int actionZoneOpponentY = 31;

    private static int trashX = 100;
    private static int trashY = 233;

    /**
     * Wenn der Spieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt
     *
     * @param card die Karte
     * @param count gibt an, die wievielte Karte eines Zuges gerade gespielt wird.
     * @author Anna
     * @since Sprint5
     */
    public static void playCard(ImageView card, int count){
        Platform.runLater(() -> {
            Path path = new Path();
            double x = card.getX();
            double y = card.getY();
            double w = card.getFitWidth()/2;
            double h = card.getFitHeight()/2;
            path.getElements().add(new MoveTo(x,y));
            path.getElements().add(new ArcTo(30, 30, 0, actionZoneX+w + count*10, actionZoneY+h, true, false));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    /**
     * Wenn der Spieler eine Karte kauft wird sie auf seinen Ablagestapel bewegt.
     *
     * @param card die Karte
     * @author Anna
     * @since Sprint5
     */
    public static void buyCard(ImageView card){
        Platform.runLater(() -> {
            double xValue = card.getX();
            double yValue = card.getY();
            KeyFrame f1 = new KeyFrame(Duration.seconds(1),
                    new KeyValue(card.translateXProperty(), ablageX-xValue));
            KeyFrame f2 = new KeyFrame(Duration.seconds(1),
                    new KeyValue(card.translateYProperty(), ablageY-yValue));
            Timeline tl = new Timeline();
            tl.getKeyFrames().addAll(f1, f2);
            tl.setCycleCount(1);
            tl.setAutoReverse(true);
            tl.play();
        });
    }

    /**
     * Wenn ein Gegenspieler eine Karte kauft, wird sie aus dem Spielfeld bewegt.
     *
     * @param card die Karte
     * @author Anna
     * @since Sprint5
     */
    public static void opponentBuysCard(ImageView card){
        Platform.runLater(() -> {
            double xValue = card.getX();
            double yValue = card.getY();
            KeyFrame f1 = new KeyFrame(Duration.seconds(1),
                    new KeyValue(card.translateXProperty(), 334-xValue));
            KeyFrame f2 = new KeyFrame(Duration.seconds(1),
                    new KeyValue(card.translateYProperty(), -100-yValue));
            Timeline tl = new Timeline();
            tl.getKeyFrames().addAll(f1, f2);
            tl.setCycleCount(1);
            tl.play();
        });
    }

    /**
     * Wenn ein Gegenspieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt.
     *
     * @param card die Karte
     * @param count gibt an, die wievielte Karte gerade gespielt wird
     * @author Anna
     * @since Sprint5
     */
    public static void opponentPlaysCard(ImageView card, int count){
        Platform.runLater(() -> {
            Path path = new Path();
            double w = card.getFitWidth()/2;
            double h = card.getFitHeight()/2;
            path.getElements().add(new MoveTo(500, 0));
            path.getElements().add(new ArcTo(30, 30, 0, actionZoneOpponentX+w+count*10, actionZoneOpponentY+h, false, true));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    /**
     * Die Karte wird auf den Müllstapel gelegt.
     *
     * @param card die Karte
     * @author Anna
     * @since Sprint5
     */
    public static void deleteCard(ImageView card){
        Platform.runLater(() -> {
            Path path = new Path();
            double x = card.getX();
            double y = card.getY();
            double w = card.getFitWidth()/2;
            double h = card.getFitHeight()/2;
            path.getElements().add(new MoveTo(x,y));
            path.getElements().add(new ArcTo(30, 30, 0, trashX+w, trashY+h, true, false));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
        });
    }

    public static void addToHand(Pane[] hand, Pane card){
        //TODO
    }
}
