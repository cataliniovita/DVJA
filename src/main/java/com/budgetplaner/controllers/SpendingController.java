package com.budgetplaner.controllers;


import com.budgetplaner.entities.Spending;
import com.budgetplaner.services.SpendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spendings")
public class SpendingController {

    private SpendingService spendingService;

    public SpendingController(SpendingService spendingService) {
        this.spendingService = spendingService;
    }

    @PostMapping
    public Spending createSpending(@RequestBody Spending spending) {
        if (spending.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (spending.getCategory() == null || spending.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Category must be selected");
        }

        return spendingService.saveSpending(spending);
    }

    @GetMapping("/user/{userId}")
    public List<Spending> getSpendingsByUser(@PathVariable int userId) {
        return spendingService.findSpendingsByUser(userId);
    }

    @PutMapping("/{spendingId}")
    public Spending updateSpending(@PathVariable int spendingId, @RequestBody Spending spending) {
        return spendingService.updateSpending(spendingId, spending);
    }

    @DeleteMapping("/{spendingId}")
    public void deleteSpending(@PathVariable int spendingId) {
        spendingService.deleteSpending(spendingId);
    }


}

