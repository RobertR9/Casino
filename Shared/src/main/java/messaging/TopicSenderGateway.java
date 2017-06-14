package messaging;

import library.Result;
import org.apache.activemq.ActiveMQConnectionFactory;

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
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            TopicConnection connection = connectionFactory.createTopicConnection();

            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic messageTopic = (Topic) jndiContext.lookup(channelName);
            producer = session.createPublisher(messageTopic);
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public ObjectMessage createObjectMessage(Result result) {
        ObjectMessage objectMessage = null;
        try {
            objectMessage = session.createObjectMessage();
            objectMessage.setObject(result);
            objectMessage.setJMSType("Result");
            System.out.println(
                    "JMSMessageID=" + objectMessage.getJMSMessageID() + "\n" +
                            "JMStype=" + objectMessage.getJMSType() + "\n" +
                            "JMSDestination=" + objectMessage.getJMSDestination() + "\n" +
                            "Object: " + objectMessage.getObject() + "\n");
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return objectMessage;
    }

    public void send(Message msg) throws JMSException {
        producer.publish(msg);
    }
}