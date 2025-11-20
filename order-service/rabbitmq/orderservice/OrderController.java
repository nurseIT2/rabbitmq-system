package kz.rabbitmq.orderservice;

import kz.rabbitmq.orderservice.OrderDTO;
import kz.rabbitmq.orderservice.OrderPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderPublisher orderPublisher;
    
    @Autowired
    public OrderController(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(
            @RequestBody OrderDTO orderDTO,
            @RequestParam(defaultValue = "almaty") String region) {
        try {
            orderPublisher.sendOrderToPrepare(orderDTO, region);
            return ResponseEntity.ok("Order created and sent to region: " + region);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create order: " + e.getMessage());
        }
    }

    @PutMapping("/status/{status}")
    public ResponseEntity<String> updateOrderStatus(
            @RequestBody OrderDTO orderDTO,
            @PathVariable String status) {
        try {
            orderPublisher.updateOrderStatus(orderDTO, status);
            return ResponseEntity.ok("Order status updated to: " + status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update order: " + e.getMessage());
        }
    }
}
