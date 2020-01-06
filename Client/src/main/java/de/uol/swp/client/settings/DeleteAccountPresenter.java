package de.uol.swp.client.settings;

import com.google.common.eventbus.EventBus;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.settings.event.CloseDeleteAccountEvent;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Presenter für das Account löschen Fenster
 *
 * @author Anna
 * @since Sprint4
 */
public class DeleteAccountPresenter {

    public static final String fxml = "/fxml/DeleteAccountView.fxml";
    public static final String css = "css/DeleteAccountPresenter.css";
    private static final Logger LOG = LogManager.getLogger(DeleteAccountPresenter.class);

    private User loggedInUser;
    private LobbyService lobbyService;
    private UserService userService;
    private EventBus eventBus;


    public DeleteAccountPresenter(User loggedInUser,LobbyService lobbyService, UserService userService, EventBus eventBus) {
        this.loggedInUser = loggedInUser;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.eventBus = eventBus;
    }

    @FXML
    public void onYesButtonPressed(javafx.event.ActionEvent actionEvent) {
        lobbyService.leaveAllLobbiesOnLogout((UserDTO) loggedInUser);
        userService.dropUser(loggedInUser);
    }

    public void onNoButtonPressed(ActionEvent actionEvent) {
        eventBus.post(new CloseDeleteAccountEvent());
    }
}
