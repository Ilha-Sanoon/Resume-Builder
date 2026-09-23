package com.foliofield.resume.repository;

import com.foliofield.resume.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmailIgnoreCase(String email);
    java.util.Optional<User> findByEmailIgnoreCase(String email);
}