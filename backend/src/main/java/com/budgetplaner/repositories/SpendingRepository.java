package com.budgetplaner.repositories;

import com.budgetplaner.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpendingRepository extends JpaRepository<Spending, Integer> {
    List<Spending> findByUser(User user);
}