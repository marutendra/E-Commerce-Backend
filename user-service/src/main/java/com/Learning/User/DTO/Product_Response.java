package com.Learning.User.DTO;

import lombok.Data;

// Matches Product_DTO of the Seller service
@Data
public class Product_Response {
    private String productId;
    private String productName;
    private String productImage;
    private String title;
    private String description;
    private double price;
    private int quantity;   // stock available
}
