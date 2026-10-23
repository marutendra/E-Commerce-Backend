package com.Learning.Order.Feign;

import com.Learning.Order.DTO.Product_Response;
import com.Learning.Order.DTO.Stock_Update_Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "SELLER-SERVICE")
public interface Seller_Client {

    @GetMapping("/seller/product/{id}")
    Product_Response getProduct(@PathVariable("id") String id);

    @PutMapping("/seller/reduce-stock")
    void reduceStock(@RequestBody Stock_Update_Request request);

    @PutMapping("/seller/restore-stock")
    void restoreStock(@RequestBody Stock_Update_Request request);
}
