package kz.rabbitmq.orderservice;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {
    
    private String restaurant;
    private String courier;
    private List<String> foods;
    private String status;
    
    public OrderDTO() {
    }
    
    public OrderDTO(String restaurant, String courier, List<String> foods, String status) {
        this.restaurant = restaurant;
        this.courier = courier;
        this.foods = foods;
        this.status = status;
    }
    
    public String getRestaurant() {
        return restaurant;
    }
    
    public String getCourier() {
        return courier;
    }
    
    public List<String> getFoods() {
        return foods;
    }
    
    public String getStatus() {
        return status;
    }
}
