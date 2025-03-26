package com.example.fitnessApp.repositories;

import com.example.fitnessApp.models.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testExistsByEmail() {
        Person person = new Person();
        person.setName("John");
        person.setEmail("john@mail.ru");
        person.setAge(30);
        person.setWeight(75);
        person.setHeight(180);

        personRepository.save(person);

        boolean exists = personRepository.existsByEmail("john@mail.ru");

        assertTrue(exists);

        boolean notExists = personRepository.existsByEmail("notexist@mail.ru");
        assertFalse(notExists);
    }

    @Test
    public void testSaveAndFindDish() {
        Person person = new Person();
        person.setName("Viktor");
        person.setEmail("viktor@mail.ru");
        person.setAge(40);
        person.setWeight(100);
        person.setHeight(190);
        Person savedPerson = personRepository.save(person);

        Person foundPerson = personRepository.findById(savedPerson.getId()).orElse(null);

        assertNotNull(foundPerson);
        assertEquals("Viktor", foundPerson.getName());
        assertEquals(40, foundPerson.getAge());
    }
}
