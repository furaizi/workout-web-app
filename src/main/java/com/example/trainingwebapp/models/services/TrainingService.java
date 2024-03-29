package com.example.trainingwebapp.models.services;

import com.example.trainingwebapp.models.dto.Training;

import java.util.List;

public interface TrainingService {

    int getLastId() throws Exception;
    void addUser() throws Exception;
    boolean userHasTraining(int userId, int trainingId) throws Exception;

    List<Training> getFullTrainings(int userId) throws Exception;
    Training getFullTraining(int trainingId) throws Exception;
    void addFullTraining(Training training, int userId) throws Exception;
    void updateFullTraining(Training training) throws Exception;
    void deleteFullTraining(int trainingId) throws Exception;
}
