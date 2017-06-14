package jms;

import game.GameController;
import gateway.GateWay;
import library.Bet;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.Map;

public class ClientGateway extends GateWay {
    private GameController gameController;
    private static Map<String, Bet> bets = new HashMap<>();

    public ClientGateway(GameController gameController) {
        super("ClientSendQueue", "ClientReceiveQueue");
        this.gameController = gameController;
    }

    @Override
    protected void processObjectMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                switch (objectMessage.getJMSType()) {
                    case "BetResult":
                        gameController.handleBet((Bet) objectMessage.getObject(), bets.get(objectMessage.getJMSCorrelationID()));
                        break;
                    default:
                        throw new RuntimeException("No Object Message");
                }
            } catch (JMSException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void sendBet(Bet bet) throws JMSException {
        ObjectMessage objectMessage = null;
        try {
            objectMessage = this.getSender().createObjectMessage(bet);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        this.getSender().send(objectMessage);
        try {
            bets.put(objectMessage.getJMSMessageID(), bet);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
