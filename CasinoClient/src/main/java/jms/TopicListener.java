package jms;

import library.Result;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerInfo;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TopicListener implements MessageListener {

    private static Session session;
//    private Map<String, String> users = new HashMap<>();

    public TopicListener(String channelName) {
        try {
            System.err.print("Constcoasdasdtor");
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("topic." + channelName), channelName);

            Context jndiContext = new InitialContext(props);
            TopicConnectionFactory connectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
            TopicConnection connection = connectionFactory.createTopicConnection();

            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

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
    public void onMessage(Message message) {
        System.err.print("onMessage: " + message);
        if (message instanceof ActiveMQMessage) {
            ActiveMQMessage aMsg = (ActiveMQMessage) message;

            try {
                if (message instanceof ObjectMessage) {
                    System.err.print("asdasda");
                    try {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        if (message.getJMSType().equals("Result")) {
                            System.err.print("ObjectMessage: " + objectMessage.getObject());
                            Result result = (Result) objectMessage.getObject();
                        } else {
                            throw new RuntimeException("Wrong type Message");
                        }
                    } catch (JMSException e) {
                        Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
                    }
                }
                if (aMsg.getDataStructure() instanceof ConsumerInfo) {
                    ConsumerInfo consumerInfo = (ConsumerInfo) aMsg.getDataStructure();
                    System.out.println(consumerInfo);
//                    users.put(consumerInfo.getClientId(), consumerInfo.getConsumerId().toString());
                    System.out.println(consumerInfo.getConsumerId().toString());
//                    mIRCController.notifyUserListAdd(consumerInfo.getClientId());
                }
//                else if (aMsg.getDataStructure() instanceof RemoveInfo) {
//                    RemoveInfo removeInfo = (RemoveInfo) aMsg.getDataStructure();
//                    String objectID = removeInfo.getObjectId().toString();
////                    for (Map.Entry<String, String> entry : users.entrySet()) {
////                        System.out.println("in loop");
////                        String username = entry.getKey();
////                        String consumerID = entry.getValue();
////                        System.out.println(objectID);
////                        if (consumerID.equals(objectID)) {
////                            mIRCController.notifyUserListRemove(username);
////                        }
////                    }
//                }
            } catch (Exception ignored) {
            }
        }
    }
}