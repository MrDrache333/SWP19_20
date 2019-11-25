package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.server.lobby.LobbyManagement;
import javafx.event.ActionEvent;

/**
 * @author Paula, Haschem, Ferit
 * @version 0.1
 */

public class LobbyPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LobbyView.fxml";
    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();

    public LobbyPresenter() {
    }

    @Inject
    public LobbyPresenter(EventBus eventBus, LobbyManagement lobbyManagement) {
        setEventBus(eventBus);
    }

    public void onLogoutButtonPressed(ActionEvent actionEvent) {

        userService.logout(loggedInUser);
    }
}






