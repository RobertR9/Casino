package login;

import Client.Client;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import library.AuthPlayer;
import library.Lobby;
import lobby.LobbyController;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import java.rmi.RemoteException;

public class LoginController {

    private Client client;

    public LoginController(Client client) {
        this.client = client;
    }

    public void initialize() {

    }

    public void dialog(String barTitle, String title, Action action, TextField username, PasswordField password, Label validationError) {
        Dialog dlg = new Dialog(client.stage, barTitle);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        validationError.setStyle("-fx-text-fill: red");
        validationError.setVisible(false);
        grid.add(validationError, 1, 2);

        username.setPromptText("Username");
        password.setPromptText("Password");
        username.setOnMouseClicked((e) -> validationError.setVisible(false));
        password.setOnMouseClicked((e) -> validationError.setVisible(false));

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);


        final Action actionCancel = new AbstractAction("Cancel") {
            public void handle(ActionEvent ae) {
                dlg.hide();
            }
        };

        ButtonBar.setType(action, ButtonType.OK_DONE);
        action.disabledProperty().set(true);

        ButtonBar.setType(actionCancel, ButtonType.OK_DONE);

        username.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            action.disabledProperty().set(newValue.trim().isEmpty());
        });

        dlg.setMasthead(title);
        dlg.setContent(grid);
        dlg.getActions().addAll(action, actionCancel);

		/* Request focus on the username field by default */
        Platform.runLater(username::requestFocus);

        dlg.show();

    }

    public void login() {

        final TextField username = new TextField();
        final PasswordField password = new PasswordField();
        final Label validationError = new Label("Wrong username/password.");

        final Action action = new AbstractAction("Login") {

            public void handle(ActionEvent ae) {
                Dialog d = (Dialog) ae.getSource();

                try {
                    Object[] login = client.loginServer.login(username.getText(), password.getText());

                    if (login == null) {
                        throw new RemoteException("Invalid username or password");
                    }

                    client.authPlayer = (AuthPlayer) login[0];
                    Lobby lobby = (Lobby) login[1];
                    client.lobbyController = new LobbyController(client, lobby);
                    client.setLobbyScene();

                } catch (RemoteException e) {
                    e.printStackTrace();
                    /* Wrong username pass */
                    validationError.setVisible(true);
                    password.clear();
                    return;
                }
                d.hide();
            }
        };

        dialog("Login", "Login to Roulette", action, username, password, validationError);

    }

    public void register() {
        final TextField username = new TextField();
        final PasswordField password = new PasswordField();
        final Label validationError = new Label("Username already taken.");

        final Action action = new AbstractAction("Register") {
            /* This method is called when the login button is clicked */
            public void handle(ActionEvent ae) {
                Dialog d = (Dialog) ae.getSource();

                try {
                    client.loginServer.register(username.getText(), password.getText());
                } catch (RemoteException e) {

                    //TODO set to e.message.  username taken? password too short?
                    /* Wrong username pass */
                    validationError.setVisible(true);
                    password.clear();
                    return;
                }
                d.hide();
            }
        };

        dialog("Register", "Register for Roulette", action, username, password, validationError);
    }
}
