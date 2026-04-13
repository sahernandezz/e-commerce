package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.user.persisten.entity.*;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IRoleQueryRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private IUserCommandRepository userCommandRepository;

    @Mock
    private IUserQueryRepository userQueryRepository;

    @Mock
    private IRoleQueryRepository roleQueryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(2)
                .name("USER")
                .status(RoleStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Debe crear un usuario correctamente")
    void shouldCreateUserSuccessfully() {
        when(userQueryRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());
        when(roleQueryRepository.findById(2)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userCommandRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User result = createUserUseCase.execute("Test User", "test@email.com", "password123", 2);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@email.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        assertEquals(role, result.getRole());

        verify(userCommandRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el email ya está en uso")
    void shouldThrowWhenEmailAlreadyExists() {
        User existingUser = User.builder().id(1L).email("test@email.com").build();
        when(userQueryRepository.findByEmail("test@email.com")).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalStateException.class,
                () -> createUserUseCase.execute("Test", "test@email.com", "pass", 2));
        verify(userCommandRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando el rol no existe")
    void shouldThrowWhenRoleNotFound() {
        when(userQueryRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
        when(roleQueryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> createUserUseCase.execute("New User", "new@email.com", "pass", 99));
        verify(userCommandRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe encriptar la contraseña del usuario")
    void shouldEncryptPassword() {
        when(userQueryRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());
        when(roleQueryRepository.findById(2)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("rawPassword")).thenReturn("$2a$encoded");
        when(userCommandRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = createUserUseCase.execute("User", "test@email.com", "rawPassword", 2);

        assertEquals("$2a$encoded", result.getPassword());
        verify(passwordEncoder).encode("rawPassword");
    }
}

