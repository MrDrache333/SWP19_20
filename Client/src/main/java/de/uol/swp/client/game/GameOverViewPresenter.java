package de.uol.swp.client.game;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Der Presenter für die Ansicht des Spielergebnisses.
 *
 * @author Anna
 * @since Sprint6
 */
public class GameOverViewPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/GameOverView.fxml";
    static final Logger LOG = LogManager.getLogger(GameOverViewPresenter.class);

    @FXML
    Pane anchorPane;
    @FXML
    HBox showResults;
    @FXML
    HBox bannerTextBox;
    @FXML
    ImageView background;
    @FXML
    ImageView trophy;
    @FXML
    ImageView bannerDrawing;
    @FXML
    Text bannerTextSmall;
    @FXML
    Text bannerText;

    private GameManagement gameManagement;

    private User loggedInUser;
    private String winner;
    private Map<String, Integer> results;

    /**
     * Initialisiert einen neuen GameOverViewPresenter.
     *
     * @param gameManagement das GameManagement
     * @param loggedInUser   der aktuelle User
     * @param winner         der Gewinner des Spiels
     */
    public GameOverViewPresenter(GameManagement gameManagement, User loggedInUser, String winner, Map<String, Integer> results) {
        this.gameManagement = gameManagement;
        this.loggedInUser = loggedInUser;
        this.winner = winner;
        this.results = results;
    }

    /**
     * Initilaisieren.
     * Die Spieler und ihre Punkte werden angezeigt.
     * Je nachdem ob gewonnen oder verliren wurde, werden verschiedene Elemente zur GameOverView hinzugefügt.
     *
     * @author Anna
     * @since Sprint6
     */
    @FXML
    public void initialize() {
        for (Map.Entry<String, Integer> e : results.entrySet()) {
            TextFlow flow = new TextFlow();
            flow.setMaxWidth(90);
            showResults.getChildren().add(flow);
            Text player = new Text(e.getKey() + ": " + e.getValue() + " SP");
            player.setStyle("-fx-font-size: 12");
            player.setFill(Paint.valueOf("gold"));
            flow.getChildren().add(player);
        }
        if (winner.equals(loggedInUser.getUsername())) {
            background.setImage(new Image("file:Client/src/main/resources/images/burgHohenzollern.jpg"));
            bannerText.setText("Du hast gewonnen!");
            bannerTextBox.setAlignment(Pos.CENTER_LEFT);
            bannerTextBox.setLayoutX(80);
            ImageView confettiShot = new ImageView(new Image("file:Client/src/main/resources/images/confettiShot.gif"));
            confettiShot.setLayoutX(30);
            confettiShot.setLayoutY(5);
            confettiShot.setPreserveRatio(true);
            confettiShot.setFitWidth(300);
            anchorPane.getChildren().add(confettiShot);
            bannerDrawing.toFront();
            bannerTextBox.toFront();
            bannerTextSmall.setText("Herzlichen Glückwunsch!");
            bannerTextSmall.setLayoutX(89);
            bannerTextSmall.toFront();
            trophy.setVisible(true);
            trophy.toFront();
            ImageView confettiGoldSlow = new ImageView(new Image("file:Client/src/main/resources/images/confettiGoldSlow.gif"));
            confettiGoldSlow.setPreserveRatio(true);
            confettiGoldSlow.setFitWidth(420);
            anchorPane.getChildren().add(confettiGoldSlow);
        } else {
            trophy.setVisible(false);
            background.setImage(new Image("file:Client/src/main/resources/images/burgHohenzollernDarker.jpg"));
            bannerText.setText(winner + " hat gewonnen!");
            bannerText.setLayoutX(62);
            bannerText.setTextAlignment(TextAlignment.CENTER);
            double scale = 160 / bannerText.getBoundsInLocal().getWidth();
            LOG.debug(bannerText.getBoundsInLocal().getWidth());
            if (scale < 1) {
                double fontSize = 18 * scale;
                bannerText.setStyle("-fx-font-size: " + fontSize);
            }
            bannerTextSmall.setText("Vielleicht klappt's beim nächsten Mal.");
            bannerTextSmall.setLayoutX(80);
            ImageView rain = new ImageView(new Image("file:Client/src/main/resources/images/rain.gif"));
            rain.setPreserveRatio(true);
            rain.setFitWidth(420);
            ImageView uTried = new ImageView(new Image("file:Client/src/main/resources/images/uTried.gif"));
            uTried.setLayoutX(65);
            uTried.setLayoutY(45);
            uTried.setPreserveRatio(true);
            uTried.setFitWidth(80);
            anchorPane.getChildren().add(uTried);
            anchorPane.getChildren().add(rain);
        }
    }

    /**
     * Wird aufgerufen, wenn der Spieler auf den "nochmal" Button drückt.
     * Spieler kehrt zurück zur Lobby.
     *
     * @param actionEvent das Ereignis der Aktion
     * @author Anna
     * @since Sprint6
     */
    @FXML
    public void onAgainButtonPressed(ActionEvent actionEvent) {
        LOG.debug("Player " + loggedInUser.getUsername() + " wants to play again.");
        gameManagement.showLobbyView();
        gameManagement.closeGameOverView();
    }

    /**
     * Wird aufgerufen, wenn der Spieler auf den "aufhören" Button drückt.
     * Spieler verlässt Lobby und gelangt zurück zum Hauptmenü.
     *
     * @param actionEvent das Ereignis der Aktion
     * @author Anna
     * @since Sprint6
     */
    @FXML
    public void onReturnButtonPressed(ActionEvent actionEvent) {
        LOG.debug("Player " + loggedInUser.getUsername() + " wants to return to the MainMenu.");
        gameManagement.close();
        gameManagement.closeGameOverViewAndLeaveLobby();
    }
}
