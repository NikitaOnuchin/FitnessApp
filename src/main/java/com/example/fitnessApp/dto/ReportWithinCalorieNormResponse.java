package com.example.fitnessApp.dto;

public class ReportWithinCalorieNormResponse {

    Boolean withinCalorieNorm;

    public ReportWithinCalorieNormResponse(Boolean isWithinCalorieNorm) {
        this.withinCalorieNorm = isWithinCalorieNorm;
    }

    public Boolean getWithinCalorieNorm() {
        return withinCalorieNorm;
    }

    public void setWithinCalorieNorm(Boolean withinCalorieNorm) {
        this.withinCalorieNorm = withinCalorieNorm;
    }
}
