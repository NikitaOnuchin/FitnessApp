package com.example.fitnessApp.controllers;

import com.example.fitnessApp.dto.ReportForDayRequest;
import com.example.fitnessApp.dto.ReportDaysResponse;
import com.example.fitnessApp.dto.ReportForDaysRequest;
import com.example.fitnessApp.dto.ReportWithinCalorieNormResponse;
import com.example.fitnessApp.models.Meal;
import com.example.fitnessApp.models.MealDish;
import com.example.fitnessApp.models.Person;
import com.example.fitnessApp.services.MealService;
import com.example.fitnessApp.services.PersonService;
import com.example.fitnessApp.services.ValidationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ValidationService validationService;
    private final MealService mealService;
    private final PersonService personService;

    public ReportController(ValidationService validationService, MealService mealService, PersonService personService) {
        this.validationService = validationService;
        this.mealService = mealService;
        this.personService = personService;
    }

    @GetMapping("/sumForDay")
    public ResponseEntity<ReportDaysResponse.DayResponse> getReportSumForDay(
            @Valid ReportForDayRequest request,
            BindingResult bindingResult
    ) {
        validationService.validate(bindingResult);

        LocalDate localDate = parseDate(request.getDate());
        List<Meal> mealList = mealService.findMealsByPersonAndCreateDate(request.getPersonId(), localDate);
        int sumCalories = getSumCalories(mealList);

        return new ResponseEntity<>(new ReportDaysResponse.DayResponse(localDate.toString(),
                sumCalories, mealList.size()), HttpStatus.OK);
    }

    @GetMapping("/isWithinCalorieNorm")
    public ResponseEntity<ReportWithinCalorieNormResponse> isWithinCalorieNorm(@Valid ReportForDayRequest request,
                                                                               BindingResult bindingResult) {
        validationService.validate(bindingResult);

        LocalDate localDate = parseDate(request.getDate());
        List<Meal> mealList = mealService.findMealsByPersonAndCreateDate(request.getPersonId(), localDate);
        int sumCalories = getSumCalories(mealList);
        Person person = personService.findById(request.getPersonId());

        return new ResponseEntity<>(new ReportWithinCalorieNormResponse(
                person.getCalorieNorm() >= sumCalories), HttpStatus.OK
        );
    }

    @GetMapping("/sumForDays")
    public ResponseEntity<ReportDaysResponse> getReportSumForDays(@Valid ReportForDaysRequest request,
                                                                  BindingResult bindingResult) {
        validationService.validate(bindingResult);

        List<LocalDate> localDateArray = parseDatesToArray(request.getDateFrom(), request.getDateTo());
        List<ReportDaysResponse.DayResponse> dayResponseList = getDayResponses(request, localDateArray);

        return new ResponseEntity<>(new ReportDaysResponse(dayResponseList), HttpStatus.OK);
    }

    private List<ReportDaysResponse.DayResponse> getDayResponses(ReportForDaysRequest request,
                                                                 List<LocalDate> localDateArray) {
        List<ReportDaysResponse.DayResponse> dayResponseList = new ArrayList<>();
        for (LocalDate localDate : localDateArray) {
            List<Meal> mealList = mealService.findMealsByPersonAndCreateDate(request.getPersonId(), localDate);
            int sumCalories = getSumCalories(mealList);
            dayResponseList.add(new ReportDaysResponse.DayResponse(localDate.toString(), sumCalories, mealList.size()));
        }
        return dayResponseList;
    }

    private int getSumCalories(List<Meal> mealList) {
        int sumCalories = 0;
        for (Meal meal : mealList){
            for(MealDish mealDish : meal.getMealDishes()){
                sumCalories = sumCalories + (mealDish.getDish().getCalories() * mealDish.getCount());
            }
        }
        return sumCalories;
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    private List<LocalDate> parseDatesToArray(String dateFrom, String dateTo) {
        LocalDate localDateFrom = parseDate(dateFrom);
        LocalDate localDateTo = parseDate(dateTo);

        return localDateFrom.datesUntil(localDateTo.plusDays(1))
                .toList();
    }
}
