package com.budgetplaner.services;

import com.budgetplaner.entities.Savings;
import com.budgetplaner.entities.User;
import com.budgetplaner.repositories.SavingRepository;
import com.budgetplaner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class SavingService {

    private SavingRepository savingRepository;

    private UserRepository userRepository;

    public SavingService(SavingRepository savingRepository, UserRepository userRepository) {
        this.savingRepository = savingRepository;
        this.userRepository = userRepository;
    }

    public Savings saveSaving(Savings saving) {
        return savingRepository.save(saving);
    }

    public List<Savings> findSavingsByUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savingRepository.findByUser(user);
    }

    public Savings addContribution(int savingsId, Double contribution) {
        Savings savings = savingRepository.findById(savingsId)
                .orElseThrow(() -> new RuntimeException("Savings goal not found"));

        savings.setSavedAmount(savings.getSavedAmount() + contribution);

        if (savings.getSavedAmount() > savings.getTargetAmount()) {
            throw new RuntimeException("Contribution exceeds target amount");
        }

        return savingRepository.save(savings);
    }

}