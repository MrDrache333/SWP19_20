package de.uol.swp.client.settings;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.settings.event.CloseSettingsEvent;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

import static de.uol.swp.client.main.MainMenuPresenter.showAlert;

/**
 * The type Settings presenter.
 */
public class SettingsPresenter extends AbstractPresenter {

    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/SettingsView.fxml";
    public static final String css = "css/SettingsPresenter.css";
    private static final Logger LOG = LogManager.getLogger(SettingsPresenter.class);

    private User loggedInUser;
    private LobbyService lobbyService;
    private UserService userService;
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

    public SettingsPresenter(User loggedInUser, LobbyService lobbyService, UserService userService, EventBus eventBus) {
        this.loggedInUser = loggedInUser;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.eventBus = eventBus;
    }

    /**
     * Überprüft die Benutzereingaben. Falls alle gültig sind, wird im UserService die Methode updateUser aufgerufen,
     * ansonsten wird eine entsprechende Fehlermeldung angezeigt
     *
     * @param event
     * @author Julia
     * @since Sprint4
     */
    @FXML
    public void onSaveButtonPressed(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String password2 = password2Field.getText();

        //keine Eingaben vom User -> Fenster schließen
        if (Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(email) && Strings.isNullOrEmpty(password) && Strings.isNullOrEmpty(password2)) {
            eventBus.post(new CloseSettingsEvent());
            clearAll();
        } else if (!password.equals(password2)) {
            showAlert(Alert.AlertType.ERROR, "Die Passwörter stimmen nicht überein", "Fehler");
            passwordField.clear();
            password2Field.clear();
            passwordField.requestFocus();
        } else if(!Pattern.matches("(?:[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])\")@(?:(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-][a-z0-9])?|[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+", email)) {
            showAlert(Alert.AlertType.ERROR, email + " ist keine gültige E-Mail-Adresse", "Fehler");
            emailField.clear();
            emailField.requestFocus();
        }
        else {
            //Wenn Felder leer sind, Daten vom loggedInUser übernehmen
            if (Strings.isNullOrEmpty(username)) {
                username = loggedInUser.getUsername();
            }
            if (Strings.isNullOrEmpty(email)) {
                email = loggedInUser.getEMail();
            }
            if (Strings.isNullOrEmpty(password)) {
                password = loggedInUser.getPassword();
            }
            userService.updateUser(new UserDTO(username, password, email), loggedInUser);
            clearAll();
        }
    }

    @FXML
    public void onDeleteAccountButtonPressed(ActionEvent actionEvent) {
        lobbyService.leaveAllLobbiesOnLogout((UserDTO) loggedInUser);
        //TODO implement
    }

    @FXML
    public void onCancelButtonPressed(ActionEvent actionEvent) {
        eventBus.post(new CloseSettingsEvent());
        clearAll();
    }

    /**
     * Aktualisiert den loggedInUser
     *
     * @param message
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if(loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
    }

    private void clearAll() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        password2Field.clear();
    }

}
