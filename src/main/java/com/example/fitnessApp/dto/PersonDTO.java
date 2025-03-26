package com.example.fitnessApp.dto;

import jakarta.validation.constraints.*;

public class PersonDTO {

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 character")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

    @NotNull(message = "Age cannot be null")
    @Min(0)
    @Max(120)
    private Integer age;

    @NotNull(message = "Weight cannot be null")
    @Min(0)
    @Max(400)
    private Integer weight;

    @NotNull(message = "Height cannot be null")
    @Min(0)
    @Max(300)
    private Integer height;

    @NotNull(message = "Goal cannot be null")
    @Pattern(regexp = "WEIGHT_LOSS|MAINTENANCE|WEIGHT_GAIN",
            message = "Invalid goal. Use only - WEIGHT_LOSS, MAINTENANCE, WEIGHT_GAIN")
    private String goal;

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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
