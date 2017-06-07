package jms;

import controller.ServerController;
import gateway.GateWay;
import library.Bet;
import library.Player;
import library.Result;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ServerGateway extends GateWay {
    private static Map<Integer, Message> messages = new HashMap<>();
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
                        handleBetRequest((Bet) objectMessage.getObject());
                        break;
                    default:
                        throw new RuntimeException("Wrong message type");
                }
            } catch (JMSException e) {
                Logger.getLogger("ServerGateway").severe(e.getMessage());
            }
        }
    }

    public void handleReply(Player player) {
        ObjectMessage objectMessage = this.getSender().createObjectMessage(player);
        this.getSender().send(objectMessage);
    }

    public void handleWinningNumberReply(Result result) {
        System.out.print("\n" + "handleWinningNumberReply" + "\n");

        ObjectMessage objectMessage = this.getSender().createObjectMessage(result);
        this.getSender().send(objectMessage);
    }

    private void handleBetRequest(Bet bet) {
        serverController.addBet(bet);
    }

}
