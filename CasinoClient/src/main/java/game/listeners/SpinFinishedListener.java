package game.listeners;


import game.GameController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SpinFinishedListener implements EventHandler<ActionEvent> {
    private int result;
    private GameController gameCtrl;

    public SpinFinishedListener(GameController gameCtrl, int result) {
        this.gameCtrl = gameCtrl;
        this.result = result;
    }

    @Override
    public void handle(ActionEvent event) {
        //TODO;

    }

}
