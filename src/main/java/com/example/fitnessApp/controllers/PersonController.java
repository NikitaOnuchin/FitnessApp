package com.example.fitnessApp.controllers;

import com.example.fitnessApp.dto.PersonDTO;
import com.example.fitnessApp.models.Person;
import com.example.fitnessApp.services.PersonService;
import com.example.fitnessApp.services.ValidationService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private final ModelMapper modelMapper;
    private final ValidationService validationService;

    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper, ValidationService validationService) {
        this.personService = personService;
        this.modelMapper = modelMapper;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<PersonDTO> savePerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        validationService.validate(bindingResult);
        Person person = convertPersonDTOToPerson(personDTO);
        Person personSaved = personService.save(person);

        PersonDTO personDTOResponse = convertPersonToPersonDTO(personSaved);
        return new ResponseEntity<>(personDTOResponse, HttpStatus.CREATED);
    }

    private Person convertPersonDTOToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertPersonToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
