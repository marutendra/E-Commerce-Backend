package com.Learning.Order.Entity;

// The order of these values matters: an order can only move forward.
public enum Order_Status {
    PLACED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
