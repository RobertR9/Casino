package jms;

import gateway.GateWay;
import library.Player;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerGateway extends GateWay {
    private static Map<Integer, Message> messages = new HashMap<>();

    public ServerGateway() {
        super("ClientReceiveQueue", "ClientSendQueue");
    }

    @Override
    protected void processObjectMessage(Message message) {
        System.err.print("ObjectMessage: " + message);
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                if (message.getJMSType().equals("login")) {
                    System.err.print("ObjectMessage: " + objectMessage.getObject());
                } else {
                    throw new RuntimeException("Wrong type Message");
                }
            } catch (JMSException e) {
                Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            }
        }
    }

    public void handleReply(Player player) {
        ObjectMessage objectMessage = this.getSender().createObjectMessage(player);
        this.getSender().send(objectMessage);
    }

}
