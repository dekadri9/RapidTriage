package com.example.RapidTriage.Beans;

import java.time.LocalDate;
import java.util.List;

import com.example.RapidTriage.Input.UserInput;
import com.example.RapidTriage.Models.User;
import com.example.RapidTriage.Repositories.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfig {
    User user;

    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {
            UserInput userDetails = new UserInput("demoEmail@gmail.com", "demoName", "demoUsername", "demoPassword", LocalDate.of(1998, 12, 2));
            user = new User(userDetails.getEmail(), userDetails.getName(), userDetails.getUsername(), userDetails.getPassword(), userDetails.getBirthDate());
            user.setToken("AUTH");
        };
    }
}
