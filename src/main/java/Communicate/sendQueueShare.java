package Communicate;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class sendQueueShare {

    private String brokerUrl;
    private String sharedQueueName;
    
    private String BROKER_URLM = "tcp://10.0.134.242:61616"; // URL của ActiveMQ Broker
    private String DESTINATION = "shared.ExampleQueue"; // Tên của Queue để gửi tin nhắn

    public sendQueueShare(String brokerUrl, String sharedQueueName, String brokerURLM) {
        this.brokerUrl = brokerUrl;
        this.sharedQueueName = sharedQueueName;
        this.BROKER_URLM = brokerURLM;
    }

    public void sendMessage(String text) throws JMSException {
    	
    	try {
    		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URLM);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            
            try {
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination sharedQueue = session.createQueue(sharedQueueName);
                MessageProducer producer = session.createProducer(sharedQueue);
                TextMessage message = session.createTextMessage(text);
                producer.send(message);
            } finally {
                connection.close();
            }
            
    	}catch(JMSException e) {
    		
    		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            
            try {
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination sharedQueue = session.createQueue(sharedQueueName);
                MessageProducer producer = session.createProducer(sharedQueue);
                TextMessage message = session.createTextMessage(text);
                producer.send(message);
            } finally {
                connection.close();
            }
    	}
    	}
    	
        
    
    public static void main(String[] args) throws JMSException {
        String brokerUrl = "tcp://192.168.5.5:61616";
        String sharedQueueName = "shared.ExampleQueue";
        String BROKER_URLM = "tcp://192.168.137.87:61616";
        sendQueueShare producer = new sendQueueShare(brokerUrl, sharedQueueName,BROKER_URLM);
        producer.sendMessage("Hello, ActiveMQ!");
        producer.sendMessage("Hello, NguyenVanDat!");
        producer.sendMessage("Hello, DangTrongDanh!");
        producer.sendMessage("Hello, LeNgocPhuc!");
        producer.sendMessage("Hello, DuongThanhHai!");
    }
}