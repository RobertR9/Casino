package Client;

import game.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.LoginServer;
import library.Player;
import library.ServerGame;
import login.LoginController;

import java.io.IOException;


public class Client extends Application {
    public Stage stage;
    public LoginServer loginServer;
    public Player player;
    public ServerGame serverGame;

    public Client() {
        try {
//            Registry registry = LocateRegistry.getRegistry();
//            this.loginServer = (LoginServer) registry.lookup("RouletteServer");
            this.loginServer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        System.err.print("LOGINFXML=" + loginFxml.toString());
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
            GameController gameController = new GameController(this);
            gameFxml.setController(gameController);
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
