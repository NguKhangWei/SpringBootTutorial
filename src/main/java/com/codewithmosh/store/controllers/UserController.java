package com.codewithmosh.store.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.jsf.FacesContextUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.codewithmosh.store.dtos.RegisterUserRequest;
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
    public List<UserDto> getAllUsers(
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

    @PostMapping
    public ResponseEntity<UserDto> createUser(
        @RequestBody RegisterUserRequest request,
        UriComponentsBuilder uriBuilder
    ){
        var user = UserMapper.toEntity(request);
        this.userRepository.save(user);

        var UserDto = this.UserMapper.toDTO(user);
        // The path is not obtained from Request Mapping, so need to provide /users/{id}
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(UserDto.getId()).toUri();
        return ResponseEntity.created(uri).body(UserDto);
    }
}
