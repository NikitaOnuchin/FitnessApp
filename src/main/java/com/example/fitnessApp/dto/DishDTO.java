package com.example.fitnessApp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DishDTO {

    @NotNull
    private String name;

    @NotNull
    @Min(0)
    private Integer calories;

    @NotNull
    @DecimalMin("0.0")
    private Double proteins;

    @NotNull
    @DecimalMin("0.0")
    private Double fats;

    @NotNull
    @DecimalMin("0.0")
    private Double carbohydrates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}
