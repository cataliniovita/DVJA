package com.budgetplaner.repositories;

import com.budgetplaner.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer>{
    List<Budget> findByUser(User user);
}