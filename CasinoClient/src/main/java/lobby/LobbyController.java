package lobby;


import Client.Client;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import library.Lobby;
import library.ServerGame;

import java.rmi.RemoteException;
import java.util.List;

public class LobbyController {

    private Client client;
    private Lobby lobby;

    @FXML
    public TableView<LobbyRow> tableView;
    @FXML
    public TableColumn<LobbyRow, String> nameCol;
    @FXML
    public TableColumn<LobbyRow, String> playersCol;
    @FXML
    public TableColumn<LobbyRow, Button> joinCol;
    @FXML
    public Button searchBtn;
    @FXML
    public Button createBtn;
    @FXML
    public TextField searchText;
    @FXML
    public TextField createText;

    public LobbyController(Client client, Lobby lobby) {
        this.client = client;
        this.lobby = lobby;
    }

    @FXML
    public void initialize() {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        playersCol.setCellValueFactory(new PropertyValueFactory<>("players"));
        joinCol.setCellValueFactory(new PropertyValueFactory<>("joinBtn"));

        searchBtn.setOnMouseClicked((e) -> search(searchText.getText()));
//        createBtn.setOnMouseClicked((e) -> create(createText.getText()));
    }

    private void search(String name) {
        ObservableList<LobbyRow> rows = tableView.getItems();
        rows.clear();

        List<ServerGame> serverGames;
        try {
            serverGames = lobby.lookupGame(name, 8);
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        for (ServerGame sg : serverGames) {
            LobbyRow lr = new LobbyRow(client, lobby, sg);
            rows.add(lr);
        }
    }

    private void create(String name) {
        try {
            ServerGame g = lobby.addGame(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
