package kz.rabbitmq.notificationservice.config;

import org.springframework.amqp.core.*;
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
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("orders_queue.dlq").build();
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return ExchangeBuilder.topicExchange("dlx").durable(true).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dlx.orders");
//        Егер DLX exchange-ке dlx.orders деген routing key-мен хабар келсе → оны DLQ-ға сал.
    }

    @Bean
    public Queue ordersQueueWithDLQ() {
        return QueueBuilder.durable("orders_main_queue")
                .withArgument("x-dead-letter-exchange", "dlx")
                .withArgument("x-dead-letter-routing-key", "dlx.orders")
                .build();
    }

    @Bean
    public Queue dlqRetryQueue() {
        return QueueBuilder.durable("orders_retry_queue").build();
    }
}