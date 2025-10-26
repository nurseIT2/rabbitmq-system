package kz.rabbitmq.orderservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderPublisher publisher;

    public OrderController(OrderPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/{region}")
    public ResponseEntity<String> createOrder(@PathVariable String region, @RequestBody OrderDTO order) {
        publisher.sendOrder(order, region);
        return ResponseEntity.ok("Order sent to region: " + region);
    }
}
