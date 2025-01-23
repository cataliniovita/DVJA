package com.budgetplaner.services;

import com.budgetplaner.entities.Savings;
import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.SavingRepository;
import com.budgetplaner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SavingServiceTest {
    @Mock
    private SavingRepository savingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SavingService savingService;

    private Savings savings;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a mock User object
        user = new User();
        user.setId(1);
        user.setFullName("John Doe");

        // Set up a mock Savings object
        savings = new Savings();
        savings.setId(1);
        savings.setUser(user);
        savings.setTargetAmount(500.0);
        savings.setSavedAmount(200.0);
    }

    @Test
    public void testSaveSaving() {
        // Arrange
        when(savingRepository.save(any())).thenReturn(savings);

        // Act
        Savings savedSavings = savingService.saveSaving(savings);

        // Assert
        assertNotNull(savedSavings);
        assertEquals(200.0, savedSavings.getSavedAmount());
        assertEquals(500.0, savedSavings.getTargetAmount());
        verify(savingRepository, times(1)).save(savings);
    }

    @Test
    public void testFindSavingsByUser_Success() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(savingRepository.findByUser(user)).thenReturn(List.of(savings));

        // Act
        List<Savings> savingsList = savingService.findSavingsByUser(1);

        // Assert
        assertNotNull(savingsList);
        assertFalse(savingsList.isEmpty());
        assertEquals(1, savingsList.size());
        verify(savingRepository, times(1)).findByUser(user);
    }

    @Test
    public void testFindSavingsByUser_UserNotFound() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            savingService.findSavingsByUser(1);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testAddContribution_Success() {
        // Arrange
        double contribution = 100.0;
        savings.setSavedAmount(200.0);
        when(savingRepository.findById(1)).thenReturn(Optional.of(savings));
        when(savingRepository.save(any(Savings.class))).thenReturn(savings);

        // Act
        Savings updatedSavings = savingService.addContribution(1, contribution);

        // Assert
        assertNotNull(updatedSavings);
        assertEquals(300.0, updatedSavings.getSavedAmount());
        verify(savingRepository, times(1)).findById(1);
        verify(savingRepository, times(1)).save(savings);
    }

    @Test
    public void testAddContribution_ExceedsTarget() {
        // Arrange
        double contribution = 350.0; // This will exceed the target
        savings.setSavedAmount(200.0);
        when(savingRepository.findById(1)).thenReturn(Optional.of(savings));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            savingService.addContribution(1, contribution);
        });

        assertEquals("Contribution exceeds target amount", exception.getMessage());
    }

    @Test
    public void testAddContribution_SavingsNotFound() {
        // Arrange
        double contribution = 100.0;
        when(savingRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            savingService.addContribution(1, contribution);
        });

        assertEquals("Savings goal not found", exception.getMessage());
    }
}