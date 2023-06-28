package Consumers;

import TopicCommunicate.Subscriber;

public class Consumer2 {
	public static void main(String[] args) throws Exception {

		Subscriber subscriber2 = new Subscriber("tcp://localhost:61616", "myTopic");
		System.out.println("Waiting for messages...");
		System.in.read();
		subscriber2.close();
	}
}
