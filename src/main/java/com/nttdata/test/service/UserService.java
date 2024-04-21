package com.nttdata.test.service;

import com.nttdata.test.dto.request.UserDto;
import com.nttdata.test.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserEntity createUser(UserDto userDto);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findAll();
    Optional<UserEntity> findById(UUID uuid);
    void updateUser(UserDto userDto, UserEntity userEntity);
    void deleteById(UUID uuid);
    UserEntity changeState(UUID uuid);
    Optional<UserEntity> findByEmailAndPassword(String username, String password);
    void updateLastLogin(UUID uuid);
}
