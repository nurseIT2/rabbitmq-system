package kz.rabbitmq.orderservice;

import kz.rabbitmq.orderservice.OrderDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    
    @Autowired
    public OrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // Установка JSON конвертера
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    @Value("${mq.order.topic.exchange}")
    private String topicExchange;

    @Value("${mq.order.fanout.exchange}")
    private String fanoutExchange;

    public void sendOrderToPrepare(OrderDTO order, String region) {
        String routingKey = "order." + region;
        logger.info("Sending order to region: {} with routing key: {}", region, routingKey);
        rabbitTemplate.convertAndSend(topicExchange, routingKey, order);
        logger.info("Order sent successfully: {}", order);

    }

    public void updateOrderStatus(OrderDTO orderDTO, String status) {
        orderDTO.setStatus(status);
        logger.info("Broadcasting order status update: {}", status);
        rabbitTemplate.convertAndSend(fanoutExchange, "", orderDTO);
        logger.info("Order status update sent: {}", orderDTO);
    }
}
