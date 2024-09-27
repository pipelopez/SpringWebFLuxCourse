package com.example.demo.messaging.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {
	
	@RabbitListener(queues={"MQ_DEMO"})
	public void receiveManage(@Payload String message) {
		System.out.println("Received message: " + message);
	}

}
