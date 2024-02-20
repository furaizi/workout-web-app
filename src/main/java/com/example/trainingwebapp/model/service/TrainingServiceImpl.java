package com.example.trainingwebapp.model.service;

import com.example.trainingwebapp.model.dao.TrainingDAO;
import com.example.trainingwebapp.model.dto.Training;
import com.example.trainingwebapp.model.dto.TrainingDay;

import java.util.List;

public class TrainingServiceImpl implements TrainingService {

    private TrainingDAO trainingDAO;

    public TrainingServiceImpl(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public void addUser() throws Exception {
        trainingDAO.addUser();
    }

    public int getLastId() throws Exception {
        return trainingDAO.getLastId();
    }

    public boolean userHasTraining(int userId, int trainingId) throws Exception {
        var userTrainings = getFullTrainings(userId);
        var wantedTraining = getFullTraining(trainingId);

        return userTrainings.contains(wantedTraining);
    }

    @Override
    public List<Training> getFullTrainings(int userId) throws Exception {
        var fullTrainings = trainingDAO.getTrainings(userId);

        for (var fullTraining : fullTrainings) {
            var fullTrainingDays = getFullTrainingDays(fullTraining.getId());
            fullTraining.setTrainingDays(fullTrainingDays);
        }

        return fullTrainings;
    }

    @Override
    public Training getFullTraining(int trainingId) throws Exception {
        var fullTraining = trainingDAO.getTraining(trainingId);
        var fullTrainingDays = getFullTrainingDays(trainingId);
        fullTraining.setTrainingDays(fullTrainingDays);

        return fullTraining;
    }

    @Override
    public void addFullTraining(Training training, int userId) throws Exception {
        trainingDAO.addTraining(training, userId);

        var trainingWithId = trainingDAO.getTrainings(userId)
                .stream()
                .filter(tr -> tr.getName().equals( training.getName() ))
                .findFirst()
                .orElseThrow();

        var trainingDays = training.getTrainingDays();
        var trainingId = trainingWithId.getId();

        for (var trainingDay : trainingDays) {
            addFullTrainingDay(trainingDay, trainingId);
        }
    }

    @Override
    public void updateFullTraining(Training training) throws Exception {
        trainingDAO.updateTraining(training);

        var oldTraining = getFullTraining(training.getId());
        var oldTrainingDays = oldTraining.getTrainingDays();
        var newTrainingDays = training.getTrainingDays();

        for (var oldTrainingDay : oldTrainingDays) {
            deleteFullTrainingDay( oldTrainingDay.getId() );
        }

        for (var newTrainingDay : newTrainingDays) {
            addFullTrainingDay(newTrainingDay, training.getId());
        }
    }


    @Override
    public void deleteFullTraining(int trainingId) throws Exception {
        var trainingDays = trainingDAO.getTrainingDays(trainingId);
        for (var trainingDay : trainingDays) {
            deleteFullTrainingDay( trainingDay.getId() );
        }

        trainingDAO.deleteTraining(trainingId);
    }




    private List<TrainingDay> getFullTrainingDays(int trainingId) throws Exception {
        var fullTrainingDays = trainingDAO.getTrainingDays(trainingId);

        for (var fullTrainingDay : fullTrainingDays) {
            var exercises = trainingDAO.getExercises( fullTrainingDay.getId() );
            fullTrainingDay.setExercises(exercises);
        }

        return fullTrainingDays;
    }

    private TrainingDay getFullTrainingDay(int trainingDayId) throws Exception {
        var fullTrainingDay = trainingDAO.getTrainingDay(trainingDayId);
        var exercises = trainingDAO.getExercises(trainingDayId);
        fullTrainingDay.setExercises(exercises);

        return fullTrainingDay;
    }

    private void addFullTrainingDay(TrainingDay trainingDay, int trainingId) throws Exception {
        trainingDAO.addTrainingDay(trainingDay, trainingId);

        var trainingDayWithId = trainingDAO.getTrainingDays(trainingId)
                .stream()
                .filter(trDay -> trDay.getDay() == trainingDay.getDay())
                .findFirst()
                .get();

        var exercises = trainingDay.getExercises();
        var trainingDayId = trainingDayWithId.getId();

        for (var exercise : exercises) {
            trainingDAO.addExercise(exercise, trainingDayId);
        }
    }

    private void deleteFullTrainingDay(int trainingDayId) throws Exception {
        var exercises = trainingDAO.getExercises(trainingDayId);
        for (var exercise : exercises) {
            trainingDAO.deleteExercise( exercise.getId() );
        }

        trainingDAO.deleteTrainingDay( trainingDayId );
    }

    private void updateFullTrainingDay(TrainingDay trainingDay) throws Exception {
        trainingDAO.updateTrainingDay(trainingDay);
        var exercises = trainingDay.getExercises();

        for (var exercise : exercises) {
            trainingDAO.updateExercise(exercise);
        }
    }
}
