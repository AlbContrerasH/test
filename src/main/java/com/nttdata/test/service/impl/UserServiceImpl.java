package com.nttdata.test.service.impl;

import com.nttdata.test.dto.request.UserDto;
import com.nttdata.test.entity.PhoneEntity;
import com.nttdata.test.entity.UserEntity;
import com.nttdata.test.repository.UserRepository;
import com.nttdata.test.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nttdata.test.util.TokenJwtConfig.SECRET_KEY;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserEntity createUser(UserDto userDto) {

        // Generando token
        String token = Jwts.builder()
                .subject(userDto.getEmail())
                .signWith(SECRET_KEY)
                .compact();

        // Se agrega nuevo usuario
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
        user.setToken(token); //TODO get token

        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public void updateUser(UserDto userDto, UserEntity userEntity) {
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

        userRepository.save(user);
    }

    @Override
    public void deleteById(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    @Override
    public UserEntity changeState(UUID uuid) {
        // buscar usuario
        UserEntity userEntity = userRepository.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

        // Se setea el cambio isActive al estado contrario
        userEntity.setActive(!userEntity.isActive());
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findByEmailAndPassword(String username, String password) {
        return userRepository.findByEmailAndPassword(username, password);
    }

    @Override
    public void updateLastLogin(UUID uuid) {
        UserEntity userEntity = userRepository.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));
        userEntity.setLastLogin(new Date());
        userRepository.save(userEntity);
    }
}
