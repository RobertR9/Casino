package jms;

import game.GameController;
import gateway.GateWay;
import library.Bet;
import library.Result;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

public class ClientGateway extends GateWay {
    private GameController gameController;

    public ClientGateway(GameController gameController) {
        super("ClientSendQueue", "ClientReceiveQueue");
        this.gameController = gameController;
    }

    @Override
    protected void processObjectMessage(Message message) {
        System.out.print("\n processObjectMessage: " + message + "\n");

        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                switch (objectMessage.getJMSType()) {
                    case "login":
                        System.err.print(message);
                        break;
                    case "BetResult":
                        System.out.print("\n Betresult: " + objectMessage.getObject());
                        gameController.handleBet((Result) objectMessage.getObject());
                        break;
                    default:
                        throw new RuntimeException("No Object Message");
                }
            } catch (JMSException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //TODO: Fix login?? Send player to server so server knows which players are playing
    public void handleLogin(String username) {
        ObjectMessage objectMessage;
        objectMessage = this.getSender().createObjectMessage(username);
//        this.getSender().send(objectMessage);
    }

    //TODO:: Send Bet to server so server knows which bets are for this round
    public void sendBet(Bet bet) {
        ObjectMessage objectMessage = this.getSender().createObjectMessage(bet);
        this.getSender().send(objectMessage);
    }
}
