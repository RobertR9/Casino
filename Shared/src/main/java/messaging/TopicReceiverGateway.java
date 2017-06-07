package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Arrays;
import java.util.Properties;

public class TopicReceiverGateway {

    private TopicSubscriber topicSubscriber;

    public TopicReceiverGateway(String channelName, String clientID) {
        try {
            if (channelName == null) {
                throw new IllegalArgumentException("Channel name is null.");
            }

            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("topic." + channelName), channelName);

            Context jndiContext = new InitialContext(props);

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connectionFactory.setTrustedPackages(Arrays.asList("java.util", "java.lang", "library"));
            TopicConnection connection = connectionFactory.createTopicConnection();

            connection.setClientID(clientID);

            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic messageTopic = (Topic) jndiContext.lookup(channelName);
            topicSubscriber = session.createDurableSubscriber(messageTopic, "RouletteResults");

            connection.start();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public void setListener(MessageListener ml) throws JMSException {
        topicSubscriber.setMessageListener(ml);
    }
}