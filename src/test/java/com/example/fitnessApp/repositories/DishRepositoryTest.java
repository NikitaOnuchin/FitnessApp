package com.example.fitnessApp.repositories;

import com.example.fitnessApp.models.Dish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DishRepositoryTest {

    @Autowired
    private DishRepository dishRepository;

    @Test
    public void testExistsByName() {
        Dish dish = new Dish();
        dish.setName("Pasta");
        dish.setCalories(350);
        dish.setProteins(10.0);
        dish.setFats(5.7);
        dish.setCarbohydrates(6.5);
        dishRepository.save(dish);

        boolean exists = dishRepository.existsByName("Pasta");

        assertTrue(exists);
    }

    @Test
    public void testExistsByName_NotExists() {
        boolean exists = dishRepository.existsByName("Pizza");

        assertFalse(exists);
    }

    @Test
    public void testSaveAndFindDish() {
        Dish dish = new Dish();
        dish.setName("Apple");
        dish.setCalories(100);
        dish.setProteins(5.0);
        dish.setFats(2.7);
        dish.setCarbohydrates(3.5);
        Dish savedDish = dishRepository.save(dish);

        Dish foundDish = dishRepository.findById(savedDish.getId()).orElse(null);

        assertNotNull(foundDish);
        assertEquals("Apple", foundDish.getName());
        assertEquals(100, foundDish.getCalories());
    }
}
