package fyuway.ActiveMQDemo01;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ActiveMQ Hello world!
 *
 */
public class JmsProducer_Topic {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.11.129:61616";
	public static final String TOPIC_NAME = "topic1";
	
	//要先有Consumer存在才可由Producer發送topic, 否則沒有意義
	public static void main(String[] args) throws JMSException {
		
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic queue = session.createTopic(TOPIC_NAME);
		MessageProducer producer = session.createProducer(queue);
		
		for (int i = 0; i < 3; i++) {
			TextMessage textMessage = session.createTextMessage("TOPIC_NAME----" + i);
			producer.send(textMessage);
			System.out.println("send topic--- " + textMessage.getText());
		}
		
		producer.close();
		session.close();
		connection.close();
	}
}
