package com.example.fitnessApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;


public class MealDTO {

    @NotNull(message = "PersonId cannot be null")
    private Long personId;

    @NotNull(message = "DishIds cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> dishIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<MealDishResponse> mealDishResponses;

    @NotNull(message = "MealType cannot be null")
    @Pattern(regexp = "BREAKFAST|LUNCH|DINNER",
            message = "Invalid mealType. Use only - BREAKFAST, LUNCH, DINNER")
    private String mealType;

    public List<Long> getDishIds() {
        return dishIds;
    }

    public void setDishIds(List<Long> dishIds) {
        this.dishIds = dishIds;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Set<MealDishResponse> getMealDishResponses() {
        return mealDishResponses;
    }

    public void setMealDishResponses(Set<MealDishResponse> mealDishResponses) {
        this.mealDishResponses = mealDishResponses;
    }

    public static class MealDishResponse {
        private Long dishId;
        private int count;

        public MealDishResponse(Long dishId, int count) {
            this.dishId = dishId;
            this.count = count;
        }

        public Long getDishId() {
            return dishId;
        }

        public void setDishId(Long dishId) {
            this.dishId = dishId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
