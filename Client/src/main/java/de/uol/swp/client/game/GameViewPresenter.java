package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.SceneManager;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.client.game.event.GameQuitEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.Optional;

public class GameViewPresenter extends AbstractPresenter {
    public static final String fxml = "/fxml/GameView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    private static final GameQuitEvent gameQuitMessage = new GameQuitEvent();
    private static SceneManager sceneManager;


    public void showAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            eventBus.post(gameQuitMessage);
        } else {

        }

    }


    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        userService.logout(loggedInUser);
    }

    @FXML
    public void onGiveUpButtonPressed(ActionEvent actionEvent) {

        showAlert(Alert.AlertType.CONFIRMATION, " ", "Wollen Sie wirklich Aufgeben?");

    }
}

