package com.example.demo.messaging.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {
	
	@Autowired
	private Queue queue;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessage(String message) {
		rabbitTemplate.convertAndSend(this.queue.getName(), message);
	}

}
