package login;

import Client.Client;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jms.ClientGateway;
import library.Player;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

public class LoginController {

    private Client client;

    private static ClientGateway clientGateway;

    @FXML
    public TextField usernameField;

    public LoginController(Client client) {
        this.client = client;
    }

    @FXML
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

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
//        clientGateway.handleLogin(usernameField.getText());
        client.player = new Player(1L, usernameField.getText(), 1000d);
        client.setGameScene();
//        Boolean loginCheck = databaseConnector.checkUser(usernameField.getText(), passwordField.getText());

//        if (loginCheck) {
//            Context.getInstance().currentUser().setUsername(usernameField.getText());
//            Context.getInstance().currentUser().setPassword(passwordField.getText());
//            this.redirectFrame();
//        } else {
//            this.errorLogin();
//        }
    }
}
