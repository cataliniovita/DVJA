package com.budgetplaner.services;

import com.budgetplaner.entities.Budget;
import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.BudgetRepository;
import com.budgetplaner.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {
    private BudgetRepository budgetRepository;

    private UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> findBudgetsByUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return budgetRepository.findByUser(user);
    }

    public Budget updateBudget(int budgetId, Budget updatedBudget) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("budget not found"));

        budget.setTotalBudget(updatedBudget.getTotalBudget());
        budget.setFoodBudget(updatedBudget.getFoodBudget());
        budget.setTransportBudget(updatedBudget.getTransportBudget());
        budget.setEntertainmentBudget(updatedBudget.getEntertainmentBudget());
        budget.setUtilitiesBudget(updatedBudget.getUtilitiesBudget());
        budget.setOtherBudget(updatedBudget.getOtherBudget());

        return budgetRepository.save(budget);
    }
    
}