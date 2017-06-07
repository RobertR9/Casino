package listeners;

import controller.ServerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import library.Result;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public class SpinFinishedListener implements EventHandler<ActionEvent> {
    private int winningNr;
    private ServerController serverController;

    public SpinFinishedListener(ServerController serverController, int winningNr) {
        this.serverController = serverController;
        this.winningNr = winningNr;
    }

    @Override
    public void handle(ActionEvent event) {
        Result result = new Result(winningNr);
        serverController.addResult(result);
        ObjectMessage objectMessage = serverController.topicSenderGateway.createObjectMessage(result);
        try {
            serverController.topicSenderGateway.send(objectMessage);
        } catch (JMSException e) {
            System.err.print(e.getMessage());
        }

        ServerController.serverGateway.handleWinningNumberReply(result);
    }

}
