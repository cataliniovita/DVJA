package com.budgetplaner.services;

import com.budgetplaner.entities.Spending;
import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.SpendingRepository;
import com.budgetplaner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpendingServiceTest {
    @Mock
    private SpendingRepository spendingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SpendingService spendingService;

    private Spending spending;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a mock User object
        user = new User();
        user.setId(1);
        user.setFullName("John Doe");

        // Set up a mock Spending object
        spending = new Spending();
        spending.setId(1);
        spending.setUser(user);
        spending.setCategory("Food");
        spending.setAmount(50.0);
        spending.setDate("2025-01-17");
    }

    @Test
    public void testSaveSpending() {
        // Arrange
        when(spendingRepository.save(any())).thenReturn(spending);

        // Act
        Spending savedSpending = spendingService.saveSpending(spending);

        // Assert
        assertNotNull(savedSpending);
        assertEquals("Food", savedSpending.getCategory());
        verify(spendingRepository, times(1)).save(spending);
    }

    @Test
    public void testFindSpendingsByUser_Success() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(spendingRepository.findByUser(user)).thenReturn(new ArrayList<>());

        // Act
        List<Spending> spendings = spendingService.findSpendingsByUser(1);

        // Assert
        assertNotNull(spendings);
        verify(spendingRepository, times(1)).findByUser(user);
    }

    @Test
    public void testFindSpendingsByUser_UserNotFound() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spendingService.findSpendingsByUser(1);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testUpdateSpending_Success() {
        // Arrange
        Spending updatedSpending = new Spending();
        updatedSpending.setCategory("Entertainment");
        updatedSpending.setAmount(100.0);
        updatedSpending.setDate("2025-01-18");

        when(spendingRepository.findById(1)).thenReturn(Optional.of(spending));
        when(spendingRepository.save(any())).thenReturn(updatedSpending);

        // Act
        Spending updated = spendingService.updateSpending(1, updatedSpending);

        // Assert
        assertNotNull(updated);
        assertEquals("Entertainment", updated.getCategory());
        assertEquals(100.0, updated.getAmount());
    }

    @Test
    public void testUpdateSpending_SpendingNotFound() {
        // Arrange
        Spending updatedSpending = new Spending();
        updatedSpending.setCategory("Entertainment");

        when(spendingRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spendingService.updateSpending(1, updatedSpending);
        });

        assertEquals("Spending not found", exception.getMessage());
    }

    @Test
    public void testDeleteSpending_Success() {
        // Arrange
        when(spendingRepository.findById(1)).thenReturn(Optional.of(spending));

        // Act
        spendingService.deleteSpending(1);

        // Assert
        verify(spendingRepository, times(1)).delete(spending);
    }

    @Test
    public void testDeleteSpending_SpendingNotFound() {
        // Arrange
        when(spendingRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spendingService.deleteSpending(1);
        });

        assertEquals("Spending not found", exception.getMessage());
    }
}