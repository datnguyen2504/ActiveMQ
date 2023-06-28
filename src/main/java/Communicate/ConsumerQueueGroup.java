package Communicate;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;

public class ConsumerQueueGroup implements MessageListener{

	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private QueueReceiver queueReceiver;
	
	public ConsumerQueueGroup(String brokerUrl, String queueName) throws JMSException {
		// Khởi tạo kết nối và phiên làm việc với hàng đợi
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        queueConnection = connectionFactory.createQueueConnection();
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // Đăng ký đối tượng MessageListener để xử lý các tin nhắn được gửi đến hàng đợi
        Queue queue = queueSession.createQueue(queueName);
        queueReceiver = queueSession.createReceiver(queue);
        queueReceiver.setMessageListener(this);

        // Bắt đầu kết nối
        queueConnection.start();
		
	}
	
	
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage txtMessage = (TextMessage) message;

                // Lấy group ID từ tin nhắn
                String groupId = txtMessage.getStringProperty("JMSXGroupID");

                // Xử lý tin nhắn
                System.out.println("Received message with group ID: " + groupId + " - " + txtMessage.getText());

                // Xác nhận tin nhắn đã được xử lý và loại bỏ khỏi hàng đợi
                message.acknowledge();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() throws JMSException {
        // Đóng kết nối khi không sử dụng nữa
        queueReceiver.close();
        queueSession.close();
        queueConnection.close();
    }

    public static void main(String[] args) {
        try {
            String brokerUrl = "tcp://localhost:61616";
            String queueName = "myQueue";
            ConsumerQueueGroup messageGroupReceiver = new ConsumerQueueGroup(brokerUrl, queueName);

            System.out.println("Message group receiver is running...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
