package com.nttdata.test.controller;

import com.nttdata.test.dto.request.PhoneDto;
import com.nttdata.test.dto.request.UserDto;
import com.nttdata.test.dto.response.ActiveResponse;
import com.nttdata.test.entity.PhoneEntity;
import com.nttdata.test.entity.UserEntity;
import com.nttdata.test.service.UserService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHealthCheck() {
        ResponseEntity<String> response = userController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ok", response.getBody());
    }

    @Test
    public void testValidUserCreation() throws BadRequestException {
        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "alberto@contreras.cl", "123Asdf#", phoneDtoList);

        // Mock
        UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.fromString("a37bf07f-c32d-454a-9adf-0890a2a2c877"));
        when(userService.findByEmail(userDto.getEmail())).thenReturn(java.util.Optional.empty());
        when(userService.createUser(userDto)).thenReturn(userEntity);

        ResponseEntity response = userController.post(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testEmailFormatError() {
        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "albertocontreras.cl", "123Asdf#", phoneDtoList);
        assertThrows(BadRequestException.class, () -> userController.post(userDto));
    }

    @Test
    public void testInvalidPasswordFormatPost() {
        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "alberto@contreras.cl", "1234", phoneDtoList);
        assertThrows(BadRequestException.class, () -> userController.post(userDto));
    }

    @Test
    public void testGetAllUsers() {
        List<PhoneEntity> phoneEntityList = List.of(
                new PhoneEntity(1L, 52071373L, 9, 56)
        );
        List<UserEntity> userList = Arrays.asList(
                new UserEntity(UUID.fromString("a37bf07f-c32d-454a-9adf-0890a2a2c877"), "Alberto Contreras", "alberto@contreras.cl", "123Asdf#", new Date(), new Date(), new Date(), "token", true, phoneEntityList)
        );
        when(userService.findAll()).thenReturn(userList);

        ResponseEntity<List<UserEntity>> response = userController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    public void testGetAllUsersEmptyList() {
        List<UserEntity> emptyList = Arrays.asList();
        when(userService.findAll()).thenReturn(emptyList);

        ResponseEntity<List<UserEntity>> response = userController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyList, response.getBody());
    }

    @Test
    public void testDeleteUser() {
        UUID uuid = UUID.randomUUID();
        userController.delete(uuid);
        verify(userService).deleteById(uuid);
    }

    @Test
    public void testActivateUser() {
        UUID uuid = UUID.randomUUID();
        boolean activeState = true;

        UserEntity userEntity = new UserEntity();
        userEntity.setActive(activeState);
        when(userService.changeState(uuid)).thenReturn(userEntity);

        ResponseEntity<ActiveResponse> response = userController.active(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeState, Objects.requireNonNull(response.getBody()).isActive());
    }

    @Test
    public void testUpdateUser() throws BadRequestException {
        UUID uuid = UUID.randomUUID();
        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "alberto@contreras.cl", "123Asdf#", phoneDtoList);

        UserEntity userEntity = new UserEntity();
        when(userService.findById(uuid)).thenReturn(Optional.of(userEntity));

        userController.put(uuid, userDto);

        verify(userService).updateUser(userDto, userEntity);
    }

    @Test
    public void testInvalidPasswordFormatPut() {
        UUID uuid = UUID.randomUUID();
        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "alberto@contreras.cl", "1234", phoneDtoList);
        assertThrows(BadRequestException.class, () -> userController.put(uuid, userDto));
    }

    @Test
    public void testUserPostNotFound() {
        UUID uuid = UUID.randomUUID();
        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "alberto@contreras.cl", "123Asdf#", phoneDtoList);
        when(userService.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new UserEntity()));

        assertThrows(BadRequestException.class, () -> userController.post(userDto));
    }

    @Test
    public void testUserPutNotFound() {
        UUID uuid = UUID.randomUUID();
        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "alberto@contreras.cl", "123Asdf#", phoneDtoList);
        when(userService.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userController.put(uuid, userDto));
    }

    @Test
    public void testGetUser() throws BadRequestException {
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();

        when(userService.findById(uuid)).thenReturn(Optional.of(userEntity));
        ResponseEntity<UserEntity> response = userController.get(uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
    }

    @Test
    public void testUserSingleNotFound() {
        UUID uuid = UUID.randomUUID();
        when(userService.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> userController.get(uuid));
    }
}
