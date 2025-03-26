package com.example.fitnessApp.repositories;

import com.example.fitnessApp.models.Meal;
import com.example.fitnessApp.models.MealType;
import com.example.fitnessApp.models.Person;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    Meal findMealByPersonAndMealType(Person person, MealType mealType);
    List<Meal> findMealByPersonAndCreateDate(Person person, LocalDate createDate);
}
