package com.example.fitnessApp.controllers;

import com.example.fitnessApp.dto.DishDTO;
import com.example.fitnessApp.models.Dish;
import com.example.fitnessApp.services.DishService;
import com.example.fitnessApp.services.ValidationService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
public class DishController {

    private final DishService dishService;
    private final ModelMapper modelMapper;
    private final ValidationService validationService;

    @Autowired
    public DishController(DishService dishService, ModelMapper modelMapper, ValidationService validationService) {
        this.dishService = dishService;
        this.modelMapper = modelMapper;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<DishDTO> saveDish(@RequestBody @Valid DishDTO dishDTO, BindingResult bindingResult) {
        validationService.validate(bindingResult);
        Dish dish = convertDishDTOToDish(dishDTO);
        Dish dishSaved = dishService.save(dish);

        DishDTO dishDTOResponse = convertDishToDishDTO(dishSaved);
        return new ResponseEntity<>(dishDTOResponse, HttpStatus.CREATED);
    }

    private Dish convertDishDTOToDish(DishDTO dishDTO) {
        return modelMapper.map(dishDTO, Dish.class);
    }

    private DishDTO convertDishToDishDTO(Dish dish) {
        return modelMapper.map(dish, DishDTO.class);
    }

}
