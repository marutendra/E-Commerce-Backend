package com.Learning.Seller.Repository;

import com.Learning.Seller.Entity.Seller_Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Seller_Repository extends MongoRepository<Seller_Entity, String> {

    Optional<Seller_Entity> findByEmail(String Email);
}
