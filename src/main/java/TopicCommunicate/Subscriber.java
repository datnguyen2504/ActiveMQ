package TopicCommunicate;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Subscriber implements MessageListener {
	
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private Topic topic;
    private MessageConsumer consumer;
    
    public Subscriber(String brokerUrl, String topicName) throws JMSException {
        // Tạo kết nối đến ActiveMQ
        factory = new ActiveMQConnectionFactory(brokerUrl);
        connection = factory.createConnection();
        
        // Tạo session để truy cập topic
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        // Tạo topic
        topic = session.createTopic(topicName);
        
        // Tạo consumer để nhận tin nhắn từ topic
        consumer = session.createConsumer(topic);
        
        // Thiết lập message listener để lắng nghe tin nhắn
        consumer.setMessageListener(this);
        
        // Bắt đầu kết nối
        connection.start();
    }
    
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text = ((TextMessage) message).getText();
                System.out.println("Received message: " + text);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void close() throws JMSException {
        // Đóng consumer, session và kết nối
        consumer.close();
        session.close();
        connection.close();
    }
}
