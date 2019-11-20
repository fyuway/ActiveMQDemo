package fyuway.ActiveMQDemo01;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ActiveMQ Hello world!
 *
 */
public class JmsConsumer {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.11.129:61616";
	public static final String QUEUE_NAME = "queue01";
	
	public static void main(String[] args) throws JMSException, IOException {
		
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);
		
		String type = "監聽法";
		
		if ("阻塞法".equals(type)) {//同步阻塞法
			while(true) {
				TextMessage textMessage = (TextMessage) consumer.receive();
//			TextMessage textMessage = (TextMessage) consumer.receive(5000);
//			TextMessage textMessage = (TextMessage) consumer.receiveNoWait();
				if(textMessage != null) {
					System.out.println("receive--- " + textMessage.getText());
				} else {
					break;
				}
			}
		} 
		
		if ("監聽法".equals(type)) {//異步非阻塞法(Listener on Message)
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					if(message != null && message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;
						try {
							System.out.println("receive by MessageListener--- " + textMessage.getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			System.in.read();  //System Pause, let console do not close.
		}
		
		consumer.close();
		session.close();
		connection.close();
		
//		先啟動2個Consumer(Ac,Bc), 再由Producer發布6條message(m1,m2,m3,m4,m5,m6),  2個Consumer會一人消費一半(Ac:m1,m3,m5  Bc:m2,m4,m6)
	}
}
