package de.uol.swp.client.auth;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.MediaPlayer;
import de.uol.swp.client.register.event.ShowRegistrationViewEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class LoginPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LoginView.fxml";
    public static final String css = "css/LoginView.css";
    private static final Logger LOG = LogManager.getLogger(LoginPresenter.class);
    private static final ShowRegistrationViewEvent showRegViewMessage = new ShowRegistrationViewEvent();

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button LoginButton;
    @FXML
    private Hyperlink registerButton, forgotPasswordButton;

    public LoginPresenter() {
    }

    @FXML
    private void initialize() {
        LoginButton.setOnMouseEntered(event -> {
            new MediaPlayer(new File("/sounds/button_mouseover.wav"), MediaPlayer.Type.Sound).play();
        });
        registerButton.setOnMouseEntered(event -> {
            new MediaPlayer(new File("/sounds/button_mouseover.wav"), MediaPlayer.Type.Sound).play();
        });
        forgotPasswordButton.setOnMouseEntered(event -> {
            new MediaPlayer(new File("/sounds/button_mouseover.wav"), MediaPlayer.Type.Sound).play();
        });

    }

    @FXML
    private void onLoginButtonPressed(ActionEvent event) {
        new MediaPlayer(new File("/sounds/button_pressed.wav"), MediaPlayer.Type.Sound).play();
        userService.login(userField.getText(), passwordField.getText());
    }

    @FXML
    private void onRegisterButtonPressed(ActionEvent event) {
        new MediaPlayer(new File("/sounds/button_pressed.wav"), MediaPlayer.Type.Sound).play();
        eventBus.post(showRegViewMessage);
    }
}
