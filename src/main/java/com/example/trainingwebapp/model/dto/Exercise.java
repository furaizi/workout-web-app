package com.example.trainingwebapp.model.dto;

import org.json.JSONObject;
import org.json.JSONString;

import java.util.Objects;

public class Exercise {

    private int id;
    private String name;
    private int numberOfReps;
    private int numberOfSets;
    private int workWeight;

    public Exercise(String name, int numberOfReps, int numberOfSets, int workWeight) {
        this.name = name;
        this.numberOfReps = numberOfReps;
        this.numberOfSets = numberOfSets;
        this.workWeight = workWeight;
    }

    public Exercise(int id, String name, int numberOfReps, int numberOfSets, int workWeight) {
        this(name, numberOfReps, numberOfSets, workWeight);
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

    public int getNumberOfReps() {
        return numberOfReps;
    }
    public void setNumberOfReps(int numberOfReps) {
        this.numberOfReps = numberOfReps;
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }
    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public int getWorkWeight() {
        return workWeight;
    }
    public void setWorkWeight(int workWeight) {
        this.workWeight = workWeight;
    }

    public String toJSON() {
        JSONObject exercise = new JSONObject(this);
        return exercise.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfReps, numberOfSets, workWeight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id == exercise.id &&
                numberOfReps == exercise.numberOfReps &&
                numberOfSets == exercise.numberOfSets &&
                workWeight == exercise.workWeight &&
                Objects.equals(name, exercise.name);
    }
}
