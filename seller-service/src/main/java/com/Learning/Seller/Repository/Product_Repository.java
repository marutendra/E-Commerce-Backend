package com.Learning.Seller.Repository;

import com.Learning.Seller.Entity.Product_Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Product_Repository extends MongoRepository<Product_Entity, String> {

    // all products of one seller

    List<Product_Entity> findByUserid(String userid);
}
