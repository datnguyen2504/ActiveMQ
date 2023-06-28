package TopicCommunicate;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicProducer {
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private Topic topic;
    private MessageProducer producer;
    
    public TopicProducer(String brokerUrl, String topicName) throws JMSException {
        // Tạo kết nối đến ActiveMQ
        factory = new ActiveMQConnectionFactory(brokerUrl);
        connection = factory.createConnection();
        
        // Tạo session để truy cập topic
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        // Tạo topic
        topic = session.createTopic(topicName);
        
        // Tạo producer để gửi tin nhắn lên topic
        producer = session.createProducer(topic);
    }
    
    public void send(String messageText) throws JMSException {
        // Tạo message
        TextMessage message = session.createTextMessage(messageText);
        
        // Gửi message lên topic
        producer.send(message);
    }
    
    public void close() throws JMSException {
        // Đóng producer, session và kết nối
        producer.close();
        session.close();
        connection.close();
    }
}
