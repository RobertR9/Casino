package Client;

import game.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.Player;
import login.LoginController;

import java.io.IOException;


public class Client extends Application {
    public Stage stage;
    public Player player;

    public void start(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        stage.setWidth(1024);
        stage.setHeight(700);
        stage.setTitle("Roulette");
        setLoginScene();
        stage.show();
    }

    private void setLoginScene() {
        FXMLLoader loginFxml = new FXMLLoader();
        loginFxml.setLocation(getClass().getResource("/Login.fxml"));
        loginFxml.setController(new LoginController(this));
        Scene loginScene;
        try {
            loginScene = new Scene(loginFxml.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(loginScene);
    }

    @Deprecated
    public void setLobbyScene() {

        FXMLLoader lobbyFxml = new FXMLLoader(getClass().getResource("/Lobby.fxml"));
        System.err.print("LOBBYFXML=" + lobbyFxml.toString());
        Scene lobbyScene;
        try {
            lobbyScene = new Scene(lobbyFxml.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(lobbyScene);
    }

    public void setGameScene() {
        FXMLLoader gameFxml = new FXMLLoader(getClass().getResource("/Game.fxml"));
        Scene gameScene;
        try {
            gameFxml.setController(new GameController(this));
            gameScene = new Scene(gameFxml.load());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(gameScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
