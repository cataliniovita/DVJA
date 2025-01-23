package com.budgetplaner.controllers;

import com.budgetplaner.entities.Budget;
import com.budgetplaner.entities.Spending;
import com.budgetplaner.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public Budget createBudget(@RequestBody Budget budget) {
        double totalCategoryBudget = budget.getFoodBudget() + budget.getTransportBudget() +
                budget.getEntertainmentBudget() + budget.getUtilitiesBudget() +
                budget.getOtherBudget();

        if (totalCategoryBudget > budget.getTotalBudget()) {
            throw new IllegalArgumentException("Category budgets exceeds total monthly budget");
        }

        return budgetService.saveBudget(budget);
    }

    @PutMapping("/{budgetId}")
    public Budget updateBudget(@PathVariable int budgetId, @RequestBody Budget budget) {
        return budgetService.updateBudget(budgetId, budget);
    }

    @GetMapping("/user/{userId}")
    public List<Budget> getBudgetsByUser(@PathVariable int userId) {
        return budgetService.findBudgetsByUser(userId);
    }

}