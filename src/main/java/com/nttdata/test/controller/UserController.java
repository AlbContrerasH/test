package com.nttdata.test.controller;

import com.nttdata.test.dto.request.UserDto;
import com.nttdata.test.dto.response.ActiveResponse;
import com.nttdata.test.dto.response.UuidResponse;
import com.nttdata.test.entity.UserEntity;
import com.nttdata.test.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("user")
@Validated
public class UserController {

    // TODO: Excepciones controladas

    @Autowired
    UserService userService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    // Anadiendo usuario
    @PostMapping
    public ResponseEntity post(@RequestBody UserDto userDto) throws BadRequestException {
        // Validaciones
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if(!userDto.getEmail().matches(emailRegex)) {
            throw new BadRequestException("Error creacion Usuario: Correo no cuenta con formato correspondiente.");
        }

        String passwordRegex = "^(?=(?:.*\\d){2})(?=.*[A-Z])(?=.*[!@#$%^&*()-_+=])[A-Za-z\\d!@#$%^&*()-_+=]{8,}$"; // TODO revisar
        if(!userDto.getPassword().matches(passwordRegex)) {
            throw new BadRequestException("Error creacion Usuario: Contrasena invalida");
        }

        if(userService.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BadRequestException("Error creacion Usuario: El correo ya sido registrado");
        }
        UserEntity newUser = userService.createUser(userDto);

        return new ResponseEntity(new UuidResponse(newUser.getUuid().toString()), HttpStatus.CREATED);
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAll() {
        List<UserEntity> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{uuidString}")
    public ResponseEntity<UserEntity> get(@PathVariable UUID uuidString) throws BadRequestException {
        UserEntity user = userService.findById(uuidString).orElseThrow(() -> new BadRequestException("Error obteniendo Usuario: Usuario no encontrado"));
        return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
    }

    @PutMapping("/{uuidString}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@PathVariable UUID uuidString, @RequestBody UserDto userDto) throws BadRequestException {
        // Validaciones
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if(!userDto.getEmail().matches(emailRegex)) {
            throw new BadRequestException("Error actualizando Usuario: Correo no cuenta con formato correspondiente.");
        }

        String passwordRegex = "^(?=(?:.*\\d){2})(?=.*[A-Z])(?=.*[!@#$%^&*()-_+=])[A-Za-z\\d!@#$%^&*()-_+=]{8,}$"; // TODO revisar
        if(!userDto.getPassword().matches(passwordRegex)) {
            throw new BadRequestException("Error actualizando Usuario: Contrasena invalida");
        }

        // Buscar el usuario en la bdd
        UserEntity userEntity = userService.findById(uuidString).orElseThrow(() -> new BadRequestException("Error actualizando Usuario: Usuario no encontrado"));

        // Se setean los datos nuevos
        userService.updateUser(userDto, userEntity);
    }

    @DeleteMapping("/{uuidString}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuidString) {
        userService.deleteById(uuidString);
    }

    @PatchMapping("/{uuidString}")
    public ResponseEntity<ActiveResponse> active(@PathVariable UUID uuidString) {
        UserEntity userEntity = userService.changeState(uuidString);
        return new ResponseEntity<>(new ActiveResponse(userEntity.isActive()), HttpStatus.OK);
    }
}
