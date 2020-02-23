package de.uol.swp.client.game;
import javafx.animation.PathTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.List;

public class AnimationManagement {

    private static int handX = 284;
    private static int handY = 541;

    private static int deckX = 150;
    private static int deckY = 538;

    private static int ablageX = 733;
    private static int ablageY = 538;

    private static int actionZoneX = 356;
    private static int actionZoneY = 415;

    private static int actionZoneOpponentX = actionZoneX;
    private static int actionZoneOpponentY = 31;

    private static int trashX = 100;
    private static int trashY = 233;


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
     * Die neuen Koordinaten werden am Ende übernommen.
     *
     * @param card die zu bewegende Karte
     * @param EndPointX die X-Koordinate des Endpunktes
     * @param EndPointY die Y-Koordinate des Endpunktes
     */
    public static void createLineToPath(ImageView card, int EndPointX, int EndPointY){
        Path path = new Path();
        double x = card.getX();
        double y = card.getY();
        double w = card.getFitWidth()/2;
        double h = card.getFitHeight()/2;
        path.getElements().add(new MoveTo(x+w,y+h));
        path.getElements().add(new LineTo(EndPointX + w, EndPointY + h));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(card);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
        pathTransition.play();
        setNewCoordinates(card,pathTransition);
    }

    /**
     * Erstellt einen neuen gebogenen Pfad.
     * Die neuen Koordinaten werden am Ende übernommen.
     *
     * @param card die zu bewegende Karte
     * @param moveTo der Startpunkt
     * @param EndPointX die x-Koordinate des Endpunktes
     * @param EndPointY die y-Koordinate des Endpunktes
     * @param count gibt an, die wievielte Karte gespielt wird
     * @param largeArc gibt an, ob der große Bogen genommen werden soll
     * @author Anna
     * @since Sprint5
     */
    public static void createArcToPath(ImageView card, MoveTo moveTo, int EndPointX, int EndPointY, int count, boolean largeArc){
        Path path = new Path();
        double w = card.getFitWidth()/2;
        double h = card.getFitHeight()/2;
        path.getElements().add(moveTo);
        path.getElements().add(new ArcTo(30, 30, 0, EndPointX+w+count*w, EndPointY+h, largeArc, !largeArc));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(card);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
        pathTransition.play();
        setNewCoordinates(card,pathTransition);
    }

    /**
     * Wenn der Spieler eine Karte ausspielt, wird sie in seine Aktionszone bewegt.
     *
     * @param card die Karte
     * @param count gibt an, die wievielte Karte eines Zuges gerade gespielt wird.
     * @author Anna
     * @since Sprint5
     */
    public static void playCard(ImageView card, int count){
        createArcToPath(card, keepPosition(card), actionZoneX, actionZoneY, count, true);
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
        createArcToPath(card, new MoveTo(500, 0), actionZoneOpponentX, actionZoneOpponentY, count, false);
    }

    /**
     * Wenn der Spieler eine Karte kauft wird sie auf seinen Ablagestapel bewegt.
     * Die neuen Koordinaten werden übernommen.
     *
     * @param card die Karte
     * @author Anna
     * @since Sprint5
     */
    public static void buyCard(ImageView card){
        createLineToPath(card, ablageX, ablageY);
    }

    /**
     * Wenn ein Gegenspieler eine Karte kauft, wird sie aus dem Spielfeld bewegt.
     *
     * @param card die Karte
     * @author Anna
     * @since Sprint5
     */
    public static void opponentBuysCard(ImageView card){
        createLineToPath(card, 334, -300);
    }

    /**
     * Die Karte wird auf den Müllstapel gelegt.
     *
     * @param card die Karte
     * @author Anna
     * @since Sprint5
     */
    public static void deleteCard(ImageView card){
        createArcToPath(card, keepPosition(card), trashX, trashY, 0, true);
    }

    /**
     * Die übergebene Karte wird zur Hand des Spieler hinzugefügt.
     * Wenn mehr als 5 Karten auf der Hand liegen, werden die Abstände verringert.
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
        if (count == 0){
            path.getElements().add(new LineTo(handX + w, handY + h));
        }
        if (moreThan5 && count > 0){
            path.getElements().add(new LineTo(handX + w + count * w + 5, handY + h));
        }
        if (!moreThan5 && count > 0) {
            path.getElements().add(new LineTo(handX + w + count * w*2 + 5, handY + h));
        }
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(card);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
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
        for (int i = 0; i < cards.size(); i++){
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
        for (int i = 0; i < cards.size(); i++){
            addToHand(cards.get(i), i, false);
        }
    }

    /**
     * Die Methode setzt die neuen Koordinaten der Karte, nachdem die Bewegung beendet wurde
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
