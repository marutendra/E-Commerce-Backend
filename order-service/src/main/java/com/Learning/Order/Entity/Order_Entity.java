package com.Learning.Order.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ORDERS")
@Data
public class Order_Entity {
    @Id
    private String orderId;
    private String userId;
    private List<Order_Item> items;
    private double totalAmount;
    private String shippingAddress;
    private Order_Status status;
    private LocalDateTime orderDate;
}
