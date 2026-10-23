package com.Learning.Order.Repository;

import com.Learning.Order.Entity.Order_Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Order_Repository extends MongoRepository<Order_Entity, String> {

    // newest order first
    List<Order_Entity> findByUserIdOrderByOrderDateDesc(String userId);
}
