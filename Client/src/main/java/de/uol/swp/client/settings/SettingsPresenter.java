package de.uol.swp.client.settings;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.Notifyer;
import de.uol.swp.client.SceneManager;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.settings.event.CloseSettingsEvent;
import de.uol.swp.client.settings.event.DeleteAccountEvent;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;


/**
 * Der Settings Presenter für den Typ.
 *
 * @author Anna
 * @since Sprint 4
 */
public class SettingsPresenter extends AbstractPresenter {

    /**
     * Die FXML Konstante.
     *
     * @author Anna
     * @since Sprint 4
     */
    public static final String fxml = "/fxml/SettingsView.fxml";
    public static final String css = "css/SettingsPresenter.css";
    private static final Logger LOG = LogManager.getLogger(SettingsPresenter.class);

    private User loggedInUser;
    private LobbyService lobbyService;
    private UserService userService;
    private Injector injector;
    private EventBus eventBus;

    @FXML
    private Button cancelButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField password2Field;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private ImageView chatMuteImage;
    @FXML
    private ToggleButton chatMuteToggleButton;

    /**
     * Konstruktor des SettingPresenters
     *
     * @param loggedInUser
     * @param lobbyService
     * @param userService
     * @param injector
     * @param eventBus
     * @author Julia, Keno S.
     * @since Sprint 4
     */
    public SettingsPresenter(User loggedInUser, LobbyService lobbyService, UserService userService, Injector injector, EventBus eventBus) {
        this.loggedInUser = loggedInUser;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.injector = injector;
        this.eventBus = eventBus;
    }

    /**
     * Überprüft die Benutzereingaben. Falls alle gültig sind, wird im UserService die Methode updateUser aufgerufen,
     * ansonsten wird eine entsprechende Fehlermeldung angezeigt
     *
     * @param event
     * @author Julia
     * @since Sprint 4
     */
    @FXML
    public void onSaveButtonPressed(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String password2 = password2Field.getText();
        String currentPassword = currentPasswordField.getText();

        // Keine Eingaben vom User -> Fenster schließen

        if (Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(email) && Strings.isNullOrEmpty(password) && Strings.isNullOrEmpty(password2)) {
            eventBus.post(new CloseSettingsEvent());
            clearAll();
        } else if (!password.equals(password2)) {
            SceneManager.showAlert(Alert.AlertType.ERROR, "Die Passwörter stimmen nicht überein", "Fehler");
            passwordField.clear();
            password2Field.clear();
            passwordField.requestFocus();
        } else if (!Strings.isNullOrEmpty(password) && password.contains(" ")) {
            SceneManager.showAlert(Alert.AlertType.ERROR, "Das Passwort darf keine Leerzeichen enthalten", "Fehler");
            passwordField.clear();
            password2Field.clear();
            passwordField.requestFocus();
        } else if (!Strings.isNullOrEmpty(email) && !Pattern.matches("(?:[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])\")@(?:(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-][a-z0-9])?|[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+", email)) {
            SceneManager.showAlert(Alert.AlertType.ERROR, email + " ist keine gültige E-Mail-Adresse", "Fehler");
            emailField.clear();
            emailField.requestFocus();
        } else if (Strings.isNullOrEmpty(currentPassword)) {
            SceneManager.showAlert(Alert.AlertType.ERROR, "Gib dein aktuelles Passwort ein", "Fehler");
            currentPasswordField.clear();
            currentPasswordField.requestFocus();
        } else {
            //Wenn Felder leer sind, Daten vom loggedInUser übernehmen
            if (Strings.isNullOrEmpty(username)) {
                username = loggedInUser.getUsername();
            }
            if (Strings.isNullOrEmpty(email)) {
                email = loggedInUser.getEMail();
            }
            if (Strings.isNullOrEmpty(password)) {
                password = currentPassword;
            }
            userService.updateUser(new UserDTO(username, password, email), loggedInUser, currentPassword);
            clearAll();
        }
    }

    /**
     * Postet auf den EventBus das Accountlöschung-Event
     *
     * @param actionEvent
     * @author Julia
     * @since Sprint 4
     */
    @FXML
    public void onDeleteAccountButtonPressed(ActionEvent actionEvent) {
        eventBus.post(new DeleteAccountEvent(loggedInUser));
    }

    /**
     * Postet auf den EventBus das Schließe-Settings-Event
     *
     * @param actionEvent
     * @author Julia
     * @since Sprint 4
     */
    @FXML
    public void onCancelButtonPressed(ActionEvent actionEvent) {
        eventBus.post(new CloseSettingsEvent());
        clearAll();
    }

    /**
     * Mutet alle Benachrichtigungen beim Aufruf
     *
     * @param actionEvent
     * @author Keno S.
     * @since Sprint 7
     */
    @FXML
    public void onChatMuteToggleButtonPressed(ActionEvent actionEvent) {
        Notifyer.setMuteState(chatMuteToggleButton.isSelected());
        if (chatMuteToggleButton.isSelected())
            chatMuteImage.setImage(new Image("images/chat_on_icon.png"));
        else
            chatMuteImage.setImage(new Image("images/chat_off_icon.png"));
    }

    /**
     * Aktualisiert den loggedInUser
     *
     * @param message
     * @author Julia
     * @since Sprint 4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
    }

    /**
     * Leert alle Felder (Benutzername, E-Mail und alle Passwortfelder)
     *
     * @author Julia
     * @since Sprint 4
     */
    private void clearAll() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        password2Field.clear();
        currentPasswordField.clear();
    }
}
