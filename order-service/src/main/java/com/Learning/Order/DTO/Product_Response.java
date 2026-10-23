package com.Learning.Order.DTO;

import lombok.Data;

// Matches Product_DTO of the Seller service
@Data
public class Product_Response {
    private String productId;
    private String productName;
    private double price;
    private int quantity;   // stock
}
