package com.Learning.User.DTO;

import lombok.Data;

@Data
public class Return_type_dto {
    private String productid;
    private String productName;
    private double price;        // price of ONE item
    private int quantity;
    private double total_price;  // price * quantity
}
