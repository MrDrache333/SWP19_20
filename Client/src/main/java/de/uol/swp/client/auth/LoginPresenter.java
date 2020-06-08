package de.uol.swp.client.auth;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.register.event.ShowRegistrationViewEvent;
import de.uol.swp.client.sound.SoundMediaPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML
    private ImageView soundIcon;

    public LoginPresenter() {
    }

    /**
     * Der SoundMediaPlayer wird initialisiert.
     *
     * @author Keno O
     * @since Sprint 4
     */
    @FXML
    private void initialize() {
        LoginButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
        registerButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
        forgotPasswordButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
        soundIcon.setOnMouseClicked(event -> {
            SoundMediaPlayer.setSound(!SoundMediaPlayer.isSoundEnabled());
            soundIcon.setImage(new Image(new File(getClass().getResource(SoundMediaPlayer.isSoundEnabled() ? "/images/sound_on_icon.png" : "/images/sound_off_icon.png").toExternalForm().replace("file:", "")).toURI().toString()));
        });
    }

    /**
     * Beim Drücken des Login-Buttons werden die eingegebenen Daten zum Server geschickt
     *
     * @param event Das Event, dass die Methode Aufruft
     * @author Keno O
     * @since Sprint 4
     */
    @FXML
    private void onLoginButtonPressed(ActionEvent event) {
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Pressed, SoundMediaPlayer.Type.Sound).play();
        userService.login(userField.getText(), passwordField.getText());
    }

    /**
     * Beim Drücken des Registrieren-Buttons wird das Fenster zum Registrieren geöffnet
     *
     * @param event Das Event, dass die Methode aufruft
     * @author Keno O
     * @since Sprint 4
     */
    @FXML
    private void onRegisterButtonPressed(ActionEvent event) {
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Pressed, SoundMediaPlayer.Type.Sound).play();
        eventBus.post(showRegViewMessage);
    }
}
