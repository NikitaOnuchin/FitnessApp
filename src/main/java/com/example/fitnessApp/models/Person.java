package com.example.fitnessApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 character")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

    @NotNull(message = "Age cannot be null")
    @Min(0)
    @Max(120)
    private int age;

    @NotNull(message = "Weight cannot be null")
    @Min(0)
    @Max(400)
    private int weight;

    @NotNull(message = "Height cannot be null")
    @Min(0)
    @Max(300)
    private int height;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name = "calorie_norm")
    private int calorieNorm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public int getCalorieNorm() {
        return calorieNorm;
    }

    public void setCalorieNorm(int calorieNorm) {
        this.calorieNorm = calorieNorm;
    }
}
