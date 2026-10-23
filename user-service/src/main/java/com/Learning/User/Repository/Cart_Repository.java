package com.Learning.User.Repository;

import com.Learning.User.Entity.Cart_Entity;   // CHANGED: new package
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Cart_Repository extends MongoRepository<Cart_Entity, String> {

    Optional<Cart_Entity> findByUserId(String userId);
}
