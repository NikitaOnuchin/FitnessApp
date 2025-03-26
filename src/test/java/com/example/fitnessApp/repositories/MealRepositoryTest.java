package com.example.fitnessApp.repositories;

import com.example.fitnessApp.models.Meal;
import com.example.fitnessApp.models.MealType;
import com.example.fitnessApp.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MealRepositoryTest {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    void setUp() {
        // Создаем человека
        person = new Person();
        person.setName("John");
        person.setEmail("john@mail.ru");
        person.setAge(30);
        person.setWeight(75);
        person.setHeight(180);

        // Сохраняем человека в репозитории
        person = personRepository.save(person);

        // Создаем типы еды
        MealType mealTypeBreakfast = MealType.BREAKFAST;
        MealType mealTypeLunch = MealType.LUNCH;

        // Создаем прием пищи
        Meal mealBreakfast = new Meal();
        mealBreakfast.setPerson(person);
        mealBreakfast.setMealType(mealTypeBreakfast);
        mealBreakfast.setCreateDate(LocalDate.of(2023, 3, 25));

        Meal mealLunch = new Meal();
        mealLunch.setPerson(person);
        mealLunch.setMealType(mealTypeLunch);
        mealLunch.setCreateDate(LocalDate.of(2023, 3, 25));

        // Сохраняем прием пищи
        mealRepository.save(mealBreakfast);
        mealRepository.save(mealLunch);
    }

    @Test
    void testFindMealByPersonAndCreateDate() {
        LocalDate date = LocalDate.of(2023, 3, 25);

        List<Meal> meals = mealRepository.findMealByPersonAndCreateDate(person, date);

        assertNotNull(meals);
        assertEquals(2, meals.size());
    }
}
