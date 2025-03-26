package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotFoundException;
import com.example.fitnessApp.models.*;
import com.example.fitnessApp.repositories.DishRepository;
import com.example.fitnessApp.repositories.MealRepository;
import com.example.fitnessApp.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MealService {

    private final MealRepository mealRepository;
    private final DishRepository dishRepository;
    private final PersonService personService;
    private final PersonRepository personRepository;

    @Autowired
    public MealService(MealRepository mealRepository, DishRepository dishRepository,
                       PersonService personService, PersonRepository personRepository) {
        this.mealRepository = mealRepository;
        this.dishRepository = dishRepository;
        this.personService = personService;
        this.personRepository = personRepository;
    }

    public Meal saveMeal(Meal meal, List<Long> dishesIds) throws NotFoundException {
        Meal mealSaved = saveOrGetIfExist(meal);

        List<Dish> dishes = getDishesById(dishesIds);
        mealSaved.setMealDishes(addDishesToMeal(mealSaved, dishes));

        return mealSaved;
    }

    @Transactional(readOnly = true)
    public List<Meal> findMealsByPersonAndCreateDate(Long personId, LocalDate date) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty()) {
            throw new NotFoundException("Person not found");
        }

        List<Meal> meals = mealRepository.findMealByPersonAndCreateDate(person.get(), date);
        if (meals.isEmpty()) {
            return new ArrayList<>();
        } else {
            return meals;
        }
    }

    protected Meal saveOrGetIfExist(Meal meal) {
        Meal mealSaved = mealRepository
                .findMealByPersonAndMealType(meal.getPerson(), meal.getMealType());

        if (mealSaved == null) {
            Person person = personService.findById(meal.getPerson().getId());
            meal.setPerson(person);
            enrichMeal(meal);
            mealSaved = mealRepository.save(meal);
        }

        return mealSaved;
    }

    private void enrichMeal(Meal meal) {
        meal.setCreateDate(LocalDate.now());
    }

    @Transactional(readOnly = true)
    protected List<Dish> getDishesById(List<Long> dishesIds) throws NotFoundException {
        List<Dish> uniqueDishes = dishRepository.findAllById(new HashSet<>(dishesIds));

        if (uniqueDishes.size() != new HashSet<>(dishesIds).size()) {
            throw new NotFoundException("One or more dishes not found");
        }

        Map<Long, Dish> dishMap = uniqueDishes.stream()
                .collect(Collectors.toMap(Dish::getId, dish -> dish));

        return dishesIds.stream()
                .map(dishMap::get)
                .collect(Collectors.toList());
    }

    protected Set<MealDish> addDishesToMeal(Meal meal, List<Dish> dishesToAdd) {
        Set<MealDish> mealDishes = meal.getMealDishes();

        for (Dish dish : dishesToAdd) {
            Optional<MealDish> existingMealDish = mealDishes.stream()
                    .filter(md -> md.getDish().getId().equals(dish.getId()))
                    .findFirst();

            if (existingMealDish.isPresent()) {
                MealDish mealDish = existingMealDish.get();
                mealDish.setCount(mealDish.getCount() + 1);
            } else {
                MealDish newMealDish = new MealDish(dish, meal, 1);
                mealDishes.add(newMealDish);
            }
        }

        meal.setMealDishes(mealDishes);
        mealRepository.save(meal);

        return mealDishes;
    }
}
