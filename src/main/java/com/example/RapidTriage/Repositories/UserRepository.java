package com.example.RapidTriage.Repositories;

import com.example.RapidTriage.Models.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);

    User getUserByUsername(String username);
    User getUserByEmail(String email);
    Boolean existsUserByEmail(String email);
    Boolean existsUserByUsername(String username);
}
