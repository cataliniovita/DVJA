package com.budgetplaner.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "budgets")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "total_monthly_budget")
    private Double totalBudget;
    @Column(name = "food_budget")
    private Double foodBudget;
    @Column(name = "transportation_budget")
    private Double transportBudget;
    @Column(name = "entertainment_budget")
    private Double entertainmentBudget;
    @Column(name = "utilities_budget")
    private Double utilitiesBudget;
    @Column(name = "other_budget")
    private Double otherBudget;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Double getFoodBudget() {
        return foodBudget;
    }

    public void setFoodBudget(Double foodBudget) {
        this.foodBudget = foodBudget;
    }

    public Double getTransportBudget() {
        return transportBudget;
    }

    public void setTransportBudget(Double transportBudget) {
        this.transportBudget = transportBudget;
    }

    public Double getEntertainmentBudget() {
        return entertainmentBudget;
    }

    public void setEntertainmentBudget(Double entertainmentBudget) {
        this.entertainmentBudget = entertainmentBudget;
    }

    public Double getUtilitiesBudget() {
        return utilitiesBudget;
    }

    public void setUtilitiesBudget(Double utilitiesBudget) {
        this.utilitiesBudget = utilitiesBudget;
    }

    public Double getOtherBudget() {
        return otherBudget;
    }

    public void setOtherBudget(Double otherBudget) {
        this.otherBudget = otherBudget;
    }
}