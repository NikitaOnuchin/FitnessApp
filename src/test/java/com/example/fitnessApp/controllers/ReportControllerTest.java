package com.example.fitnessApp.controllers;

import com.example.fitnessApp.models.Dish;
import com.example.fitnessApp.models.Meal;
import com.example.fitnessApp.models.MealDish;
import com.example.fitnessApp.models.Person;
import com.example.fitnessApp.services.MealService;
import com.example.fitnessApp.services.PersonService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private MealService mealService;

    @Mock
    private PersonService personService;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetReportSumForDay_Success() throws Exception {
        LocalDate localDate = LocalDate.of(2024, 3, 25);
        Meal meal = new Meal();
        Dish dish = new Dish();
        dish.setCalories(200);
        meal.setMealDishes(Set.of(new MealDish(dish, null, 1)));

        when(mealService.findMealsByPersonAndCreateDate(1L, localDate)).thenReturn(List.of(meal));

        mockMvc.perform(get("/report/sumForDay")
                        .param("personId", "1")
                        .param("date", "25-03-2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.day").value("2024-03-25"))
                .andExpect(jsonPath("$.sumCalories").value(200))
                .andExpect(jsonPath("$.countMeals").value(1));

        verify(mealService, times(1)).findMealsByPersonAndCreateDate(1L, localDate);
    }

    @Test
    void testIsWithinCalorieNorm_Success() throws Exception {

        LocalDate localDate = LocalDate.of(2024, 3, 25);
        Meal meal = new Meal();
        Dish dish = new Dish();
        dish.setCalories(200);
        meal.setMealDishes(Set.of(new MealDish(dish, null, 1)));

        Person person = new Person();
        person.setCalorieNorm(2000);

        when(mealService.findMealsByPersonAndCreateDate(1L, localDate)).thenReturn(List.of(meal));
        when(personService.findById(1L)).thenReturn(person);

        mockMvc.perform(get("/report/isWithinCalorieNorm")
                        .param("personId", "1")
                        .param("date", "25-03-2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.withinCalorieNorm").value(true));

        verify(mealService, times(1)).findMealsByPersonAndCreateDate(1L, localDate);
        verify(personService, times(1)).findById(1L);
    }

    @Test
    void testGetReportSumForDays_Success() throws Exception {
        LocalDate date1 = LocalDate.of(2024, 3, 23);
        LocalDate date2 = LocalDate.of(2024, 3, 24);
        LocalDate date3 = LocalDate.of(2024, 3, 25);

        Meal meal = new Meal();
        Dish dish = new Dish();
        dish.setCalories(300);
        meal.setMealDishes(Set.of(new MealDish(dish, null, 1)));

        when(mealService.findMealsByPersonAndCreateDate(1L, date1)).thenReturn(List.of(meal));
        when(mealService.findMealsByPersonAndCreateDate(1L, date2)).thenReturn(List.of(meal));
        when(mealService.findMealsByPersonAndCreateDate(1L, date3)).thenReturn(List.of(meal));

        mockMvc.perform(get("/report/sumForDays")
                        .param("personId", "1")
                        .param("dateFrom", "23-03-2024")
                        .param("dateTo", "25-03-2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daysResponse").isArray())
                .andExpect(jsonPath("$.daysResponse[0].day").value("2024-03-23"))
                .andExpect(jsonPath("$.daysResponse[0].sumCalories").value(300))
                .andExpect(jsonPath("$.daysResponse[0].countMeals").value(1))
                .andExpect(jsonPath("$.daysResponse[1].day").value("2024-03-24"))
                .andExpect(jsonPath("$.daysResponse[1].sumCalories").value(300))
                .andExpect(jsonPath("$.daysResponse[1].countMeals").value(1))
                .andExpect(jsonPath("$.daysResponse[2].day").value("2024-03-25"))
                .andExpect(jsonPath("$.daysResponse[2].sumCalories").value(300))
                .andExpect(jsonPath("$.daysResponse[2].countMeals").value(1));

        verify(mealService, times(1)).findMealsByPersonAndCreateDate(1L, date1);
        verify(mealService, times(1)).findMealsByPersonAndCreateDate(1L, date2);
        verify(mealService, times(1)).findMealsByPersonAndCreateDate(1L, date3);
    }
}
