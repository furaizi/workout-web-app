package com.example.trainingwebapp.models.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Training {

    public static final Training EMPTY = new Training(null, (List<TrainingDay>) null);

    private int id;
    private String name;
    private List<TrainingDay> trainingDays;

    public Training(String name, List<TrainingDay> trainingDays) {
        this.name = name;
        this.trainingDays = trainingDays;
    }

    public Training(String name, TrainingDay... trainingDays) {
        this.name = name;
        this.trainingDays = new ArrayList<>();
        Collections.addAll(this.trainingDays, trainingDays);
    }

    public Training(int id, String name, List<TrainingDay> trainingDays) {
        this(name, trainingDays);
        this.id = id;
    }

    public Training(int id, String name, TrainingDay... trainingDays) {
        this(name, trainingDays);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TrainingDay> getTrainingDays() {
        return trainingDays;
    }

    public void setTrainingDays(List<TrainingDay> trainingDays) {
        this.trainingDays = trainingDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id == training.id &&
                Objects.equals(name, training.name)
                && Objects.equals(trainingDays, training.trainingDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, trainingDays);
    }
}
