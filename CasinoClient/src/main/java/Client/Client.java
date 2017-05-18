package Client;

import account.AccountController;
import game.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.AuthPlayer;
import library.LoginServer;
import library.ServerGame;
import lobby.LobbyController;
import login.LoginController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client extends Application {
    public Stage stage;
    public LoginServer loginServer;
    public AuthPlayer authPlayer;
    public ServerGame serverGame;
    public LobbyController lobbyController;

    public Client() {
        try {
            Registry registry = LocateRegistry.getRegistry();
//            this.loginServer = (LoginServer) registry.lookup("RouletteServer");
            this.loginServer = null;
        } catch (RemoteException e) {
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

        FXMLLoader loginFxml = new FXMLLoader(getClass().getResource("login/Login.fxml"));
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

        FXMLLoader lobbyFxml = new FXMLLoader(getClass().getResource("lobby/Lobby.fxml"));
        lobbyFxml.setController(lobbyController);
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

        if (serverGame == null) {
            System.err.println("No game loaded. Can't switch to game scene.");
            return;
        }

        FXMLLoader gameFxml = new FXMLLoader(getClass().getResource("game/Game.fxml"));
        try {
            GameController gameController = new GameController(this, serverGame);
            gameFxml.setController(gameController);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        Scene gameScene;
        try {
            gameScene = new Scene(gameFxml.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(gameScene);
    }

    public void setAccountScene() {

        FXMLLoader accountFxml = new FXMLLoader(getClass().getResource("account/Account.fxml"));
        accountFxml.setController(new AccountController(this));
        Scene accountScene;
        try {
            accountScene = new Scene(accountFxml.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(accountScene);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
