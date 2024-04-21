package com.nttdata.test.service;

import com.nttdata.test.dto.request.PhoneDto;
import com.nttdata.test.dto.request.UserDto;
import com.nttdata.test.entity.UserEntity;
import com.nttdata.test.repository.UserRepository;
import com.nttdata.test.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(response -> response.getArgument(0));

        List<PhoneDto> phoneDtoList = List.of(
                new PhoneDto(52071373L, 9, 56)
        );
        UserDto userDto = new UserDto("Alberto Contreras", "alberto@contreras.cl", "123Asdf#", phoneDtoList);

        userService.createUser(userDto);

        verify(passwordEncoder).encode("123Asdf#");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void testFindByEmail() {
        String email = "alberto@contreras.cl";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        Optional<UserEntity> result = userService.findByEmail(email);
        verify(userRepository).findByEmail(email);
        assertEquals(Optional.of(userEntity), result);
    }

    @Test
    public void testFindAll() {
        List<UserEntity> userEntityList = Arrays.asList(
                new UserEntity(),
                new UserEntity()
        );

        when(userRepository.findAll()).thenReturn(userEntityList);
        List<UserEntity> result = userService.findAll();
        verify(userRepository).findAll();
        assertEquals(userEntityList, result);
    }

    @Test
    public void testFindById() {
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));
        Optional<UserEntity> result = userService.findById(uuid);
        verify(userRepository).findById(uuid);
        assertEquals(Optional.of(userEntity), result);
    }

    @Test
    public void testFindByEmailAndPassword() {
        String email = "test@example.com";
        String password = "password";
        UserEntity userEntity = new UserEntity();

        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.findByEmailAndPassword(email, password);

        verify(userRepository).findByEmailAndPassword(email, password);
        assertEquals(Optional.of(userEntity), result);
    }

    @Test
    public void testUpdateLastLogin() {
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setLastLogin(new Date());

        when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));

        userService.updateLastLogin(uuid);

        verify(userRepository).findById(uuid);
        verify(userRepository).save(userEntity);
    }

    @Test
    public void testUserNotFound() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.updateLastLogin(uuid));
    }

    @Test
    public void testUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setName("Test User");
        userDto.setPassword("password");
        PhoneDto phoneDto = new PhoneDto(52071373L, 9, 56);
        userDto.setPhones(Arrays.asList(phoneDto));

        UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID());
        userEntity.setCreated(new Date());
        userEntity.setLastLogin(new Date());
        userEntity.setToken("oldToken");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userService.updateUser(userDto, userEntity);

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void testDeleteById() {
        UUID uuid = UUID.randomUUID();
        userService.deleteById(uuid);
        verify(userRepository).deleteById(uuid);
    }

    @Test
    public void testChangeState() {
        // Mocking user entity and repository
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setActive(false);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity result = userService.changeState(uuid);

        verify(userRepository).findById(uuid);
        assertTrue(userEntity.isActive());
        verify(userRepository).save(userEntity);
    }
}
