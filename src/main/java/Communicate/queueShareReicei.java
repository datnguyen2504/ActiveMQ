package Communicate;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class queueShareReicei {

	
	
    
    private String brokerUrl;
    private String sharedQueueName;
    
    public queueShareReicei(String brokerUrl, String sharedQueueName) {
        this.brokerUrl = brokerUrl;
        this.sharedQueueName = sharedQueueName;
    }
    
  

    public void receiveMessages() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        
        
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination sharedQueue = session.createQueue(sharedQueueName);
            MessageConsumer consumer = session.createConsumer(sharedQueue);
            

            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received message: " + text);
                }
                                
            }
        
    }
    
    private static final String BROKER_URL = "tcp://192.168.137.87:61616"; // URL của ActiveMQ Broker chính
    private static final String DESTINATION = "shared.ExampleQueue"; // Tên của Queue để nhận tin nhắn
    
	public static void main(String[] args) throws JMSException {

		try {
			String brokerUrl = BROKER_URL;
			String sharedQueueName = DESTINATION;
			queueShareReicei consumer = new queueShareReicei(brokerUrl, sharedQueueName);
			consumer.receiveMessages();
		} catch (JMSException a2) {

		}

		try {

			String brokerUrl = BROKER_URL;
			String sharedQueueName = DESTINATION;
			queueShareReicei consumer = new queueShareReicei(brokerUrl, sharedQueueName);
			consumer.receiveMessages();

		} catch (JMSException a) {

			try {
				String brokerUrl = "tcp://192.168.5.5:61616";
				String sharedQueueName = "shared.ExampleQueue";
				queueShareReicei consumer = new queueShareReicei(brokerUrl, sharedQueueName);
				consumer.receiveMessages();
			} catch (JMSException a2) {

			}

		}

	}
}
