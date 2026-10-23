package com.Learning.Order.DTO;

import lombok.Data;

// Body of POST /order/place
@Data
public class Place_Order_Request {
    private String userId;
    private String shippingAddress;
}
