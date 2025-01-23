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
@Table(name = "savings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Savings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "goal_name")
    private String goalName;
    @Column(name = "target_amount")
    private Double targetAmount;
    @Column(name = "amount_saved")
    private Double savedAmount;

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

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Double getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(Double savedAmount) {
        this.savedAmount = savedAmount;
    }

    @Override
    public String toString() {
        return "Savings{" +
                "id=" + id +
                ", user=" + user +
                ", goalName='" + goalName + '\'' +
                ", targetAmount=" + targetAmount +
                ", savedAmount=" + savedAmount +
                '}';
    }
}