package jms;

import controller.ServerController;
import gateway.GateWay;
import library.Bet;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ServerGateway extends GateWay {
    private Map<Integer, Message> bets = new HashMap<>();
    private ServerController serverController;

    public ServerGateway(ServerController serverController) {
        super("ClientReceiveQueue", "ClientSendQueue");
        this.serverController = serverController;
    }

    @Override
    protected void processObjectMessage(Message message) {
        System.err.print("ObjectMessage: " + message);
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                switch (message.getJMSType()) {
                    case "Bet":
                        System.out.print("\nbet incoming\n");
                        handleBetRequest(objectMessage);
                        break;
                    default:
                        throw new RuntimeException("Wrong message type");
                }
            } catch (JMSException e) {
                Logger.getLogger("ServerGateway").severe(e.getMessage());
            }
        }
    }

    public void handleBetResultReply(Bet bet, ObjectMessage oldObjectMessage) throws JMSException {
        ObjectMessage objectMessage = this.getSender().createObjectMessage(bet, oldObjectMessage);
        this.getSender().send(objectMessage);
        //removed bet from list
        serverController.removeBet(bet);
    }

    private void handleBetRequest(ObjectMessage objectMessage) {
        try {
            Bet bet = (Bet) objectMessage.getObject();
            int indexOf = serverController.addBet(bet);
            bets.put(indexOf, objectMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public Map<Integer, Message> getBets() {
        return bets;
    }
}
