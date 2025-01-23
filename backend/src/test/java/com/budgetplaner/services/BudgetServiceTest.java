package com.budgetplaner.services;

import com.budgetplaner.entities.Budget;
import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.BudgetRepository;
import com.budgetplaner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceTest {
    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    private User user;
    private Budget budget;

    @BeforeEach
    void setUp() {
        // Initialize mocks and setup test data
        MockitoAnnotations.openMocks(this);

        // Creating a mock User object using setters
        user = new User();
        user.setId(1);
        user.setFullName("Test User");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setCreatedAt(null);
        user.setUpdatedAt(null);

        // Creating a mock Budget object using setters
        budget = new Budget();
        budget.setId(1);
        budget.setUser(user);
        budget.setTotalBudget(1000.0);
        budget.setFoodBudget(200.0);
        budget.setTransportBudget(150.0);
        budget.setEntertainmentBudget(100.0);
        budget.setUtilitiesBudget(300.0);
        budget.setOtherBudget(250.0);
    }

    @Test
    void saveBudgetTest() {
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        // Test saving a budget
        Budget savedBudget = budgetService.saveBudget(budget);

        assertNotNull(savedBudget);
        assertEquals(1000.0, savedBudget.getTotalBudget());
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void findBudgetsByUserTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(budgetRepository.findByUser(user)).thenReturn(List.of(budget));

        // Test finding budgets by user
        List<Budget> budgets = budgetService.findBudgetsByUser(1);

        assertNotNull(budgets);
        assertEquals(1, budgets.size());
        assertEquals(1000.0, budgets.get(0).getTotalBudget());
        verify(userRepository, times(1)).findById(1);
        verify(budgetRepository, times(1)).findByUser(user);
    }

    @Test
    void findBudgetsByUserThrowsExceptionTest() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Test that an exception is thrown when user is not found
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            budgetService.findBudgetsByUser(1);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(budgetRepository, times(0)).findByUser(user);
    }

    @Test
    void updateBudgetTest() {
        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget updatedBudget = new Budget();
        updatedBudget.setId(1);
        updatedBudget.setUser(user);
        updatedBudget.setTotalBudget(1200.0);
        updatedBudget.setFoodBudget(250.0);
        updatedBudget.setTransportBudget(180.0);
        updatedBudget.setEntertainmentBudget(110.0);
        updatedBudget.setUtilitiesBudget(350.0);
        updatedBudget.setOtherBudget(310.0);

        // Test updating a budget
        Budget result = budgetService.updateBudget(1, updatedBudget);

        assertNotNull(result);
        assertEquals(1200.0, result.getTotalBudget());
        assertEquals(250.0, result.getFoodBudget());
    }

    @Test
    void updateBudgetThrowsExceptionTest() {
        when(budgetRepository.findById(1)).thenReturn(Optional.empty());

        Budget updatedBudget = new Budget();
        updatedBudget.setId(1);
        updatedBudget.setUser(user);
        updatedBudget.setTotalBudget(1200.0);
        updatedBudget.setFoodBudget(250.0);
        updatedBudget.setTransportBudget(180.0);
        updatedBudget.setEntertainmentBudget(110.0);
        updatedBudget.setUtilitiesBudget(350.0);
        updatedBudget.setOtherBudget(310.0);

        // Test that an exception is thrown when budget is not found
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            budgetService.updateBudget(1, updatedBudget);
        });

        assertEquals("budget not found", exception.getMessage());
        verify(budgetRepository, times(1)).findById(1);
        verify(budgetRepository, times(0)).save(updatedBudget);
    }
}