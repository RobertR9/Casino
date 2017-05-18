package messaging;

import com.sun.media.jfxmedia.logging.Logger;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Arrays;

public class MessageReceiverGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;

    public MessageReceiverGateway(String channelName) {
        try {
            if (channelName == null) {
                throw new IllegalArgumentException("Channel name is null.");
            }
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connectionFactory.setTrustedPackages(Arrays.asList("messaging", "model.loan", "model.bank"));

            this.connection = connectionFactory.createConnection();
            this.connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            this.destination = session.createQueue(channelName);
            this.consumer = session.createConsumer(destination);
        } catch (JMSException e) {
            Logger.logMsg(9000, e.getMessage());
        }
    }

    public void setListener(MessageListener messageListener) {
        try {
            consumer.setMessageListener(messageListener);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
