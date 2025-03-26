package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotCreateException;
import com.example.fitnessApp.exception.NotFoundException;
import com.example.fitnessApp.models.Goal;
import com.example.fitnessApp.models.Person;
import com.example.fitnessApp.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private PersonService personService;

    @Test
    void testSavePersonSuccessfully() {
        Person person = new Person();
        person.setName("John");
        person.setWeight(80);
        person.setHeight(180);
        person.setAge(30);
        person.setEmail("john@mail.ru");
        person.setGoal(Goal.MAINTENANCE);

        when(validationService.existsByEmail(person.getEmail())).thenReturn(false);
        when(personRepository.save(person)).thenReturn(person);

        Person savedPerson = personService.save(person);

        assertNotNull(savedPerson);
        assertEquals("John", savedPerson.getName());
        assertEquals("john@mail.ru", savedPerson.getEmail());

        verify(validationService, times(1)).existsByEmail(person.getEmail());
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void testSavePersonThrowsNotCreateExceptionIfEmailExists() {
        Person person = new Person();
        person.setEmail("existing@mail.ru");
        when(validationService.existsByEmail(person.getEmail())).thenReturn(true);

        NotCreateException exception = assertThrows(NotCreateException.class, () -> personService.save(person));

        assertEquals("Email already exist", exception.getMessage());
        verify(personRepository, never()).save(person);
    }

    @Test
    void testFindByIdSuccessfully() {
        Person person = new Person();
        person.setId(1L);
        person.setName("Alice");
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        Person foundPerson = personService.findById(1L);

        assertNotNull(foundPerson);
        assertEquals(1L, foundPerson.getId());
        assertEquals("Alice", foundPerson.getName());
        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdThrowsNotFoundException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> personService.findById(99L));

        assertEquals("Person not found", exception.getMessage());
        verify(personRepository, times(1)).findById(99L);
    }
}
