package kz.rabbitmq.orderservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher {
    private static final Logger log = LoggerFactory.getLogger(OrderPublisher.class);
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${mq.order.topic.exchange}")
    private String topicExchange;

    public OrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(OrderDTO order, String region) {
        String routingKey = "order." + region;
        rabbitTemplate.convertAndSend(topicExchange, routingKey, order);
        log.info("Order sent to region {}: {}", region, order);
    }
}
