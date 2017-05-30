package controller;


import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;
import library.Bet;
import library.Result;
import listeners.SpinFinishedListener;
import server.Server;

public class ServerController {

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
    @FXML
    public TableView<Result> resultTable;
    @FXML
    public TableColumn<Result, Integer> numberColumn;
    @FXML
    public TableColumn<Result, String> colorColumn;

    public Server server;
    private ObservableList<Bet> bets = FXCollections.observableArrayList();
    private ObservableList<Result> results = FXCollections.observableArrayList();

    public ServerController(Server Server) {
        this.server = server;
    }

    /* Called after scene graph loads */
    public void initialize() {
        // library.Bet table
        betTable.setPlaceholder(new Label("No Bets Placed"));
        betTable.setItems(bets);
        resultTable.setPlaceholder(new Label("No Results yet."));
        resultTable.setItems(results);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));


        ball.setCenterX(-45);
        ball.setCenterY(13);
    }

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
        rt.setOnFinished(new SpinFinishedListener(this, result));
        System.out.println("Result: " + result);
    }

    public void addBet(Bet b) {
        bets.add(b);
    }

    public void addResult(Result result) {
        results.add(result);
        System.err.print(resultTable.getItems());
        resultTable.refresh();
    }

    @FXML
    public void handleBetAction() {
//        for (Bet bet : this.bets) {
//            if (bet.getPlayer().getId().equals(client.player.getId())) {
//                //TODO:: Send bet to server through JMS
//            }
//        }

    }
}
