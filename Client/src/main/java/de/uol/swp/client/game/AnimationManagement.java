package de.uol.swp.client.game;

import javafx.animation.PathTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.List;

public class AnimationManagement {

    private static final double HAND_X = 224;
    private static final double HAND_Y = 598;

    private static final double ABLAGE_X = 733;
    private static final double ABLAGE_Y = 538;

    private static final double ACTION_ZONE_X = 356;
    private static final double ACTION_ZONE_Y = 415;

    private static final double ACTION_ZONE_OPPONENT_X = ACTION_ZONE_X;
    private static final double ACTION_ZONE_OPPONENT_Y = 31;

    private static final double TRASH_X = 100;
    private static final double TRASH_Y = 233;


    /**
     * Erstellt ein neues MoveTo Objekt für den Pfad, wobei die aktuellen Kooridnaten der Karte übernommen werden.
     *
     * @param card die Kare
     * @return MoveTo
     * @author Anna
     * @since Sprint5
     */
    public static MoveTo keepPosition(ImageView card) {
        double w = card.getFitWidth() / 2;
        double h = card.getFitHeight() / 2;
        return new MoveTo(w, h);
    }

    /**
     * Erstellt einen neuen geradlinigen Pfad.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden am Ende übernommen.
     *
     * @param card      die zu bewegende Karte
     * @param EndPointX die X-Koordinate des Endpunktes
     * @param EndPointY die Y-Koordinate des Endpunktes
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean createLineToPath(ImageView card, double EndPointX, double EndPointY) {
        double x = card.getLayoutX();
        double y = card.getLayoutY();
        double w = card.getFitWidth() / 2;
        double h = card.getFitHeight() / 2;
        if (x != EndPointX || y != EndPointY) {
            Path path = new Path();
            path.getElements().add(new MoveTo(w, h));
            path.getElements().add(new LineTo(EndPointX - x + w, EndPointY - y + h));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            card.toFront();
            pathTransition.play();
            setNewCoordinates(card, pathTransition);
            return true;
        }
        return false;
    }

    /**
     * Erstellt einen neuen gebogenen Pfad.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden am Ende übernommen.
     *
     * @param card      die zu bewegende Karte
     * @param moveTo    der Startpunkt
     * @param EndPointX die x-Koordinate des Endpunktes
     * @param EndPointY die y-Koordinate des Endpunktes
     * @param count     gibt an, die wievielte Karte gespielt wird
     * @param largeArc  gibt an, ob der große Bogen genommen werden soll
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean createArcToPath(ImageView card, MoveTo moveTo, double EndPointX, double EndPointY, int count, boolean largeArc) {
        double x = card.getLayoutX();
        double y = card.getLayoutY();
        double w = card.getFitWidth() / 2;
        double h = card.getFitHeight() / 2;
        EndPointX = EndPointX + w + count * w;
        EndPointY = EndPointY + h;
        Path path = new Path();
        if (moveTo.getX() != EndPointX || moveTo.getY() != EndPointY) {
            path.getElements().add(moveTo);
            path.getElements().add(new ArcTo(30, 30, 0, EndPointX - x, EndPointY - y, largeArc, !largeArc));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            card.toFront();
            pathTransition.play();
            setNewCoordinates(card, pathTransition);
            return true;
        }
        return false;
    }

    /**
     * Wenn der Spieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt.
     *
     * @param card  die Karte
     * @param count gibt an, die wievielte Karte eines Zuges gerade gespielt wird.
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean playCard(ImageView card, int count) {
        return createArcToPath(card, keepPosition(card), ACTION_ZONE_X, ACTION_ZONE_Y, count, true);
    }

    /**
     * Wenn ein Gegenspieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt.
     *
     * @param card  die Karte
     * @param count gibt an, die wievielte Karte gerade gespielt wird
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean opponentPlaysCard(ImageView card, int count) {
        return createArcToPath(card, new MoveTo(500 - card.getLayoutX(), -70 - card.getLayoutY()), ACTION_ZONE_OPPONENT_X, ACTION_ZONE_OPPONENT_Y, count, false);
    }

    /**
     * Wenn der Spieler eine Karte kauft wird sie auf seinen Ablagestapel bewegt.
     * Die neuen Koordinaten werden übernommen.
     *
     * @param card die Karte
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean buyCard(ImageView card) {
        return createLineToPath(card, ABLAGE_X, ABLAGE_Y);
    }

    /**
     * Wenn ein Gegenspieler eine Karte kauft, wird sie aus dem Spielfeld bewegt.
     *
     * @param card die Karte
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean opponentBuysCard(ImageView card) {
        return createLineToPath(card, 334, -300);
    }

    /**
     * Die Karte wird auf den Müllstapel gelegt.
     *
     * @param card die Karte
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean deleteCard(ImageView card) {
        return createArcToPath(card, keepPosition(card), TRASH_X, TRASH_Y, 0, true);
    }

    /**
     * Die übergebene Karte wird zur Hand des Spieler hinzugefügt.
     * Wenn mehr als 5 Karten auf der Hand liegen, werden die Abstände verringert.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden übernommen.
     *
     * @param card       die Karte
     * @param count      gibt an, die wievielte Karte hinzugefügt wird
     * @param smallSpace gibt an, ob ein kleinerer Abstand genommen werden soll
     * @author Anna
     * @since Sprint5
     */
    public static Boolean addToHand(ImageView card, int count, boolean smallSpace) {
        double xValue = card.getLayoutX();
        double yValue = card.getLayoutY();
        double w = card.getFitWidth() / 2;
        double h = card.getFitHeight() / 2;
        if ((smallSpace && HAND_X + count * w != xValue) || (!smallSpace && HAND_X + count * w * 2 + 5 * count != xValue)) {
            Path path = new Path();
            path.getElements().add(new MoveTo(w, h));
            if (smallSpace) {
                path.getElements().add(new LineTo(HAND_X - xValue + w + count * w, HAND_Y - yValue + h));
            } else {
                path.getElements().add(new LineTo(HAND_X - xValue + w + count * w * 2 + 5 * count, HAND_Y - yValue + h));
            }
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(600));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            card.toFront();
            pathTransition.play();
            setNewCoordinates(card, pathTransition);
            return true;
        }
        return false;
    }

    /**
     * Die Karten werden neu angeordnet.
     *
     * @param cards      die Karten auf der Hand
     * @param smallSpace gibt an, ob verkleinerte Abstäne benutzt werden sollen
     * @author Anna
     * @since Sprint5
     */
    public static void refactorHand(List<ImageView> cards, boolean smallSpace) {
        for (int i = 0; i < cards.size(); i++) {
            addToHand(cards.get(i), i, smallSpace);
        }
    }

    /**
     * Die Methode setzt die neuen Koordinaten der Karte, nachdem die Bewegung beendet wurde.
     *
     * @param card           die Karte
     * @param pathTransition die PathTransition(Bewegung)
     * @author Anna
     * @since Sprint5
     */
    public static void setNewCoordinates(ImageView card, PathTransition pathTransition) {
        pathTransition.setOnFinished(actionEvent -> {
            card.setLayoutX(Math.round(card.getLayoutX() + card.getTranslateX()));
            card.setLayoutY(Math.round(card.getLayoutY() + card.getTranslateY()));
            card.setTranslateX(0);
            card.setTranslateY(0);
        });
    }
}
