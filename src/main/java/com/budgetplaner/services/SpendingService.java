package com.budgetplaner.services;


import com.budgetplaner.entities.Spending;
import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.SpendingRepository;
import com.budgetplaner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SpendingService {

    private SpendingRepository spendingRepository;

    private UserRepository userRepository;

    public SpendingService(SpendingRepository spendingRepository, UserRepository userRepository) {
        this.spendingRepository = spendingRepository;
        this.userRepository = userRepository;
    }

    public Spending saveSpending(Spending spending) {
        return spendingRepository.save(spending);
    }

    public List<Spending> findSpendingsByUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return spendingRepository.findByUser(user);
    }

    public Spending updateSpending(int spendingId, Spending updatedSpending) {
        Spending spending = spendingRepository.findById(spendingId)
                .orElseThrow(() -> new RuntimeException("Spending not found"));

        spending.setCategory(updatedSpending.getCategory());
        spending.setAmount(updatedSpending.getAmount());
        spending.setDate(updatedSpending.getDate());

        return spendingRepository.save(spending);
    }

    public void deleteSpending(int spendingId) {
        Spending spending = spendingRepository.findById(spendingId)
                .orElseThrow(() -> new RuntimeException("Spending not found"));

        spendingRepository.delete(spending);
    }



}


