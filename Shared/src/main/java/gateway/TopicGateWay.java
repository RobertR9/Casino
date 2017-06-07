package gateway;

import messaging.TopicReceiverGateway;
import messaging.TopicSenderGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.text.ParseException;

public abstract class TopicGateWay {
    private TopicSenderGateway sender;

    public TopicGateWay(String senderChannel, String receiverChannel, String clientID) {
        sender = new TopicSenderGateway(senderChannel);
        TopicReceiverGateway receiver = new TopicReceiverGateway(receiverChannel, clientID);

        try {
            receiver.setListener(message -> {
                if (message instanceof ObjectMessage) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    try {
                        System.out.println("incoming object: " + objectMessage.getObject());
                        processObjectMessage(message);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected abstract void processMessage(String message, String CorrelationId) throws JMSException, ParseException;

    protected abstract void processObjectMessage(Message message);

}
