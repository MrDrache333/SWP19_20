package de.uol.swp.client.game;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    ImageView trophy;

    @FXML
    Pane anchorPane;
    @FXML
    ImageView background;
    @FXML
    ImageView bannerDrawing;
    @FXML
    Text bannerTextSmall;
    @FXML
    Text bannerText;
    private GameManagement gameManagement;

    User loggedInUser;
    String winner;

    /**
     * Initialisiert einen neuen GameOverViewPresenter.
     *
     * @param gameManagement das GameManagement
     * @param loggedInUser   der aktuelle User
     * @param winner         der Gewinner des Spiels
     */
    public GameOverViewPresenter(GameManagement gameManagement, User loggedInUser, String winner) {
        this.gameManagement = gameManagement;
        this.loggedInUser = loggedInUser;
        this.winner = winner;
    }

    /**
     * Initilaisieren.
     * Je nachdem ob gewonnen oder verliren wurde, werden verschiedene Elemente zur GameOverView hinzugefügt.
     */
    @FXML
    public void initialize() {
        if (winner.equals(loggedInUser.getUsername())) {
            background.setImage(new Image("file:Client/src/main/resources/images/burgHohenzollern.jpg"));
            bannerText.setText("Du hast gewonnen!");
            ImageView confettiShot = new ImageView(new Image("file:Client/src/main/resources/images/confettiShot.gif"));
            confettiShot.setLayoutX(30);
            confettiShot.setLayoutY(5);
            confettiShot.setPreserveRatio(true);
            confettiShot.setFitWidth(300);
            anchorPane.getChildren().add(confettiShot);
            bannerDrawing.toFront();
            bannerText.toFront();
            bannerTextSmall.setText("Herzlichen Glückwunsch!");
            bannerTextSmall.toFront();
            trophy.toFront();
            ImageView confettiGoldSlow = new ImageView(new Image("file:Client/src/main/resources/images/confettiGoldSlow.gif"));
            confettiGoldSlow.setPreserveRatio(true);
            confettiGoldSlow.setFitWidth(420);
            anchorPane.getChildren().add(confettiGoldSlow);
        } else {
            trophy.setVisible(false);
            background.setImage(new Image("file:Client/src/main/resources/images/burgHohenzollernDarker.jpg"));
            bannerText.setText(winner + " hat gewonnen!");
            bannerTextSmall.setText("Vielleicht klappt's beim nächsten Mal.");
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
        closeWindow();
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
        gameManagement.getLobbyService().leaveLobby(gameManagement.getID(), new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()));
        closeWindow();
    }

    /**
     * Schließt das Spielergebnis-Fenster.
     *
     * @author Anna
     * @since Sprint6
     */
    public void closeWindow() {
        gameManagement.closeGameOverView();
    }
}
