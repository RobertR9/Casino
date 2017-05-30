package jms;

import gateway.GateWay;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.Map;

public class ClientGateway extends GateWay {
    private static Map<Integer, Message> messages = new HashMap<>();

    public ClientGateway() {
        super("ClientSendQueue", "ClientReceiveQueue");
    }

    @Override
    protected void processObjectMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                switch (objectMessage.getJMSType()) {
                    case "login":
                        System.err.print(message);
                        break;
                    default:
                        throw new RuntimeException("No Object Message");
                }
            } catch (JMSException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void handleLogin(String username) {
        ObjectMessage objectMessage;
        objectMessage = this.getSender().createObjectMessage(username);
        this.getSender().send(objectMessage);
    }


}
