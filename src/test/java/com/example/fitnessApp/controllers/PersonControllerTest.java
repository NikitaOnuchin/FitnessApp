package com.example.fitnessApp.controllers;

import com.example.fitnessApp.dto.PersonDTO;
import com.example.fitnessApp.models.Goal;
import com.example.fitnessApp.models.Person;
import com.example.fitnessApp.services.PersonService;
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
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private PersonController personController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSavePerson_Success() throws Exception {
        Person person = new Person();
        person.setName("John");
        person.setWeight(80);
        person.setHeight(180);
        person.setAge(30);
        person.setEmail("john@mail.ru");
        person.setGoal(Goal.MAINTENANCE);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("John");
        personDTO.setWeight(80);
        personDTO.setHeight(180);
        personDTO.setAge(30);
        personDTO.setEmail("john@mail.ru");
        personDTO.setGoal(Goal.MAINTENANCE.toString());

        // Мокаем вызовы
        when(modelMapper.map(any(PersonDTO.class), eq(Person.class))).thenReturn(person);
        when(personService.save(person)).thenReturn(person);
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(personDTO);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@mail.ru"))
                .andExpect(jsonPath("$.age").value(30));

        verify(personService, times(1)).save(any(Person.class));
        verify(validationService, times(1)).validate(any());
    }
}
