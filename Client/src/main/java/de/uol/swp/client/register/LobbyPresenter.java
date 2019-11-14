package de.uol.swp.client.register;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.register.event.ShowLobbyViewEvent;
import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.LobbyManagement;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class LobbyPresenter extends AbstractPresenter {
     public static final String fxml = "/fxml/LobbyView.fxml";
    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();






    public LobbyPresenter() {
    }


    @Inject
    public LobbyPresenter(EventBus eventBus, LobbyManagement lobbyManagement) {
        setEventBus(eventBus);


    }

    @FXML
    private void onLobbyCreatedButtonPressed (ActionEvent even, LobbyManagement lobbyManagement) {
        eventBus.post(showLobbyViewMessage);
    }







}




