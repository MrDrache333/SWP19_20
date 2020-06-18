package de.uol.swp.client.register;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.register.event.RegistrationCanceledEvent;
import de.uol.swp.client.register.event.RegistrationErrorEvent;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.common.user.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

/**
 * Die Presenter-Klasse des Registrierungsfensters.
 */
@SuppressWarnings("UnstableApiUsage")
public class RegistrationPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/RegistrationView.fxml";

    private static final RegistrationCanceledEvent registrationCanceledEvent = new RegistrationCanceledEvent();

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField1;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private Button registerButton;

    @FXML
    private Hyperlink cancelButton;

    @FXML
    private TextField mailField;

    public RegistrationPresenter() {
    }

    @Inject
    public RegistrationPresenter(EventBus eventBus) {
        setEventBus(eventBus);
    }

    /**
     * Initialisieren der Buttonsounds.
     *
     * @author Keno O
     */
    @FXML
    private void initialize() {
        registerButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
        cancelButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
        SoundMediaPlayer.setSound(true);
    }

    //--------------------------------------
    // FXML METHODS
    //--------------------------------------
    @FXML
    void onCancelButtonPressed(ActionEvent event) {
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Pressed, SoundMediaPlayer.Type.Sound).play();
        eventBus.post(registrationCanceledEvent);
    }

    /**
     * Logik des Registrierungsfensters
     * Prüft auch, ob die Felder des Registrierungsformulars richtig ausgefüllt wurden
     * Dazu gehört: Leere Felder, ungültige Email
     *
     * @param event Referenz auf das ActionEvent-Objekt
     * @author Timo Siems, Keno S, Keno O
     * @since Sprint 1
     */
    @FXML
    void onRegisterButtonPressed(ActionEvent event) {
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Pressed, SoundMediaPlayer.Type.Sound).play();
        if (Strings.isNullOrEmpty(loginField.getText())) {
            eventBus.post(new RegistrationErrorEvent("Der Benutzername darf nicht leer sein!"));
        } else if (!passwordField1.getText().equals(passwordField2.getText())) {
            eventBus.post(new RegistrationErrorEvent("Die Passwörter stimmen nicht überein!"));
        } else if (Strings.isNullOrEmpty(passwordField1.getText())) {
            eventBus.post(new RegistrationErrorEvent("Das Passwortfeld darf nicht leer sein!"));
        } else if (Strings.isNullOrEmpty(mailField.getText())) {
            eventBus.post(new RegistrationErrorEvent("Das E-Mailfeld darf nicht leer sein!"));
        } else if (!(Pattern.matches("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", mailField.getText()))) {
            eventBus.post(new RegistrationErrorEvent("Ungültige E-Mail-Adresse!"));
        } else {
            userService.createUser(new UserDTO(loginField.getText(), passwordField1.getText(), mailField.getText()));
        }
    }
}
