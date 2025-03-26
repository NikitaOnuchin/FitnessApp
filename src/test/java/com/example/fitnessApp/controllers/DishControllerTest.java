package com.example.fitnessApp.controllers;

import com.example.fitnessApp.dto.DishDTO;
import com.example.fitnessApp.models.Dish;
import com.example.fitnessApp.services.DishService;
import com.example.fitnessApp.services.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    @Mock
    private DishService dishService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private DishController dishController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Dish dish;
    private DishDTO dishDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();
        objectMapper = new ObjectMapper();

        dishDTO = new DishDTO();
        dishDTO.setName("Pasta");
        dishDTO.setCalories(250);
        dishDTO.setProteins(2.3);
        dishDTO.setFats(15.1);
        dishDTO.setCarbohydrates(4.0);

        dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");
        dish.setCalories(250);
        dish.setProteins(2.3);
        dish.setFats(15.1);
        dish.setCarbohydrates(4.0);
    }

    @Test
    void testSaveDish_Success() throws Exception {
        when(modelMapper.map(any(DishDTO.class), eq(Dish.class))).thenReturn(dish);
        when(dishService.save(any(Dish.class))).thenReturn(dish);
        when(modelMapper.map(any(Dish.class), eq(DishDTO.class))).thenReturn(dishDTO);

        mockMvc.perform(post("/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pasta"))
                .andExpect(jsonPath("$.calories").value(250))
                .andExpect(jsonPath("$.proteins").value(2.3))
                .andExpect(jsonPath("$.fats").value(15.1))
                .andExpect(jsonPath("$.carbohydrates").value(4.0));

        verify(dishService, times(1)).save(any(Dish.class));
    }

}
