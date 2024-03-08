package com.example.trainingwebapp.models.dao;

import com.example.trainingwebapp.models.dto.Exercise;
import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.dto.TrainingDay;

import java.util.List;

public interface TrainingDAO {

    void addUser() throws Exception;
    int getLastId() throws Exception;

    List<Training> getTrainings(int userId) throws Exception;
    void addTraining(Training training, int userId) throws Exception;
    Training getTraining(int trainingId) throws Exception;
    void updateTraining(Training training) throws Exception;
    void deleteTraining(int trainingId) throws Exception;


    List<TrainingDay> getTrainingDays(int trainingId) throws Exception;
    void addTrainingDay(TrainingDay trainingDay, int trainingId) throws Exception;
    TrainingDay getTrainingDay(int trainingDayId) throws Exception;
    void updateTrainingDay(TrainingDay trainingDay) throws Exception;
    void deleteTrainingDay(int trainingDayId) throws Exception;

    List<Exercise> getExercises(int trainingDayId) throws Exception;
    void addExercise(Exercise exercise, int trainingDayId) throws Exception;
    Exercise getExercise(int exerciseId) throws Exception;
    void updateExercise(Exercise exercise) throws Exception;
    void deleteExercise(int exerciseId) throws Exception;

    void deleteAllTrainingDays(int trainingId) throws Exception;
    void deleteAllExercises(int trainingDayId) throws Exception;
}
