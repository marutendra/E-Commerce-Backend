package com.Learning.Order.Controller;

import com.Learning.Order.DTO.Place_Order_Request;
import com.Learning.Order.Entity.Order_Entity;
import com.Learning.Order.Entity.Order_Status;
import com.Learning.Order.Service.Order_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class Order_Controller {

    @Autowired
    Order_Service orderService;

    // Checkout: turns the user's cart into an order
    @PostMapping("/place")
    public Order_Entity place(@RequestBody Place_Order_Request request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/{orderId}")
    public Order_Entity getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<Order_Entity> getUserOrders(@PathVariable String userId) {
        return orderService.getOrdersByUser(userId);
    }

    @PutMapping("/cancel/{orderId}")
    public Order_Entity cancel(@PathVariable String orderId) {
        return orderService.cancelOrder(orderId);
    }

    // e.g. PUT /order/status/{orderId}?status=SHIPPED
    @PutMapping("/status/{orderId}")
    public Order_Entity updateStatus(@PathVariable String orderId,
                                     @RequestParam Order_Status status) {
        return orderService.updateStatus(orderId, status);
    }
}
