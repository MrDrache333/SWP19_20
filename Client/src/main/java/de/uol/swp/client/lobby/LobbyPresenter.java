package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.server.lobby.LobbyManagement;
import javafx.event.ActionEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Paula, Haschem, Ferit
 * @version 0.1
 */

public class LobbyPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LobbyView.fxml";
    private static final String url = "https://confluence.swl.informatik.uni-oldenburg.de/display/SWP2019B/Spielanleitung?preview=/126746667/126746668/Dominion%20-%20Anleitung%20-%20V1.pdf";
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

    public void onInstructionsButtonPressed(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        catch (URISyntaxException e2) {
            e2.printStackTrace();
        }
    }
}






