package com.example.trainingwebapp.models.dto;

import java.util.List;

public class User {
    private int id;
    private String name;
    private List<Training> trainings;

    public User(List<Training> trainings) {
        this.trainings = trainings;
    }

    public User(int id, List<Training> trainings) {
        this(trainings);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}
