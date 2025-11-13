package com.codewithmosh.store.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.jsf.FacesContextUtils;

import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper UserMapper;
    
    @GetMapping
    public Iterable<UserDto> getAllUsers(
        @RequestParam(required = false, defaultValue = "", name = "sort") String sort
    ){
        if (!Set.of("name", "email").contains(sort)){
            sort = "name";
        }

        return this.userRepository.findAll(Sort.by(sort))
                   .stream()
                   .map(UserMapper::toDTO)
                   .toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        var user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.toDTO(user));  
    }
}
