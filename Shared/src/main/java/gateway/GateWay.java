package gateway;

import messaging.MessageReceiverGateway;
import messaging.MessageSenderGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

public abstract class GateWay {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public GateWay(String channelNameSender, String channelNameReceiver) {
        sender = new MessageSenderGateway(channelNameSender);
        receiver = new MessageReceiverGateway(channelNameReceiver);

        receiver.setListener(message -> {
            if (message instanceof ObjectMessage) {
                try {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    switch (objectMessage.getJMSType()) {
                        case "Result":
                            this.processObjectMessage(message);
                            break;
                        case "BetResult":
                            this.processObjectMessage(message);
                            break;
                        default:
                    }
                } catch (JMSException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    public MessageSenderGateway getSender() {
        return sender;
    }

    protected abstract void processObjectMessage(Message message);

}
