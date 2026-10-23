package com.Learning.User.Repository;

import com.Learning.User.Entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface User_Repository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
}
