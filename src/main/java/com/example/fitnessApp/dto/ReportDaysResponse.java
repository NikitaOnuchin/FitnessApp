package com.example.fitnessApp.dto;

import java.util.List;

public class ReportDaysResponse {
    private List<DayResponse> daysResponse;

    public ReportDaysResponse(List<DayResponse> daysResponse) {
        this.daysResponse = daysResponse;
    }

    public List<DayResponse> getDaysResponse() {
        return daysResponse;
    }

    public void setDaysResponse(List<DayResponse> daysResponse) {
        this.daysResponse = daysResponse;
    }

    public static class DayResponse {
        private String day;
        private Integer sumCalories;
        private Integer countMeals;

        public DayResponse(String day, Integer sumCalories, Integer countMeals) {
            this.day = day;
            this.sumCalories = sumCalories;
            this.countMeals = countMeals;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public Integer getSumCalories() {
            return sumCalories;
        }

        public void setSumCalories(Integer sumCalories) {
            this.sumCalories = sumCalories;
        }

        public Integer getCountMeals() {
            return countMeals;
        }

        public void setCountMeals(Integer countMeals) {
            this.countMeals = countMeals;
        }
    }
}
