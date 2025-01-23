package com.budgetplaner.services;

import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allUsers_shouldReturnListOfUsers() {
        // Arrange
        User user1 = new User().setId(1).setFullName("John Doe").setEmail("john@example.com");
        User user2 = new User().setId(2).setFullName("Jane Smith").setEmail("jane@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> result = userService.allUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("Jane Smith", result.get(1).getFullName());
    }

    @Test
    void allUsers_shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<User> result = userService.allUsers();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnUserWhenExists() {
        User user = new User().setId(1).setFullName("John Doe").setEmail("john@example.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }
}