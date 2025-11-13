package com.codewithmosh.store.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.UserRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    
    @GetMapping("/users")
    public Iterable<User> getAllUsers(){
        return this.userRepository.findAll();
    }
}
