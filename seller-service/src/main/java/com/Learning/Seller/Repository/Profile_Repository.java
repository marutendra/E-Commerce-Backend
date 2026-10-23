package com.Learning.Seller.Repository;

import com.Learning.Seller.Entity.Profile_Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Profile_Repository extends MongoRepository<Profile_Entity , String>{
}
