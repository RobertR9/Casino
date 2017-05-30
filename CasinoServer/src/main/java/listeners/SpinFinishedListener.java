package listeners;

import controller.ServerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import library.Result;

public class SpinFinishedListener implements EventHandler<ActionEvent> {
    private int result;
    private ServerController serverController;

    public SpinFinishedListener(ServerController serverController, int result) {
        this.serverController = serverController;
        this.result = result;
    }

    @Override
    public void handle(ActionEvent event) {
        serverController.addResult(new Result(result,"test"));
        //TODO;

    }

}
