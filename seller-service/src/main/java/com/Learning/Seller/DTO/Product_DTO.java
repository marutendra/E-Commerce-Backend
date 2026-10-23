package com.Learning.Seller.DTO;

import lombok.Data;

@Data
public class Product_DTO {
    private String productId;
    private String productName;
    private String productImage;
    private String title;
    private String description;
    private double price;
    private int quantity;   // current stock
}
