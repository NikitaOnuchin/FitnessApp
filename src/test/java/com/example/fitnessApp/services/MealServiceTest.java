package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotFoundException;
import com.example.fitnessApp.models.*;
import com.example.fitnessApp.repositories.DishRepository;
import com.example.fitnessApp.repositories.MealRepository;
import com.example.fitnessApp.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private MealService mealService;

    private Person testPerson;
    private Dish testDish;
    private Meal testMeal;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setId(1L);
        testPerson.setName("John");

        testDish = new Dish();
        testDish.setId(10L);
        testDish.setName("Salad");

        testMeal = new Meal();
        testMeal.setPerson(testPerson);
        testMeal.setMealType(MealType.BREAKFAST);
        testMeal.setCreateDate(LocalDate.now());
        testMeal.setMealDishes(new HashSet<>());
    }

    @Test
    void testSaveMeal_NewMeal_Success() {
        List<Long> dishIds = Collections.singletonList(testDish.getId());

        when(mealRepository.findMealByPersonAndMealType(testPerson, MealType.BREAKFAST))
                .thenReturn(null);
        doReturn(testPerson).when(personService).findById(testPerson.getId());
        doReturn(Collections.singletonList(testDish)).when(dishRepository).findAllById(new HashSet<>(dishIds));
        doReturn(testMeal).when(mealRepository).save(any(Meal.class));

        Meal savedMeal = mealService.saveMeal(testMeal, dishIds);

        assertNotNull(savedMeal);
        assertEquals(testPerson, savedMeal.getPerson());
        verify(mealRepository, times(2)).save(any(Meal.class));
    }

    @Test
    void testSaveMeal_ExistingMeal_Success() {
        List<Long> dishIds = Collections.singletonList(testDish.getId());

        doReturn(testMeal).when(mealRepository).findMealByPersonAndMealType(testPerson, MealType.BREAKFAST);
        doReturn(Collections.singletonList(testDish)).when(dishRepository).findAllById(new HashSet<>(dishIds));

        Meal savedMeal = mealService.saveMeal(testMeal, dishIds);

        assertNotNull(savedMeal);
        assertEquals(testMeal, savedMeal);
        verify(mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void testFindMealsByPersonAndCreateDate_Success() {
        LocalDate date = LocalDate.now();
        List<Meal> meals = Collections.singletonList(testMeal);

        when(personRepository.findById(testPerson.getId()))
                .thenReturn(Optional.of(testPerson));
        when(mealRepository.findMealByPersonAndCreateDate(testPerson, date))
                .thenReturn(meals);

        List<Meal> foundMeals = mealService.findMealsByPersonAndCreateDate(testPerson.getId(), date);

        assertEquals(1, foundMeals.size());
        assertEquals(testMeal, foundMeals.get(0));
    }

    @Test
    void testFindMealsByPersonAndCreateDate_PersonNotFound() {
        LocalDate date = LocalDate.now();

        when(personRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                mealService.findMealsByPersonAndCreateDate(99L, date));
    }

    @Test
    void testGetDishesById_DishNotFound() {
        List<Long> dishIds = Arrays.asList(10L, 20L);

        when(dishRepository.findAllById(new HashSet<>(dishIds)))
                .thenReturn(Collections.singletonList(testDish));

        assertThrows(NotFoundException.class, () ->
                mealService.getDishesById(dishIds));
    }

    @Test
    void testAddDishesToMeal_NewDishAdded() {
        List<Dish> dishes = Collections.singletonList(testDish);

        Set<MealDish> mealDishes = mealService.addDishesToMeal(testMeal, dishes);

        assertEquals(1, mealDishes.size());
        assertEquals(1, mealDishes.iterator().next().getCount());
        verify(mealRepository, times(1)).save(testMeal);
    }

    @Test
    void testAddDishesToMeal_ExistingDishCountIncremented() {
        MealDish existingMealDish = new MealDish(testDish, testMeal, 1);
        testMeal.getMealDishes().add(existingMealDish);

        List<Dish> dishes = Collections.singletonList(testDish);

        Set<MealDish> mealDishes = mealService.addDishesToMeal(testMeal, dishes);

        assertEquals(1, mealDishes.size());
        assertEquals(2, mealDishes.iterator().next().getCount());
        verify(mealRepository, times(1)).save(testMeal);
    }
}
