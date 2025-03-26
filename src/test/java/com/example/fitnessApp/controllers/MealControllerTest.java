package com.example.fitnessApp.controllers;

import com.example.fitnessApp.dto.MealDTO;
import com.example.fitnessApp.exception.NotFoundException;
import com.example.fitnessApp.models.*;
import com.example.fitnessApp.services.MealService;
import com.example.fitnessApp.services.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MealControllerTest {

    @Mock
    private MealService mealService;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private MealController mealController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mealController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSaveMeal_Success() throws Exception {
        // Подготовка данных
        MealDTO mealDTORequest = new MealDTO();
        mealDTORequest.setPersonId(1L);
        mealDTORequest.setMealType(MealType.BREAKFAST.toString());
        mealDTORequest.setDishIds(Collections.singletonList(1L));

        Meal meal = new Meal();
        Person person = new Person();
        person.setId(1L);
        meal.setPerson(person);
        meal.setMealType(MealType.BREAKFAST);

        MealDish mealDish = new MealDish();
        Dish dish = new Dish();
        dish.setId(1L);
        mealDish.setDish(dish);
        mealDish.setCount(1);

        Set<MealDish> mealDishes = new HashSet<>();
        mealDishes.add(mealDish);
        meal.setMealDishes(mealDishes);

        when(mealService.saveMeal(any(Meal.class), any())).thenReturn(meal);

        mockMvc.perform(post("/meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDTORequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personId").value(1))
                .andExpect(jsonPath("$.mealType").value("BREAKFAST"))
                .andExpect(jsonPath("$.mealDishResponses[0].dishId").value(1))
                .andExpect(jsonPath("$.mealDishResponses[0].count").value(1));

        verify(mealService, times(1)).saveMeal(any(Meal.class), any());
        verify(validationService, times(1)).validate(any());
    }
}
