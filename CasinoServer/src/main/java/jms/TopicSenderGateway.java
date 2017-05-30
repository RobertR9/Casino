package jms;

import library.Player;
import library.Lobby;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class TopicSenderGateway {

    private TopicSession session;
    private TopicPublisher producer;

    public TopicSenderGateway(String channelName) {
        if (channelName == null)
            throw new IllegalArgumentException("Channel name is null.");

        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("topic." + channelName), channelName);

            Context jndiContext = new InitialContext(props);
            TopicConnectionFactory connectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
            TopicConnection connection = connectionFactory.createTopicConnection();

            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic messageTopic = (Topic) jndiContext.lookup(channelName);
            producer = session.createPublisher(messageTopic);
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public Message createObjectMessage(Lobby lobby) {
        Message message = null;
        try {
            message = session.createObjectMessage();

        } catch (JMSException e) {

        }
        return message;
    }

    public ObjectMessage createObjectMessage(Player player) {
        ObjectMessage message = null;
        try {
            message = session.createObjectMessage();
            message.setObject(player);

        } catch (JMSException e) {
            e.printStackTrace();
        }
        return message;
    }

    public Message createTextMessage(String body) throws JMSException {
        return session.createTextMessage(body);
    }

    public void send(Message msg) throws JMSException {
        producer.publish(msg);
    }
}