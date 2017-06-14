package gateway;

import messaging.TopicReceiverGateway;
import messaging.TopicSenderGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

public abstract class TopicGateWay {
    private TopicSenderGateway sender;

    public TopicGateWay(String senderChannel, String receiverChannel, String clientID) {
        sender = new TopicSenderGateway(senderChannel);
        TopicReceiverGateway receiver = new TopicReceiverGateway(receiverChannel, clientID);

        try {
            receiver.setListener(message -> {
                if (message instanceof ObjectMessage) {
                    processObjectMessage(message);
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected abstract void processObjectMessage(Message message);

}
