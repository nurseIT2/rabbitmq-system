package kz.rabbitmq.notificationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(OrderNotificationListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "almaty_orders_queue"),
            exchange = @Exchange(value = "${mq.order.topic.exchange}", type = ExchangeTypes.TOPIC),
            key = "order.almaty"))
    public void handleAlmatyOrder(OrderDTO order) {
        log.info("Received Almaty order: {}", order);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "astana_orders_queue"),
            exchange = @Exchange(value = "${mq.order.topic.exchange}", type = ExchangeTypes.TOPIC),
            key = "order.astana"))
    public void handleAstanaOrder(OrderDTO order) {
        log.info("Received Astana order: {}", order);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "common_orders_queue"),
            exchange = @Exchange(value = "${mq.order.topic.exchange}", type = ExchangeTypes.TOPIC),
            key = "order.#"))
    public void handleAllOrders(OrderDTO order) {
        log.info("Received (common listener) order: {}", order);
    }
}
