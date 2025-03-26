package com.example.fitnessApp.repositories;

import com.example.fitnessApp.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByName(String name);
}
