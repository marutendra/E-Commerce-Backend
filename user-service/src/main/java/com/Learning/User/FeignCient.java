package com.Learning.User;

import com.Learning.User.DTO.Product_Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SELLER-SERVICE")
public interface FeignCient {

    @GetMapping("/seller/show/{show}")
    List<Product_Response> show(@PathVariable("show") String show);

    // CHANGED: now calls the single /seller/product/{id} endpoint
    @GetMapping("/seller/product/{id}")
    Product_Response getProduct(@PathVariable("id") String id);
}
