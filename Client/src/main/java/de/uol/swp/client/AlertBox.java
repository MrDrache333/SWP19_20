package de.uol.swp.client;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox extends Alert {

    public AlertBox(AlertType alertType) {
        super(alertType);
        init();
    }

    public AlertBox(AlertType alertType, String Content, String Header) {
        super(alertType);
        init();
        this.getDialogPane().setHeaderText(Header);
        this.getDialogPane().setContentText(Content);
        this.show();
    }

    public AlertBox(AlertType alertType, String s, ButtonType... buttonTypes) {
        super(alertType, s, buttonTypes);
        init();
    }

    private void init() {
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.getDialogPane().setMaxWidth(400);
        this.getDialogPane().setPadding(new Insets(10, 10, 10, 10));
        Stage MainScene = ClientApp.getSceneManager().getPrimaryStage();
        this.setX(MainScene.getX() + MainScene.getWidth() / 2 - this.getDialogPane().getWidth() / 2);
        this.setY(MainScene.getY() + MainScene.getHeight() / 2 - this.getDialogPane().getHeight() / 2);
    }
}
