package game.listeners;


import game.Coordinate;
import game.GameController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class SelectorExitListener implements EventHandler<Event> {

    private GameController gameCtrl;
    private List<Integer> blackNums = Arrays.asList(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);

    public SelectorExitListener(GameController gameCtrl) {
        this.gameCtrl = gameCtrl;
    }

    @Override
    public void handle(Event event) {
        Pane p = (Pane) event.getSource();
        int row = GridPane.getRowIndex(p);
        int col = GridPane.getColumnIndex(p);

        Coordinate[] selection = gameCtrl.coordToSelection.get(new Coordinate(row, col));
        if (selection != null) {
            for (Coordinate c : selection) {
                Node n = gameCtrl.getPaneFromCoordinate(c.row, c.col);
                n.setEffect(new ColorAdjust());
                n.setStyle("-fx-border-color: transparent;\n");

                if (blackNums.contains(gameCtrl.coordToNumber.get(c))) {
                    n.setStyle("-fx-background-color: #191919;\n");
                }
            }
        }
    }
}
