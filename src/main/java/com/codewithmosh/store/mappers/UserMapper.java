package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;

import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDTO(User user);
}
