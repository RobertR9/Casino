package listeners;

import controller.ServerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import library.Bet;
import library.Result;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.Iterator;

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
        ObjectMessage objectMessage = ServerController.topicSenderGateway.createObjectMessage(result);
        try {
            ServerController.topicSenderGateway.send(objectMessage);
        } catch (JMSException e) {
            System.err.print(e.getMessage());
        }

        Iterator<Bet> iterator = serverController.getBets().iterator();
        while (iterator.hasNext()) {
            Bet bet = iterator.next();
            if (bet.cameTrue(winningNr)) {
                bet.setStatus("Won");
            } else {
                bet.setStatus("Lost");
            }
            ServerController.serverGateway.handleBetResultReply(bet);

        }
//        ServerController.serverGateway.handleWinningNumberReply(result);
    }

}
