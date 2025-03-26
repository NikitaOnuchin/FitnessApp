package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotCreateException;
import com.example.fitnessApp.exception.NotFoundException;
import com.example.fitnessApp.models.Person;
import com.example.fitnessApp.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final ValidationService validationService;

    @Autowired
    public PersonService(PersonRepository personRepository, ValidationService validationService) {
        this.personRepository = personRepository;
        this.validationService = validationService;
    }

    @Transactional
    public Person save(Person person) throws NotCreateException {
        if (validationService.existsByEmail(person.getEmail())) {
            throw new NotCreateException("Email already exist");
        }
        getCalorieCalculation(person);
        return personRepository.save(person);
    }

    private void getCalorieCalculation(Person person) {
        double BMR = 88.362 +
                (9.247 * person.getWeight()) +
                (3.098 * person.getHeight()) - (4.330 * person.getAge());
        double TDEE = BMR * 1.55;
        switch (person.getGoal()) {
            case WEIGHT_LOSS:
                person.setCalorieNorm((int) Math.round(TDEE * 0.85));
                break;
            case MAINTENANCE:
                person.setCalorieNorm((int) Math.round(TDEE));
                break;
            default:
                person.setCalorieNorm((int) Math.round(TDEE * 1.15));
        }
    }

    public Person findById(long id) {
        return personRepository.findById(id).orElseThrow(()-> new NotFoundException("Person not found"));
    }
}
