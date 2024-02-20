package com.example.trainingwebapp.model.dto;

import java.util.List;
import java.util.Objects;

public class TrainingDay implements Comparable<TrainingDay> {


    private int id;
    private DayOfWeek day;
    private List<Exercise> exercises;

    public TrainingDay(DayOfWeek day, List<Exercise> exercises) {
        this.day = day;
        this.exercises = exercises;
    }

    public TrainingDay(int id, DayOfWeek day, List<Exercise> exercises) {
        this(day, exercises);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public int compareTo(TrainingDay o) {
        return this.day.ordinal() - o.day.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingDay that = (TrainingDay) o;
        return id == that.id &&
                day == that.day &&
                Objects.equals(exercises, that.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, exercises);
    }
}
