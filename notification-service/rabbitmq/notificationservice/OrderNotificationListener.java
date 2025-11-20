package kz.rabbitmq.notificationservice;

import kz.rabbitmq.orderservice.OrderDTO;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderNotificationListener {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderNotificationListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "all_orders_queue", durable = "true"),
            exchange = @Exchange(value = "${mq.order.topic.exchange}", type = ExchangeTypes.TOPIC),
            key = "order.#"))
    public void receiveAllOrders(OrderDTO order) {
        logger.info("===== NEW ORDER RECEIVED =====");
        logger.info("Restaurant: {}", order.getRestaurant());
        logger.info("Courier: {}", order.getCourier());
        logger.info("Foods: {}", order.getFoods());
        logger.info("Status: {}", order.getStatus());
        logger.info("==============================");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "almaty_orders_queue", durable = "true"),
            exchange = @Exchange(value = "${mq.order.topic.exchange}", type = ExchangeTypes.TOPIC),
            key = "order.almaty"))
    public void receiveAlmatyOrders(OrderDTO order) {
        logger.info("[ALMATY] Received order for restaurant: {}", order.getRestaurant());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "astana_orders_queue", durable = "true"),
            exchange = @Exchange(value = "${mq.order.topic.exchange}", type = ExchangeTypes.TOPIC),
            key = "order.astana"))
    public void receiveAstanaOrders(OrderDTO order) {
        logger.info("[ASTANA] Received order for restaurant: {}", order.getRestaurant());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "customer_updates_queue", durable = "true"),
            exchange = @Exchange(value = "${mq.order.fanout.exchange}", type = ExchangeTypes.FANOUT),
            key = ""))
    public void receiveCustomerOrderStatusUpdate(OrderDTO order) {
        logger.info("===== CUSTOMER NOTIFICATION =====");
        logger.info("Order status updated to: {}", order.getStatus());
        logger.info("Restaurant: {}", order.getRestaurant());
        logger.info("Foods: {}", order.getFoods());
        logger.info("=================================");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "courier_updates_queue", durable = "true"),
            exchange = @Exchange(value = "${mq.order.fanout.exchange}", type = ExchangeTypes.FANOUT),
            key = ""))
    public void receiveCourierOrderStatusUpdate(OrderDTO order) {
        logger.info("===== COURIER NOTIFICATION =====");
        logger.info("Order assigned to: {}", order.getCourier());
        logger.info("New status: {}", order.getStatus());
        logger.info("Restaurant: {}", order.getRestaurant());
        logger.info("================================");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "restaurant_updates_queue", durable = "true"),
            exchange = @Exchange(value = "${mq.order.fanout.exchange}", type = ExchangeTypes.FANOUT),
            key = ""))
    public void receiveRestaurantOrderStatusUpdate(OrderDTO order) {
        logger.info("===== RESTAURANT NOTIFICATION =====");
        logger.info("Restaurant: {}", order.getRestaurant());
        logger.info("Order status: {}", order.getStatus());
        logger.info("Courier: {}", order.getCourier());
        logger.info("===================================");
    }
}
