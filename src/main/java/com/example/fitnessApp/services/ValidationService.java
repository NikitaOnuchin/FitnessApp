package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotCreateException;
import com.example.fitnessApp.repositories.DishRepository;
import com.example.fitnessApp.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidationService {

    private final PersonRepository personRepository;
    private final DishRepository dishRepository;

    @Autowired
    public ValidationService(PersonRepository personRepository, DishRepository dishRepository) {
        this.personRepository = personRepository;
        this.dishRepository = dishRepository;
    }

    public void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
            }
            throw new NotCreateException(errors.toString());
        }
    }

    public boolean existsByEmail(String email) throws NotCreateException {
        return personRepository.existsByEmail(email);
    }

    public boolean existsByName(String name) {
        return dishRepository.existsByName(name);
    }
}
