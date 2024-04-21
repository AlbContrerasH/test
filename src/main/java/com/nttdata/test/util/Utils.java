package com.nttdata.test.util;

import com.nttdata.test.dto.request.UserDto;
import com.nttdata.test.entity.PhoneEntity;
import com.nttdata.test.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class Utils {

    @Autowired
    static PasswordEncoder passwordEncoder;

    public static UserEntity setNewUserEntityFromDto(UserDto userDto) {
        // Fecha creacion y last login
        Date created = new Date();

        // Se instancia entidad user
        UserEntity user = new UserEntity();

        // Set
        user.setEmail(userDto.getEmail()); // Main data
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Lista de phoneDto a PhoneEntity
        user.setPhones(userDto.getPhones().stream().map(phoneDto -> {
            PhoneEntity phoneEntity = new PhoneEntity();
            phoneEntity.setCityCode(phoneDto.getCityCode());
            phoneEntity.setNumber(phoneDto.getNumber());
            phoneEntity.setCountryCode(phoneDto.getCountryCode());
            return phoneEntity;
        }).collect(Collectors.toList()));

        user.setCreated(created);
        user.setLastLogin(created);
        user.setActive(true);
        user.setToken("asdf"); //TODO get token

        return user;
    }

    public static UserEntity setModifiedUserEntityFromDto(UserDto userDto, UserEntity userEntity) {
        // Fecha creacion y last login
        Date modified = new Date();

        // Se instancia entidad user
        UserEntity user = new UserEntity();

        // Set
        user.setEmail(userDto.getEmail()); // Main data
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());

        // Lista de phoneDto a PhoneEntity
        user.setPhones(userDto.getPhones().stream().map(phoneDto -> {
            PhoneEntity phoneEntity = new PhoneEntity();
            phoneEntity.setCityCode(phoneDto.getCityCode());
            phoneEntity.setNumber(phoneDto.getNumber());
            phoneEntity.setCountryCode(phoneDto.getCountryCode());
            return phoneEntity;
        }).collect(Collectors.toList()));

        user.setUuid(userEntity.getUuid());
        user.setCreated(userEntity.getCreated());
        user.setModified(modified);
        user.setLastLogin(userEntity.getLastLogin());
        user.setActive(true);
        user.setToken(userEntity.getToken());

        return user;
    }
}
