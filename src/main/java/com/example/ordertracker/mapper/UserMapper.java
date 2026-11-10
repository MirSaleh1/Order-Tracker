package com.example.ordertracker.mapper;


import com.example.ordertracker.entity.User;
import com.example.ordertracker.model.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);
}
