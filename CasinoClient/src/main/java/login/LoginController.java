package login;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import library.Player;

public class LoginController {
    private Client client;
    @FXML
    public TextField usernameField;

    public LoginController(Client client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        client.player = new Player(usernameField.getText(), 1000d);
        client.setGameScene();
    }
}
