package com.Learning.Order.Entity;

import lombok.Data;

// One product line inside an order. Stored inside the order document.
// Name and price are COPIED here on purpose: if the seller changes the
// price tomorrow, old orders must still show the price the buyer paid.
@Data
public class Order_Item {
    private String productId;
    private String productName;
    private double price;       // price of ONE item at the time of purchase
    private int quantity;
    private double itemTotal;   // price * quantity
}
