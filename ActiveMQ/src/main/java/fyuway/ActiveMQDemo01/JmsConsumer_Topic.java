package fyuway.ActiveMQDemo01;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ActiveMQ Hello world!
 *
 */
public class JmsConsumer_Topic {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.11.129:61616";
	public static final String TOPIC_NAME = "topic1";
	
	//要先有Consumer存在才可由Producer發送topic, 否則沒有意義
	public static void main(String[] args) throws JMSException, IOException {
		
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic queue = session.createTopic(TOPIC_NAME);
		MessageConsumer consumer = session.createConsumer(queue);
		
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				if(message != null && message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						System.out.println("receive TOPIC_NAME--- " + textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		System.in.read();  //System Pause, let console do not close.
		
		consumer.close();
		session.close();
		connection.close();
		
//		先啟動2個Consumer(Ac,Bc), 再由Producer發布6條message(m1,m2,m3,m4,m5,m6),  2個Consumer會一人消費一半(Ac:m1,m3,m5  Bc:m2,m4,m6)
	}
}
