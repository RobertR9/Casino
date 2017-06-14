package messaging;


import com.sun.media.jfxmedia.logging.Logger;
import library.Bet;
import library.Result;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class MessageSenderGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    public MessageSenderGateway(String channelName) {
        if (channelName == null) {
            throw new IllegalArgumentException("Channel name is null.");
        }
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

        props.put(("queue." + channelName), channelName);
        try {
            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            this.connection = connectionFactory.createConnection();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            this.destination = (Destination) jndiContext.lookup(channelName);
            this.producer = session.createProducer(this.destination);
        } catch (NamingException e) {
            Logger.logMsg(9000, e.getMessage());
        } catch (JMSException e) {
            Logger.logMsg(9000, e.getMessage());
        }
    }

    public void send(ObjectMessage message) throws JMSException {
        this.producer.send(message);
        System.out.println(
                "JMSMessageID=" + message.getJMSMessageID() + "\n" +
                        "JMSMessageCorId=" + message.getJMSCorrelationID() + "\n" +
                        "JMSType=" + message.getJMSType() + "\n" +
                        "JMSDestination=" + message.getJMSDestination() + "\n" +
                        "Object: " + (message).getObject() + "\n");
    }

    public ObjectMessage createObjectMessage(Result result) throws JMSException {
        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setObject(result);
        objectMessage.setJMSType("BetResult");
        return objectMessage;
    }

    public ObjectMessage createObjectMessage(Bet bet) throws JMSException {
        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setJMSType("Bet");
        objectMessage.setObject(bet);

        return objectMessage;
    }

    public ObjectMessage createObjectMessage(Bet bet, ObjectMessage oldMessage) throws JMSException {
        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setJMSType("BetResult");
        objectMessage.setObject(bet);
        objectMessage.setJMSCorrelationID(oldMessage.getJMSMessageID());
        return objectMessage;
    }
}
