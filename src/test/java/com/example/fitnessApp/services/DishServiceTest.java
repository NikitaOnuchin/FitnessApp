package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotCreateException;
import com.example.fitnessApp.models.Dish;
import com.example.fitnessApp.repositories.DishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private DishService dishService;

    @Test
    void testSaveDishSuccessfully() {
        // Создаем тестовые данные
        Dish dish = new Dish();
        dish.setName("Pasta");
        dish.setCalories(350);
        dish.setProteins(10.0);
        dish.setFats(5.7);
        dish.setCarbohydrates(6.5);
        when(validationService.existsByName(dish.getName())).thenReturn(false);
        when(dishRepository.save(dish)).thenReturn(dish);

        Dish savedDish = dishService.save(dish);

        assertNotNull(savedDish);
        assertEquals("Pasta", savedDish.getName());
        assertEquals(350, savedDish.getCalories());

        verify(validationService, times(1)).existsByName(dish.getName());
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void testSaveDishThrowsNotCreateExceptionIfDishExists() {
        Dish dish = new Dish();
        dish.setName("Apple");
        dish.setCalories(200);
        dish.setProteins(1.0);
        dish.setFats(15.7);
        dish.setCarbohydrates(16.5);
        when(validationService.existsByName(dish.getName())).thenReturn(true);

        NotCreateException exception = assertThrows(NotCreateException.class, () -> dishService.save(dish));

        assertEquals("Dish name already exists", exception.getMessage());
        verify(dishRepository, times(0)).save(dish);
    }
}
