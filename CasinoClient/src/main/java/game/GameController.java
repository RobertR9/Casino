package game;


import Client.Client;
import game.listeners.ChipClickListener;
import game.listeners.SelectorClickListener;
import game.listeners.SelectorEnterListener;
import game.listeners.SelectorExitListener;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import jms.ClientGateway;
import jms.ClientTopicListener;
import jms.RouletteResultsTopicGateway;
import library.Bet;
import library.Result;

import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML
    public AnchorPane boardPane;
    @FXML
    public GridPane grid;
    @FXML
    public Text balanceText;
    @FXML
    public ImageView whiteChip;
    @FXML
    public ImageView redChip;
    @FXML
    public ImageView blueChip;
    @FXML
    public ImageView greenChip;
    @FXML
    public ImageView blackChip;
    @FXML
    public Label whiteChipAmt;
    @FXML
    public Label redChipAmt;
    @FXML
    public Label blueChipAmt;
    @FXML
    public Label greenChipAmt;
    @FXML
    public Label blackChipAmt;
    @FXML
    public TableView<Bet> betTable;
    @FXML
    public TableColumn<Bet, Double> amountColumn;
    @FXML
    public TableColumn<Bet, String> descriptionColumn;
    @FXML
    public TableColumn<Bet, Button> deleteColumn;
    @FXML
    public TableView<Result> resultTable;
    @FXML
    public TableColumn<Result, Integer> numberColumn;
    @FXML
    public TableColumn<Result, String> colorColumn;

    public Client client;
    private Coordinate[] numberToCoordinate;
    public HashMap<Coordinate, Integer> coordToNumber;
    public HashMap<Coordinate, Coordinate[]> coordToSelection;
    private HashMap<Coordinate, String> specialDescriptions;
    public Double[] chipAmounts = new Double[]{5d, 10d, 20d, 50d, 100d};
    public int currChip = -1;
    public ImageView[] chips;
    public ImageView floatingChip = new ImageView();
    public Group chipsOnBoard = new Group();
    public HashMap<Bet, ImageView> betToChip = new HashMap<>();
    private ObservableList<Bet> bets = FXCollections.observableArrayList();
    private ObservableList<Result> results = FXCollections.observableArrayList();
    private static ClientGateway clientGateway;
    private static ClientTopicListener clientTopicListener;
    private static RouletteResultsTopicGateway rouletteResultsTopicGateway;

    public GameController(Client client) {
        super();
        this.client = client;
        coordToSelection = new HashMap<>();
        specialDescriptions = new HashMap<>();
        coordToNumber = new HashMap<>();
        numberToCoordinate = new Coordinate[37];
        clientGateway = new ClientGateway(this);
    }

    /* Called after scene graph loads */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setup topic listeners
        try {
            rouletteResultsTopicGateway = new RouletteResultsTopicGateway(this, this.client.player.getUsername());
            clientTopicListener = new ClientTopicListener("RouletteResults", this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Setup the betTable
        betTable.setPlaceholder(new Label("No Bets Placed"));
        betTable.setItems(bets);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        deleteColumn.setCellValueFactory(p -> {
            Button button = new Button("Delete");
            button.setOnMouseClicked(arg0 -> deleteBet(p.getValue()));
            return new ReadOnlyObjectWrapper<>(button);
        });

        //Setup the Result table
        resultTable.setPlaceholder(new Label("No Results yet."));
        resultTable.setItems(results);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        floatingChip.setMouseTransparent(true);
        floatingChip.setFitHeight(40);
        floatingChip.setFitWidth(40);
        boardPane.getChildren().add(chipsOnBoard);
        boardPane.getChildren().add(floatingChip);

        //Roulette board init
        grid.setOnMouseEntered(arg0 -> {
            if (currChip != -1) {
                floatingChip.setVisible(true);
            }
        });

        grid.setOnMouseExited(arg0 -> floatingChip.setVisible(false));

        grid.setOnMouseMoved(event -> {
            floatingChip.setX(event.getSceneX() - 370);
            floatingChip.setY(event.getSceneY() - 23);
        });

        whiteChipAmt.setText(chipAmounts[0] + "");
        redChipAmt.setText(chipAmounts[1] + "");
        blueChipAmt.setText(chipAmounts[2] + "");
        greenChipAmt.setText(chipAmounts[3] + "");
        blackChipAmt.setText(chipAmounts[4] + "");

        this.chips = new ImageView[]{whiteChip, redChip, blueChip, greenChip, blackChip};

        specialDescriptions.put(new Coordinate(8, 9), "Red");
        specialDescriptions.put(new Coordinate(8, 13), "Black");
        specialDescriptions.put(new Coordinate(7, 1), "1-12");
        specialDescriptions.put(new Coordinate(7, 9), "13-24");
        specialDescriptions.put(new Coordinate(7, 17), "25-36");
        specialDescriptions.put(new Coordinate(8, 1), "1-18");
        specialDescriptions.put(new Coordinate(8, 5), "Even");
        specialDescriptions.put(new Coordinate(8, 17), "Odd");
        specialDescriptions.put(new Coordinate(8, 21), "19-36");
        specialDescriptions.put(new Coordinate(5, 24), "1st Column");
        specialDescriptions.put(new Coordinate(3, 24), "2nd Column");
        specialDescriptions.put(new Coordinate(1, 24), "3rd Column");

		/* Get coordinate for each number */
        int num = 0;
        int row = 5;
        int col = 1;
        while (++num <= 36) {
            Coordinate c = new Coordinate(row, col);
            numberToCoordinate[num] = c;
            coordToNumber.put(c, num);
            if (row == 1) {
                col += 2;
                row = 5;
            } else {
                row -= 2;
            }
        }

		/* 0 selector */
        numberToCoordinate[0] = new Coordinate(1, 0);
        coordToNumber.put(new Coordinate(1, 0), 0);

		/* Each number selects itself */
        Arrays.asList(numberToCoordinate).forEach(c -> coordToSelection.put(c, new Coordinate[]{c}));

		/* Top and bottom selectors for whole column */
        for (col = 1; col <= 23; col += 2) {
            Coordinate[] coordinates = new Coordinate[]{
                    new Coordinate(1, col),
                    new Coordinate(3, col),
                    new Coordinate(5, col)
            };
            coordToSelection.put(new Coordinate(0, col), coordinates);
            coordToSelection.put(new Coordinate(6, col), coordinates);
        }

		/* Top and bottom selectors for double column */
        for (col = 2; col <= 22; col += 2) {
            Coordinate[] coordinates = new Coordinate[]{
                    new Coordinate(1, col - 1),
                    new Coordinate(1, col + 1),
                    new Coordinate(3, col - 1),
                    new Coordinate(3, col + 1),
                    new Coordinate(5, col - 1),
                    new Coordinate(5, col + 1)
            };
            coordToSelection.put(new Coordinate(0, col), coordinates);
            coordToSelection.put(new Coordinate(6, col), coordinates);
        }

		/* Four selectors */
        for (int r : new int[]{2, 4}) {
            for (int c = 2; c <= 22; c += 2) {
                coordToSelection.put(new Coordinate(r, c), new Coordinate[]{new Coordinate(r - 1, c - 1), new Coordinate(r - 1, c + 1), new Coordinate(r + 1, c + 1), new Coordinate(r + 1, c - 1)});
            }
        }

		/* Two selectors (vertical) */
        for (int r : new int[]{2, 4}) {
            for (int c = 1; c <= 23; c += 2) {
                coordToSelection.put(new Coordinate(r, c), new Coordinate[]{new Coordinate(r - 1, c), new Coordinate(r + 1, c)});
            }
        }

		/* Two selectors (horizontal) */
        for (int r : new int[]{1, 3, 5}) {
            for (int c = 2; c <= 22; c += 2) {
                coordToSelection.put(new Coordinate(r, c), new Coordinate[]{new Coordinate(r, c - 1), new Coordinate(r, c + 1)});
            }
        }

		/* 1-12 selector */
        Coordinate[] ray = new Coordinate[12];
        System.arraycopy(numberToCoordinate, 1, ray, 0, 12);
        coordToSelection.put(new Coordinate(7, 1), ray);

		/* 13-24 selector */
        ray = new Coordinate[12];
        System.arraycopy(numberToCoordinate, 13, ray, 0, 12);
        coordToSelection.put(new Coordinate(7, 9), ray);

		/* 25-36 selector */
        ray = new Coordinate[12];
        System.arraycopy(numberToCoordinate, 25, ray, 0, 12);
        coordToSelection.put(new Coordinate(7, 17), ray);

		/* 1-18 selector */
        ray = new Coordinate[18];
        System.arraycopy(numberToCoordinate, 1, ray, 0, 18);
        coordToSelection.put(new Coordinate(8, 1), ray);

		/* 19-36 selector */
        ray = new Coordinate[18];
        System.arraycopy(numberToCoordinate, 19, ray, 0, 18);
        coordToSelection.put(new Coordinate(8, 21), ray);

		/* Parity selector */
        Coordinate[] evenCoordinates = new Coordinate[18];
        Coordinate[] oddCoordinates = new Coordinate[18];
        int eIdx = 0;
        int oIdx = 0;

        for (int i = 1; i <= 36; i++) {
            if (i % 2 == 0)
                evenCoordinates[eIdx++] = numberToCoordinate[i];
            else
                oddCoordinates[oIdx++] = numberToCoordinate[i];
        }
        coordToSelection.put(new Coordinate(8, 5), evenCoordinates);
        coordToSelection.put(new Coordinate(8, 17), oddCoordinates);

        // Red/Black selector
        List<Coordinate> redCoordinates = new ArrayList<>();
        List<Coordinate> blackCoordinates = new ArrayList<>();
        Coordinate[] redRay = new Coordinate[18];
        Coordinate[] blackRay = new Coordinate[18];

		/* Reds */
        Arrays.asList(2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35).forEach
                (i -> redCoordinates.add(numberToCoordinate[i]));

		/* Blacks */
        Arrays.asList(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36).forEach
                (i -> blackCoordinates.add(numberToCoordinate[i]));

        coordToSelection.put(new Coordinate(8, 9), redCoordinates.toArray(redRay));
        coordToSelection.put(new Coordinate(8, 13), blackCoordinates.toArray(blackRay));

		/* '1st','2nd','3rd' column selectors */
        Coordinate[] first = new Coordinate[12];
        Coordinate[] second = new Coordinate[12];
        Coordinate[] third = new Coordinate[12];
        int firstIdx = 0, secondIdx = 0, thirdIdx = 0;
        for (int i = 1; i <= 36; i++) {
            if (i % 3 == 0) {
                third[thirdIdx++] = numberToCoordinate[i];
            } else if (i % 3 == 2) {
                second[secondIdx++] = numberToCoordinate[i];
            } else {
                first[firstIdx++] = numberToCoordinate[i];
            }
        }
        coordToSelection.put(new Coordinate(5, 24), first);
        coordToSelection.put(new Coordinate(3, 24), second);
        coordToSelection.put(new Coordinate(1, 24), third);

		/* Listeners */
        for (Node n : grid.getChildren()) {
            n.setOnMouseEntered(new SelectorEnterListener(this));
            n.setOnMouseExited(new SelectorExitListener(this));
            n.setOnMouseClicked(new SelectorClickListener(this));
        }

        EventHandler<MouseEvent> chipClick = new ChipClickListener(this);
        whiteChip.setOnMouseClicked(chipClick);
        redChip.setOnMouseClicked(chipClick);
        blueChip.setOnMouseClicked(chipClick);
        greenChip.setOnMouseClicked(chipClick);
        blackChip.setOnMouseClicked(chipClick);
        try {
            refreshBalanceText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Node getPaneFromCoordinate(int row, int col) {
        for (Node n : grid.getChildren()) {
            if (GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col)
                return n;
        }
        return null;
    }

    public void addBet(Bet b) {
        bets.add(b);
        changeChipsAction();
    }

    /**
     * Removes the Bet from the bets list.
     * Removes chips from the GUI.
     * Updates player balance.
     *
     * @param bet Bet
     */
    private void deleteBet(Bet bet) {
        bets.remove(bet);
        chipsOnBoard.getChildren().remove(betToChip.get(bet));
        client.player.setBalance(client.player.getBalance() + bet.getAmount());
        refreshBalanceText();
        changeChipsAction();
    }

    public String coordinateToDescription(Coordinate coordinate) {
        if (specialDescriptions.containsKey(coordinate)) {
            return specialDescriptions.get(coordinate);
        } else {
            HashSet<Integer> winningNumbers = new HashSet<>();
            Arrays.asList(coordToSelection.get(coordinate)).forEach
                    (c -> winningNumbers.add(coordToNumber.get(c)));

            String stringRay = winningNumbers.toString();
            return stringRay.substring(1, stringRay.length() - 1);
        }
    }

    @FXML
    public void handleBetButtonAction() {
        for (Bet bet : this.bets) {
            clientGateway.sendBet(bet);
        }
    }

    /**
     * Refreshes the balance textbox to the current balance of the player.
     */
    public void refreshBalanceText() {
        balanceText.setText(String.format("\u20AC %.2f", client.player.getBalance()));
    }

    /**
     * Check all bets if the bet's number(s) are correct
     * Updates Player's balance.
     * Removes chips from the GUI.
     *
     * @param result Result
     */
    public void handleBet(Result result) {
        System.err.print("\n handleBetMethod:" + result + "\n");
        for (Bet bet : this.bets) {
            if (bet.cameTrue(result.getNumber())) {
                this.client.player.setBalance(this.client.player.getBalance() + (bet.getAmount() * bet.getPayout()));
            } else {
                Platform.runLater(() -> this.chipsOnBoard.getChildren().remove(this.betToChip.get(bet)));
            }
            bets.remove(bet);
        }
        this.refreshBalanceText();
    }

    private void changeChipsAction() {
        System.err.print("\n PlayerBalance: " + this.client.player.getBalance() + "\n ChipValue: " + chipAmounts[4]);
        if (this.client.player.getBalance() < chipAmounts[0]) {
            whiteChip.setDisable(true);
        } else {
            whiteChip.setDisable(false);
        }
        if (this.client.player.getBalance() < chipAmounts[1]) {
            redChip.setDisable(true);
        } else {
            redChip.setDisable(false);
        }
        if (this.client.player.getBalance() < chipAmounts[2]) {
            blueChip.setDisable(true);
        } else {
            blueChip.setDisable(false);
        }
        if (this.client.player.getBalance() < chipAmounts[3]) {
            greenChip.setDisable(true);
        } else {
            greenChip.setDisable(false);
        }
        if (this.client.player.getBalance() < chipAmounts[4]) {
            blackChip.setDisable(true);
        } else {
            blackChip.setDisable(false);
        }
    }

    public void addResult(Result result) {
        results.add(result);
    }
}
