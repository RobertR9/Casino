package game.listeners;

import game.Coordinate;
import game.GameController;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import library.Bet;

import java.util.Arrays;
import java.util.HashSet;


public class SelectorClickListener implements EventHandler<MouseEvent> {

    private GameController gameCtrl;
    private Coordinate[] offBoard = new Coordinate[]{new Coordinate(7, 0), new Coordinate(8, 0), new Coordinate(7, 24), new Coordinate(8, 24),};

    public SelectorClickListener(GameController gameCtrl) {
        this.gameCtrl = gameCtrl;
    }

    @Override
    public void handle(MouseEvent event) {
        Pane p = (Pane) event.getSource();
        int row = GridPane.getRowIndex(p);
        int col = GridPane.getColumnIndex(p);
        Coordinate coordinate = new Coordinate(row, col);

        if (Arrays.asList(offBoard).contains(coordinate))
            return;

        String url = "/images/";
        switch (gameCtrl.currChip) {
            case -1: /* No chip selected */
                return;
            case 0:
                url += "white_chip.png";
                break;
            case 1:
                url += "red_chip.png";
                break;
            case 2:
                url += "blue_chip.png";
                break;
            case 3:
                url += "green_chip.png";
                break;
            case 4:
                url += "black_chip.png";
                break;
        }

		/* Place chip */
        ImageView chip = new ImageView(new Image(url, 40.0, 40.0, true, false));
        chip.setMouseTransparent(true);
        chip.setX(event.getSceneX() - 372);
        chip.setY(event.getSceneY() - 22);
        gameCtrl.chipsOnBoard.getChildren().add(chip);
        gameCtrl.floatingChip.setVisible(false);

        for (ImageView i : gameCtrl.chips) {
            i.setEffect(new ColorAdjust());
        }

		/* Add bet to server */
        Coordinate[] selection = gameCtrl.coordToSelection.get(coordinate);
        Double betAmount = gameCtrl.chipAmounts[gameCtrl.currChip];
        gameCtrl.currChip = -1;
        int payout = (36 / selection.length) - 1;
        System.out.printf("library.Bet: %f Payout: %d to 1\n", betAmount, payout);

        HashSet<Integer> winningNumbers = new HashSet<>();
        Arrays.asList(selection).forEach(c -> winningNumbers.add(gameCtrl.coordToNumber.get(c)));

        Bet bet;
        try {
            bet = gameCtrl.client.player.makeBet(betAmount, payout, winningNumbers, gameCtrl.coordinateToDescription(coordinate));
            System.err.print("\n Bet: " + bet + " \n");
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return;
        }

        gameCtrl.addBet(bet);
        gameCtrl.betToChip.put(bet, chip);
        gameCtrl.refreshBalanceText();
    }

}
