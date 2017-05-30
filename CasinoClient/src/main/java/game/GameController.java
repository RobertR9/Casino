package game;


import Client.Client;
import game.listeners.*;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jms.TopicListener;
import jms.TopicReceiverGateway;
import library.Bet;
import library.ClientGame;

import java.util.*;

public class GameController implements ClientGame {

    @FXML
    public AnchorPane boardPane;
    @FXML
    public GridPane grid;
    @FXML
    public ImageView wheel;
    @FXML
    public Circle ball;
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

    public Client client;
    private Coordinate[] numberToCoordinate;
    public HashMap<Coordinate, Integer> coordToNumber;
    public HashMap<Coordinate, Coordinate[]> coordToSelection;
    private HashMap<Coordinate, String> specialDescriptions;
    private ObservableList<Bet> bets = FXCollections.observableArrayList();
    public Double[] chipAmounts = new Double[]{5d, 10d, 20d, 50d, 100d};
    public int currChip = -1;
    public ImageView[] chips;
    public ImageView floatingChip = new ImageView();
    public Group chipsOnBoard = new Group();
    public HashMap<Bet, ImageView> betToChip = new HashMap<>();
    private static TopicListener topicListener;
    private static TopicReceiverGateway topicReceiverGateway;

    public GameController(Client client) {
        super();
        this.client = client;
        coordToSelection = new HashMap<>();
        specialDescriptions = new HashMap<>();
        coordToNumber = new HashMap<>();
        numberToCoordinate = new Coordinate[37];
    }

    /* Called after scene graph loads */
    @SuppressWarnings("unused")
    public void initialize() {
        topicListener = new TopicListener("RouletteResults");
        topicReceiverGateway = new TopicReceiverGateway("RouletteResults", this.client.player.getUsername());
        // library.Bet table
        betTable.setPlaceholder(new Label("No Bets Placed"));
        betTable.setItems(bets);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        deleteColumn.setCellValueFactory(p -> {
            Button button = new Button("Delete");
            button.setOnMouseClicked(arg0 -> deleteBet(p.getValue()));
            return new ReadOnlyObjectWrapper<>(button);
        });

        floatingChip.setMouseTransparent(true);
        floatingChip.setFitHeight(40);
        floatingChip.setFitWidth(40);
        boardPane.getChildren().add(chipsOnBoard);
        boardPane.getChildren().add(floatingChip);

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
            balanceText.setText(String.format("\u20AC %.2f", client.player.getBalance()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ball.setCenterX(-45);
        ball.setCenterY(13);

    }

    public Node getPaneFromCoordinate(int row, int col) {
        for (Node n : grid.getChildren()) {
            if (GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col)
                return n;
        }
        return null;
    }

    //TODO: MOVE this to server and result to topic
    @FXML
    void handleSpinAction(ActionEvent e) {
        wheel.setRotate(0);

        Path path = new Path();
        path.getElements().add(new MoveTo(0, 0));

		/* The wheel is a 16 slice pizza */
        double radius = 110;
        double[] xs = new double[]{-45, -75, -101, -110, -102, -69, -47, 0, 43, 76, 105, 115, 108, 80, 50, 0};
        double[] ys = new double[]{13, 35, 70, 105, 155, 201, 215, 228, 222, 202, 160, 115, 80, 33, 12, 0};

		/* Choose a random slice for ball to finish on */
        int ballPos = (int) (Math.random() * 16);
        int spins = 12;
        long ticks = (16 * spins) + ballPos;

        for (int i = 0; i < ticks; i++) {
            double x = xs[i % xs.length];
            double y = ys[i % ys.length];
            path.getElements().add(new ArcTo(radius, radius, 0, x, y, false, false));
        }

        long durationMillis = 12 * 1000;
        PathTransition spin = new PathTransition(Duration.millis(durationMillis), path, ball);
        spin.setInterpolator(Interpolator.LINEAR);
        spin.play();

		/* The wheel is a 37 slice pizza.  Choose random slice */
        int wheelPos = (int) (Math.random() * 37);
        int wheelSpins = 3;
        double deg = (360.0 * wheelSpins) + ((360.0 / 37.0) * wheelPos);
        RotateTransition rt = new RotateTransition(Duration.millis(durationMillis), wheel);
        rt.setInterpolator(Interpolator.EASE_BOTH);
        rt.setByAngle(deg);
        rt.play();

        int[] order = new int[]{0, 26, 3, 35, 12, 28, 7, 29, 18, 22, 9, 31, 14, 20, 1, 33, 16, 24, 5, 10, 23, 8, 30, 11, 36, 13, 27, 6, 34, 17, 25, 2, 21, 4, 19, 15, 32};
        int[] ballPosToOffset = new int[]{-1, 1, 3, 5, 7, 10, 13, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35};
        int result = order[(wheelPos + ballPosToOffset[ballPos]) % 37];
        System.out.println("Result: " + result);

        rt.setOnFinished(new SpinFinishedListener(this, result));
    }

    public void addBet(Bet b) {
        bets.add(b);
    }

    private void deleteBet(Bet bet) {
        bets.remove(bet);
        chipsOnBoard.getChildren().remove(betToChip.get(bet));
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
    public void handleBetAction() {
        for (Bet bet : this.bets) {
            if (bet.getPlayer().getId().equals(client.player.getId())) {
                //TODO:: Send bet to server through JMS
            }
        }

    }

    @Override
    public void spin(int result) {

    }
}
