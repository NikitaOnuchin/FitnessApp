package com.example.fitnessApp.services;

import com.example.fitnessApp.exception.NotCreateException;
import com.example.fitnessApp.repositories.DishRepository;
import com.example.fitnessApp.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ValidationServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testValidateThrowsExceptionWhenErrorsExist() {
        FieldError fieldError = new FieldError("person", "email", "Email is invalid");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        NotCreateException exception = assertThrows(NotCreateException.class, () -> validationService.validate(bindingResult));

        assertTrue(exception.getMessage().contains("email: Email is invalid"));
        verify(bindingResult, times(1)).getFieldErrors();
    }

    @Test
    void testValidateDoesNotThrowExceptionWhenNoErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);
        assertDoesNotThrow(() -> validationService.validate(bindingResult));
    }

    @Test
    void testExistsByEmailCallsRepository() {
        when(personRepository.existsByEmail("test@mail.ru")).thenReturn(true);

        boolean result = validationService.existsByEmail("test@mail.ru");

        assertTrue(result);
        verify(personRepository, times(1)).existsByEmail("test@mail.ru");
    }

    @Test
    void testExistsByNameCallsRepository() {
        when(dishRepository.existsByName("Pizza")).thenReturn(false);

        boolean result = validationService.existsByName("Pizza");

        assertFalse(result);
        verify(dishRepository, times(1)).existsByName("Pizza");
    }
}
