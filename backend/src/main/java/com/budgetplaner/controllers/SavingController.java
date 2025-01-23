package com.budgetplaner.controllers;

import com.budgetplaner.entities.Savings;
import com.budgetplaner.services.SavingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/savings")
public class SavingController {

    private SavingService savingService;

    public SavingController(SavingService savingService) {
        this.savingService = savingService;
    }

    @PostMapping
    public Savings createSaving(@RequestBody Savings saving) {
        return savingService.saveSaving(saving);
    }

    @GetMapping("/user/{userId}")
    public List<Savings> getSavingsByUser(@PathVariable int userId) {
        return savingService.findSavingsByUser(userId);
    }

    @PostMapping("/contribute/{savingsId}")
    public Savings contributeToSavings(@PathVariable int savingsId, @RequestBody Map<String, Object> requestBody) {
        Double contribution = ((Number) requestBody.get("contribution")).doubleValue();
        return savingService.addContribution(savingsId, contribution);
    }

}
