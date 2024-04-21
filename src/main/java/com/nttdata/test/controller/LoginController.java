package com.nttdata.test.controller;

import com.nttdata.test.dto.request.LoginDto;
import com.nttdata.test.dto.response.MessageResponse;
import com.nttdata.test.entity.UserEntity;
import com.nttdata.test.repository.UserRepository;
import com.nttdata.test.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("auth/login")
public class LoginController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;
    @PostMapping
    public ResponseEntity<MessageResponse> login(@RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();

        Optional<UserEntity> user = userService.findByEmail(username);
        if(user.isEmpty() || !passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword())){
            throw new BadCredentialsException("Credenciales invalidas");
        }
        if(!user.get().isActive()) {
            throw new BadCredentialsException("Usuario deshabilitado");
        }
        userService.updateLastLogin(user.get().getUuid());
        return new ResponseEntity(new MessageResponse(String.format("Bienvenido %s", user.get().getName())), HttpStatus.OK);
    }
}
