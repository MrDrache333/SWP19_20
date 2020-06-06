package de.uol.swp.client.game;

import de.uol.swp.client.game.container.GeneralLayoutContainer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;


public class AnimationManagement {

    private static final double HAND_X = 575;
    private static final double ABLAGE_X = 1050;
    private static final double ABLAGE_Y = 630;

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
     * @since Sprint 5
     */
    public static PathTransition createLineToPath(ImageView card, double EndPointX, double EndPointY) {
        double x = Math.max(card.getLayoutX(), card.getBoundsInParent().getMinX());
        double y = Math.max(card.getLayoutY(), card.getBoundsInParent().getMinY());
        double w = card.getFitWidth();
        double h = card.getFitHeight();
        double startPointX = -(EndPointX - sumParentX(card) - x);
        double startPointY = -(EndPointY - sumParentY(card) - y);
        Path path = new Path();
        path.getElements().add(new MoveTo(startPointX, startPointY));
        path.getElements().add(new LineTo(w / 2, h / 2));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(card);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
        card.toFront();
        pathTransition.play();
        setNewCoordinates(card, pathTransition);
        return pathTransition;
    }

    /**
     * Erstellt einen neuen gebogenen Pfad.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden am Ende übernommen.
     *
     * @param card      die zu bewegende Karte
     * @param EndPointX die x-Koordinate des Endpunktes
     * @param EndPointY die y-Koordinate des Endpunktes
     * @param count     gibt an, die wievielte Karte gespielt wird
     * @param largeArc  gibt an, ob der große Bogen genommen werden soll
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint 5
     */
    public static Boolean createArcToPath(ImageView card, double EndPointX, double EndPointY, int count, boolean largeArc) {
        double w = card.getFitWidth() / 2;
        double h = card.getFitHeight() / 2;
        Parent parent = card.getParent();
        double startPointX = parent.getLayoutX() + parent.getBoundsInLocal().getWidth() / 2 - w - EndPointX - w * 2 * count + 300;
        double startPointY = parent.getLayoutY() + parent.getBoundsInLocal().getHeight() / 2 - h - EndPointY;
        Path path = new Path();
        path.getElements().add(new MoveTo(startPointX, startPointY));
        path.getElements().add(new ArcTo(30, 20, 0, w, h, largeArc, !largeArc));
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

    /**
     * Wenn der Spieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt.
     *
     * @param card        die Karte
     * @param count       gibt an, die wievielte Karte eines Zuges gerade gespielt wird.
     * @param action_zone Die Zielzone wo die Karte sich hinbewegen soll
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint 5
     */
    public static Boolean playCard(ImageView card, int count, GeneralLayoutContainer action_zone) {
        return createArcToPath(card, action_zone.getLayoutX(), action_zone.getLayoutY(), count, true);
    }

    /**
     * Wenn der Spieler eine Karte kauft wird sie auf seinen Ablagestapel bewegt.
     * Die neuen Koordinaten werden übernommen.
     *
     * @param card die Karte
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint 5
     */
    public static PathTransition buyCard(ImageView card) {
        return createLineToPath(card, ABLAGE_X, ABLAGE_Y);
    }

    /**
     * Wenn die Clearphase ist gehen die Karten auf den Ablagestapel
     * Die neuen Koordinaten werden übernommen.
     *
     * @param card die Karte
     * @author Darian
     * @since Sprint 7
     */
    public static PathTransition clearCards(ImageView card, GeneralLayoutContainer discardPile) {
        return createLineToPath(card, discardPile.getLayoutX(), discardPile.getLayoutY());
    }

    /**
     * Die Karte wird gelöscht.
     *
     * @param card die Karte
     * @author Anna
     * @since Sprint 5
     */
    public static ParallelTransition deleteCard(ImageView card) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), card);
        fadeTransition.setFromValue(10);
        fadeTransition.setToValue(0.01);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(700), card);
        scaleTransition.setToX(0.01f);
        scaleTransition.setToY(0.01f);
        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
        parallelTransition.play();
        parallelTransition.setOnFinished(e -> {
            ((GeneralLayoutContainer) card.getParent()).getChildren().remove(card);
        });
        return parallelTransition;
    }

    /**
     * Die übergebene Karte wird zur Hand des Spieler hinzugefügt.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden übernommen.
     *
     * @param card  die Karte
     * @param count gibt an, die wievielte Karte hinzugefügt wird
     * @author Anna
     * @since Sprint 5
     */
    public static Boolean addToHand(ImageView card, int count) {
        Parent parent = card.getParent();
        double w = card.getFitWidth() / 2;
        double h = card.getFitHeight() / 2;
        double startPointX = parent.getLayoutX() + parent.getBoundsInLocal().getWidth() / 2 - w - HAND_X - w * 2 * count;
        parent.toBack();
        if (HAND_X + count * w != startPointX) {
            Path path = new Path();
            path.getElements().add(new MoveTo(startPointX, h));
            card.toFront();
            path.getElements().add(new LineTo(w, h));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(700));
            pathTransition.setNode(card);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
            setNewCoordinates(card, pathTransition);
            return true;
        }
        return false;
    }

    /**
     * Die Methode setzt die neuen Koordinaten der Karte, nachdem die Bewegung beendet wurde.
     *
     * @param card           die Karte
     * @param pathTransition die PathTransition(Bewegung)
     * @author Anna
     * @since Sprint 5
     */
    public static void setNewCoordinates(ImageView card, PathTransition pathTransition) {
        pathTransition.setOnFinished(actionEvent -> {
            card.setLayoutX(Math.round(card.getLayoutX() + card.getTranslateX()));
            card.setLayoutY(Math.round(card.getLayoutY() + card.getTranslateY()));
            card.setTranslateX(0);
            card.setTranslateY(0);
        });
    }

    /**
     * Die x-Koordinate wird berechnet, indem die x-Koordinaten aller Eltern der Karte aufsummiert werden.
     *
     * @param card  die Karte
     * @author Anna
     * @since Sprint 9
     */
    public static double sumParentX(ImageView card) {
        Parent parent = card.getParent();
        double sum = 0;
        while (parent.getParent() != null) {
            sum += Math.max(parent.getBoundsInParent().getMinX(), parent.getLayoutX());
            parent = parent.getParent();
        }
        return sum;
    }

    /**
     * Die y-Koordinate wird berechnet, indem die y-Koordinaten aller Eltern der Karte aufsummiert werden.
     *
     * @param card  die Karte
     * @author Anna
     * @since Sprint 9
     */
    public static double sumParentY(ImageView card) {
        Parent parent = card.getParent();
        double sum = 0;
        while (parent.getParent() != null) {
            sum += Math.max(parent.getBoundsInParent().getMinY(), parent.getLayoutY());
            parent = parent.getParent();
        }
        return sum;
    }
}
