package Consumers;

import TopicCommunicate.Subscriber;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class OffSubscriber {

	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;

	public OffSubscriber(String brokerUrl) throws Exception {
		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		connection = connectionFactory.createConnection();
		connection.setClientID("durableSubscriberClient");
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public void start() throws Exception {
		Topic topic = session.createTopic("myTopic");
		String subscriptionName = "example.subscription";
		MessageConsumer consumer = session.createDurableSubscriber(topic, subscriptionName);

		while (true) {
			Message message = consumer.receive();
			TextMessage textMess = (TextMessage) message;
			String text = textMess.getText();
			if (message != null) {
				System.out.println("Đã nhận tin nhắn: " + text);
			} else {
				Thread.sleep(1000);
			}
		}
	}

	public void stop() throws Exception {
		connection.close();
	}

	public static void main(String[] args) throws Exception {

		OffSubscriber subscriber = null;
		try {
			subscriber = new OffSubscriber("tcp://192.168.137.87:61616");
			subscriber.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (subscriber != null) {
				try {
					subscriber.stop();
				} catch (Exception e) {
				}
			}
		}

	}

}
