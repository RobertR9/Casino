package jms;

import game.GameController;
import gateway.TopicGateWay;
import library.Result;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.text.ParseException;

public class RouletteResultsTopicGateway extends TopicGateWay {
    private GameController gameController;

    public RouletteResultsTopicGateway(GameController gameController, String clientID) {
        super("RouletteResults", "RouletteResults", clientID);
        this.gameController = gameController;
    }

    @Override
    protected void processMessage(String message, String CorrelationId) throws JMSException, ParseException {
        System.err.print("snor:" + message);

    }

    @Override
    protected void processObjectMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                Result result = (Result) objectMessage.getObject();
                gameController.addResult(result);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
