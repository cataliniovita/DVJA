package com.budgetplaner.repositories;

import com.budgetplaner.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingRepository extends JpaRepository<Savings, Integer> {
    List<Savings> findByUser(User user);
}