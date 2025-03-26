package com.example.fitnessApp.controllers;

import com.example.fitnessApp.dto.MealDTO;
import com.example.fitnessApp.exception.NotFoundException;
import com.example.fitnessApp.models.Meal;
import com.example.fitnessApp.models.MealDish;
import com.example.fitnessApp.models.MealType;
import com.example.fitnessApp.models.Person;
import com.example.fitnessApp.services.MealService;
import com.example.fitnessApp.services.ValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/meal")
public class MealController {

    private final MealService mealService;
    private final ValidationService validationService;


    @Autowired
    public MealController(MealService mealService, ValidationService validationService) {
        this.mealService = mealService;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<MealDTO> saveMeal(@RequestBody @Valid MealDTO mealDTO,
                                            BindingResult bindingResult) throws NotFoundException {
        validationService.validate(bindingResult);
        Meal meal = convertMealDTOToMeal(mealDTO);
        Meal mealSaved = mealService.saveMeal(meal, mealDTO.getDishIds());

        MealDTO mealDTOResponse = convertMealToMealDTO(mealSaved);

        return new ResponseEntity<>(mealDTOResponse, HttpStatus.CREATED);
    }

    private Meal convertMealDTOToMeal(MealDTO mealDTO) {
        Meal meal = new Meal();
        Person person = new Person();
        person.setId(mealDTO.getPersonId());
        meal.setPerson(person);
        meal.setMealType(MealType.valueOf(mealDTO.getMealType()));
        return meal;
    }

    private MealDTO convertMealToMealDTO(Meal meal) {
        MealDTO mealDTO = new MealDTO();
        mealDTO.setPersonId(meal.getPerson().getId());
        mealDTO.setMealType(meal.getMealType().toString());
        Set<MealDTO.MealDishResponse> mealDishResponses = new HashSet<>();
        for (MealDish mealDish : meal.getMealDishes()) {
            mealDishResponses.add(new MealDTO.MealDishResponse(mealDish.getDish().getId(), mealDish.getCount()));
        }
        mealDTO.setMealDishResponses(mealDishResponses);
        return mealDTO;
    }
}
