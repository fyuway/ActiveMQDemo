package fyuway.ActiveMQDemo01;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ActiveMQ Hello world!
 *
 */
public class JmsProducer {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.11.129:61616";
	public static final String QUEUE_NAME = "queue01";
	
	public static void main(String[] args) throws JMSException {
		
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		
		for (int i = 0; i < 3; i++) {
			TextMessage textMessage = session.createTextMessage("msg----" + i);
			producer.send(textMessage);
			System.out.println("send--- " + textMessage.getText());
		}
		
		producer.close();
		session.close();
		connection.close();
	}
}
