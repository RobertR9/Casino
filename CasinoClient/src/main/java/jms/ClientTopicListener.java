package jms;

import game.GameController;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerInfo;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClientTopicListener implements MessageListener {

    private GameController gameController;

    private Map<String, String> users = new HashMap<>();

    public ClientTopicListener(String channelName, GameController gameController) {
        this.gameController = gameController;
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("topic." + channelName), channelName);

            Context jndiContext = new InitialContext(props);
            TopicConnectionFactory connectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
            TopicConnection connection = connectionFactory.createTopicConnection();

            Session session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            connection.start();

            Topic messageTopic = (Topic) jndiContext.lookup(channelName);
            Destination advisoryDestination = AdvisorySupport.getConsumerAdvisoryTopic(messageTopic);
            MessageConsumer consumer = session.createConsumer(advisoryDestination);
            consumer.setMessageListener(this);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message msg) {
        if (msg instanceof ActiveMQMessage) {
            ActiveMQMessage aMsg = (ActiveMQMessage) msg;
            try {
                if (aMsg.getDataStructure() instanceof ConsumerInfo) {
                    ConsumerInfo consumerInfo = (ConsumerInfo) aMsg.getDataStructure();
                    users.put(consumerInfo.getClientId(), consumerInfo.getConsumerId().toString());
                }
            } catch (Exception ignored) {
                System.err.print(ignored.getMessage());
            }
        }
    }
}