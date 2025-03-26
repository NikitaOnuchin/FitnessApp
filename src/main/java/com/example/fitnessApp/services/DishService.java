package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotCreateException;
import com.example.fitnessApp.models.Dish;
import com.example.fitnessApp.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class DishService {
    private final DishRepository dishRepository;
    private final ValidationService validationService;

    @Autowired
    public DishService(DishRepository dishRepository, ValidationService validationService) {
        this.dishRepository = dishRepository;
        this.validationService = validationService;
    }

    @Transactional
    public Dish save(Dish dish) {
        if (validationService.existsByName(dish.getName())) {
            throw new NotCreateException("Dish name already exists");
        }
        return dishRepository.save(dish);
    }
}
