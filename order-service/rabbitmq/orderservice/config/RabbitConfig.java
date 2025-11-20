package kz.rabbitmq.orderservice.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange("order-topic-exchange");
    }

    @Bean
    public FanoutExchange orderFanoutExchange() {
        return new FanoutExchange("order-update-exchange");
    }
}

// Также отправляем в другой регион (например, если отправили в almaty, то также в astana и наоборот)
// String otherRegion = region.equals("almaty") ? "astana" : "almaty";
// String otherRoutingKey = "order." + otherRegion;
// logger.info("Also sending order to region: {} with routing key: {}", otherRegion, otherRoutingKey);
// rabbitTemplate.convertAndSend(topicExchange, otherRoutingKey, order);

// logger.info("Order sent successfully to both regions: {}", order);

//добавление нового региона
// String[] allRegions = {"almaty", "astana", "shymkent"};

// for (String reg : allRegions) {
//     String routingKey = "order." + reg;
//     logger.info("Sending order to region: {} with routing key: {}", reg, routingKey);
//     rabbitTemplate.convertAndSend(topicExchange, routingKey, order);
// }

// logger.info("Order sent to all regions: {}", order);