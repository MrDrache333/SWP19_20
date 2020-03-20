package de.uol.swp.client.game;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.user.User;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GameOverViewPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/GameOverView.fxml";

    @FXML
    Pane anchorPane;
    @FXML
    ImageView background;
    @FXML
    ImageView bannerDrawing;
    @FXML
    Text bannerText;

    User loggedInUser;
    String winner;

    public GameOverViewPresenter(User loggedInUser, String winner) {
        this.loggedInUser = loggedInUser;
        this.winner = winner;
    }

    @FXML
    public void initialize() {
        if (winner.equals(loggedInUser.getUsername())) {
            background.setImage(new Image("file:Client/src/main/resources/images/burgHohenzollern.jpg"));
            bannerText.setText("Du hast gewonnen!");
            ImageView confetti = new ImageView(new Image("file:Client/src/main/resources/images/confettiShot.gif"));
            confetti.setLayoutX(30);
            confetti.setLayoutY(5);
            confetti.setPreserveRatio(true);
            confetti.setFitWidth(300);
            confetti.toBack();
            anchorPane.getChildren().add(confetti);
            background.toBack();
        } else {
            background.setImage(new Image("file:Client/src/main/resources/images/burgHohenzollernDarker.jpg"));
            bannerText.setText(winner + " hat gewonnen!");
            ImageView rain = new ImageView(new Image("file:Client/src/main/resources/images/rain.gif"));
            rain.setPreserveRatio(true);
            rain.setFitWidth(420);
            ImageView uTried = new ImageView(new Image("file:Client/src/main/resources/images/uTried.gif"));
            uTried.setLayoutX(55);
            uTried.setLayoutY(35);
            uTried.setPreserveRatio(true);
            uTried.setFitWidth(80);
            anchorPane.getChildren().add(uTried);
            anchorPane.getChildren().add(rain);
        }
    }
/*
    @FXML
    public void onAgainButtonPressed(ActionEvent actionEvent) {
        System.out.println("again");

    }

    @FXML
    public void onReturnButtonPressed(ActionEvent actionEvent){
        System.out.println("return");
    }*/
}
