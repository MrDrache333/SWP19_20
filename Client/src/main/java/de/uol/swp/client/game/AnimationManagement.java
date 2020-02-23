package de.uol.swp.client.game;
import javafx.animation.PathTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.List;

public class AnimationManagement {

    private static double handX = 284;
    private static double handY = 541;

    private static double deckX = 150;
    private static double deckY = 538;

    private static double ablageX = 733;
    private static double ablageY = 538;

    private static double actionZoneX = 356;
    private static double actionZoneY = 415;

    private static double actionZoneOpponentX = actionZoneX;
    private static double actionZoneOpponentY = 31;

    private static double trashX = 100;
    private static double trashY = 233;


    /**
     * Erstellt ein neues MoveTo Objekt für den Pfad, wobei die aktuellen Kooridnaten der Karte übernommen werden.
     *
     * @param card die Kare
     * @return MoveTo
     * @author Anna
     * @since Sprint5
     */
    public static MoveTo keepPosition(ImageView card){
        double x = card.getX();
        double y = card.getY();
        double w = card.getFitWidth()/2;
        double h = card.getFitHeight()/2;
        return new MoveTo(x+w,y+h);
    }

    /**
     * Erstellt einen neuen geradlinigen Pfad.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden am Ende übernommen.
     *
     * @param card die zu bewegende Karte
     * @param EndPointX die X-Koordinate des Endpunktes
     * @param EndPointY die Y-Koordinate des Endpunktes
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean createLineToPath(ImageView card, double EndPointX, double EndPointY){
        double x = card.getX();
        double y = card.getY();
        double w = card.getFitWidth()/2;
        double h = card.getFitHeight()/2;
        if (x != EndPointX && y != EndPointY){
            Path path = new Path();
            path.getElements().add(new MoveTo(x+w,y+h));
            path.getElements().add(new LineTo(EndPointX + w, EndPointY + h));
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
        else {
            return false;
        }
    }

    /**
     * Erstellt einen neuen gebogenen Pfad.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden am Ende übernommen.
     *
     * @param card die zu bewegende Karte
     * @param moveTo der Startpunkt
     * @param EndPointX die x-Koordinate des Endpunktes
     * @param EndPointY die y-Koordinate des Endpunktes
     * @param count gibt an, die wievielte Karte gespielt wird
     * @param largeArc gibt an, ob der große Bogen genommen werden soll
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean createArcToPath(ImageView card, MoveTo moveTo, double EndPointX, double EndPointY, int count, boolean largeArc){
        double w = card.getFitWidth()/2;
        double h = card.getFitHeight()/2;
        EndPointX = EndPointX+w+count*w;
        EndPointY = EndPointY+h;
        Path path = new Path();
        if (moveTo.getX() != EndPointX+w+count*w && moveTo.getY() != EndPointY) {
            path.getElements().add(moveTo);
            path.getElements().add(new ArcTo(30, 30, 0, EndPointX, EndPointY, largeArc, !largeArc));
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
        else {
            return false;
        }
    }

    /**
     * Wenn der Spieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt.
     *
     * @param card die Karte
     * @param count gibt an, die wievielte Karte eines Zuges gerade gespielt wird.
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean playCard(ImageView card, int count){
        return createArcToPath(card, keepPosition(card), actionZoneX, actionZoneY, count, true);
    }

    /**
     * Wenn ein Gegenspieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt.
     *
     * @param card die Karte
     * @param count gibt an, die wievielte Karte gerade gespielt wird
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean opponentPlaysCard(ImageView card, int count){
        return createArcToPath(card, new MoveTo(500, 0), actionZoneOpponentX, actionZoneOpponentY, count, false);
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
    public static Boolean buyCard(ImageView card){
        return createLineToPath(card, ablageX, ablageY);
    }

    /**
     * Wenn ein Gegenspieler eine Karte kauft, wird sie aus dem Spielfeld bewegt.
     *
     * @param card die Karte
     * @return boolean ob die Bewegung durchgeführt wurde
     * @author Anna
     * @since Sprint5
     */
    public static Boolean opponentBuysCard(ImageView card){
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
    public static Boolean deleteCard(ImageView card){
        return createArcToPath(card, keepPosition(card), trashX, trashY, 0, true);
    }

    /**
     * Die übergebene Karte wird zur Hand des Spieler hinzugefügt.
     * Wenn mehr als 5 Karten auf der Hand liegen, werden die Abstände verringert.
     * Die bewegte Karte wird dabei in den Vordergrund gerückt.
     * Die neuen Koordinaten werden übernommen.
     *
     * @param card die Karte
     * @param count gibt an, die wievielte Karte hinzugefügt wird
     * @param moreThan5 gibt an, ob schon 5 oder mehr Karten bereits auf der Hand liegen
     * @author Anna
     * @since Sprint5
     */
    public static void addToHand(ImageView card, int count, boolean moreThan5){
        double xValue = card.getX();
        double yValue = card.getY();
        double w = card.getFitWidth()/2;
        double h = card.getFitHeight()/2;
        Path path = new Path();
        path.getElements().add(new MoveTo(xValue + w, yValue + h));
        if (moreThan5){
            path.getElements().add(new LineTo(handX + w + count * w, handY + h));
        }
        else {
            path.getElements().add(new LineTo(handX + w + count * w*2 + 5*count, handY + h));
        }
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(card);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
        card.toFront();
        pathTransition.play();
        setNewCoordinates(card, pathTransition);
    }

    /**
     * Die Abstände zwischen den Karten werden verrringert.
     *
     * @param cards die Karten auf der Hand
     * @author Anna
     * @since Sprint5
     */
    public static void refactorHandToSmallerSpaces(List<ImageView> cards){
        for (int i = 1; i < cards.size(); i++){
            addToHand(cards.get(i), i, true);
        }
    }

    /**
     * Die Abstände aller Karten werden wieder vergrößert.
     *
     * @param cards die Karten auf der Hand
     * @author Anna
     * @since Sprint5
     */
    public static void refactorHandToBiggerSpaces(List<ImageView> cards){
        for (int i = 1; i < cards.size(); i++){
            addToHand(cards.get(i), i, false);
        }
    }

    /**
     * Die Methode setzt die neuen Koordinaten der Karte, nachdem die Bewegung beendet wurde.
     *
     * @param card die Karte
     * @param pathTransition die PathTransition(Bewegung)
     * @author Anna
     * @since Sprint5
     */
    public static void setNewCoordinates(ImageView card, PathTransition pathTransition){
        pathTransition.setOnFinished(actionEvent -> {
            card.setX(card.getX() + card.getTranslateX());
            card.setY(card.getY() + card.getTranslateY());
            card.setTranslateX(0);
            card.setTranslateY(0);
        });
    }
}
