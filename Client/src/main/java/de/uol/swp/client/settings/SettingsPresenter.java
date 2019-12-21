package de.uol.swp.client.settings;

import com.google.common.base.Strings;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static de.uol.swp.client.main.MainMenuPresenter.showAlert;

/**
 * The type Settings presenter.
 */
public class SettingsPresenter extends AbstractPresenter {

    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/SettingsView.fxml";
    private static final Logger LOG = LogManager.getLogger(SettingsPresenter.class);

    private User loggedInUser;
    private LobbyService lobbyService;
    private UserService userService;

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

    public SettingsPresenter(User loggedInUser, LobbyService lobbyService, UserService userService) {
        this.loggedInUser = loggedInUser;
        this.lobbyService = lobbyService;
        this.userService = userService;
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

        //keine Eingaben vom Nutzer
        if(Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(email) && Strings.isNullOrEmpty(password) && Strings.isNullOrEmpty(password2)) {
            cancel();
        } else if(!password.equals(password2)) {
            showAlert(Alert.AlertType.ERROR, "Die Passwörter sind nicht gleich", "Fehler");
            passwordField.clear();
            password2Field.clear();
            passwordField.requestFocus();
        } //TODO add check for valid email
        else {
            //Wenn Felder leer sind, Daten vom loggedInUser übernehmen
            if(Strings.isNullOrEmpty(username)) {
                username = loggedInUser.getUsername();
            } if(Strings.isNullOrEmpty(email)) {
                email = loggedInUser.getEMail();
            } if(Strings.isNullOrEmpty(password)) {
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
        cancel();
    }

    public void updateLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    private void clearAll() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        password2Field.clear();
    }

    /**
     * schließt das Einstellungsfenster
     *
     * @author Julia
     * @since Sprint4
     */
    private void cancel() {
        Platform.runLater(() -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
        clearAll();
    }
}
