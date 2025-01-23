package com.budgetplaner.services;

import com.budgetplaner.dtos.LoginUserDto;
import com.budgetplaner.dtos.RegisterUserDto;
import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        registerUserDto = new RegisterUserDto();
        registerUserDto.setFullName("Test User");
        registerUserDto.setEmail("testuser@example.com");
        registerUserDto.setPassword("password123");

        loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("testuser@example.com");
        loginUserDto.setPassword("password123");

        user = new User();
        user.setFullName("Test User");
        user.setEmail("testuser@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void testSignup() {
        // Mock behavior
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Test signup functionality
        User signedUpUser = authenticationService.signup(registerUserDto);

        assertNotNull(signedUpUser);
        assertEquals("Test User", signedUpUser.getFullName());
        assertEquals("testuser@example.com", signedUpUser.getEmail());
        verify(passwordEncoder, times(1)).encode(registerUserDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAuthenticate() {
        // Mock behavior
        when(authenticationManager.authenticate(any())).thenReturn(null); // Simulate successful authentication
        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(java.util.Optional.of(user));

        // Test authenticate functionality
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        assertNotNull(authenticatedUser);
        assertEquals("testuser@example.com", authenticatedUser.getEmail());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(loginUserDto.getEmail());
    }

    @Test
    void testAuthenticateThrowsExceptionWhenUserNotFound() {
        // Mock behavior
        when(authenticationManager.authenticate(any())).thenReturn(null); // Simulate successful authentication
        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(java.util.Optional.empty());

        // Test that exception is thrown when user is not found
        assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(loginUserDto);
        });

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(loginUserDto.getEmail());
    }

    @Test
    void testAllUsers() {
        // Mock behavior
        when(userRepository.findAll()).thenReturn(java.util.List.of(user));

        // Test fetching all users
        var users = authenticationService.allUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getFullName());
        verify(userRepository, times(1)).findAll();
    }
}