package server;

import controller.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jms.ServerGateway;
import models.ServerGameImpl;

public class Server extends Application {
    private static ServerGameImpl serverGame;
    private Stage stage;

    public void setGameScene() {
        FXMLLoader gameFxml = new FXMLLoader(getClass().getResource("/Game.fxml"));
        Scene gameScene;
        try {
            gameFxml.setController(new ServerController(this));
            gameScene = new Scene(gameFxml.load());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(gameScene);
    }

    public static void main(String[] args) {
        serverGame = new ServerGameImpl("Roulette");
        System.err.print("Starting server with game: " + serverGame.getName());
        try {
            new ServerGateway();
        } catch (Exception e) {
            System.err.println("Exception binding RouletteServer:");
            e.printStackTrace();
        }
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setResizable(false);
        stage.setWidth(1024);
        stage.setHeight(700);
        stage.setTitle("Roulette");
        setGameScene();
        stage.show();
    }
}
