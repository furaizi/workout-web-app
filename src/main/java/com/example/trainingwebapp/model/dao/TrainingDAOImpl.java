package com.example.trainingwebapp.model.dao;

import com.example.trainingwebapp.model.dto.DayOfWeek;
import com.example.trainingwebapp.model.dto.Exercise;
import com.example.trainingwebapp.model.dto.Training;
import com.example.trainingwebapp.model.dto.TrainingDay;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrainingDAOImpl implements TrainingDAO {

    private DataSource dataSource;

    public TrainingDAOImpl(DataSource dataSource) throws Exception {
        this.dataSource = dataSource;
    }

    @Override
    public void addUser() throws Exception {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = dataSource.getConnection();
            var sql = "INSERT INTO users " +
                    "(user_name) " +
                    "VALUES (null)";
            statement = connection.createStatement();
            statement.execute(sql);
        }
        finally {
            close(connection, statement);
        }
    }

    public int getLastId() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            var sql = "SELECT user_id FROM users " +
                    "ORDER BY user_id DESC " +
                    "LIMIT 1";
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                return id;
            }
            else {
                throw new Exception("Could not find the last id.");
            }
        }
        finally {
            close(connection, statement, resultSet);
        }
    }


    @Override
    public List<Training> getTrainings(int userId) throws Exception {
        List<Training> trainings = new ArrayList<>();
//        boolean hasTrainings = false;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            var sql = "SELECT * FROM training WHERE user_id=?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, userId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var trainingId = resultSet.getInt("training_id");
                var trainingName = resultSet.getString("training_name");

                Training newTraining = new Training(trainingId, trainingName);
                trainings.add(newTraining);
//                hasTrainings = true;
            }

//            if (!hasTrainings) {
//                throw new Exception("Could not find any trainings. User id: " + userId);
//            }

            return trainings;
        }
        finally {
            close(connection, statement, resultSet);
        }

    }

    @Override
    public void addTraining(Training training, int userId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            String sql = "INSERT INTO training " +
                    "(training_name, user_id) " +
                    "VALUES (?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setString(1, training.getName());
            statement.setInt(2, userId);
            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public Training getTraining(int trainingId) throws Exception {
        Training training = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            var sql = "SELECT * FROM training WHERE training_id=?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, trainingId);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var trainingName = resultSet.getString("training_name");
                training = new Training(trainingId, trainingName);
            }
            else {
                throw new Exception("Could not find training id: " + training);
            }

            return training;
        }
        finally {
            close(connection, statement, resultSet);
        }
    }

    @Override
    public void updateTraining(Training training) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            String sql = "UPDATE training " +
                    "SET training_name=? " +
                    "WHERE training_id=?";
            statement = connection.prepareStatement(sql);

            var trainingName = training.getName();
            statement.setString(1, trainingName);
            statement.setInt(2, training.getId());

            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public void deleteTraining(int trainingId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            String sql = "DELETE FROM training WHERE training_id=?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, trainingId);
            statement.execute();
        }
        finally {
            close(connection, statement);
        }

    }

    @Override
    public List<TrainingDay> getTrainingDays(int trainingId) throws Exception {
        List<TrainingDay> trainingDays = new ArrayList<>();
        boolean hasTrainingDay = false;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            var sql = "SELECT * FROM training_day WHERE training_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, trainingId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var trainingDayId = resultSet.getInt("training_day_id");
                var dayOfWeek = DayOfWeek.getByString( resultSet.getString("day_of_week") );

                TrainingDay newTrainingDay = new TrainingDay(trainingDayId, dayOfWeek, null);
                trainingDays.add(newTrainingDay);
                hasTrainingDay = true;
            }
        }
        finally {
            close(connection, statement, resultSet);
        }

        if (!hasTrainingDay)
            throw new Exception("Could not find any training days. Training id: " + trainingId);

        return trainingDays;
    }

    @Override
    public void addTrainingDay(TrainingDay trainingDay, int trainingId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            var sql = "INSERT INTO training_day " +
                    "(day_of_week, training_id) " +
                    "VALUES (?, ?)";

            statement = connection.prepareStatement(sql);
            var dayOfWeek = trainingDay.getDay().toString();
            statement.setString(1, dayOfWeek);
            statement.setInt(2, trainingId);

            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public TrainingDay getTrainingDay(int trainingDayId) throws Exception {
        TrainingDay trainingDay = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            var sql = "SELECT * FROM training_day WHERE training_day_id=?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, trainingDayId);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var dayOfWeek = DayOfWeek.getByString( resultSet.getString("day_of_week") );

                trainingDay = new TrainingDay(trainingDayId, dayOfWeek, null);
            }
            else {
                throw new Exception("Could not find training day id: " + trainingDayId);
            }

            return trainingDay;
        }
        finally {
            close(connection, statement, resultSet);
        }
    }

    @Override
    public void updateTrainingDay(TrainingDay trainingDay) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            String sql = "UPDATE training_day " +
                    "SET day_of_week=? " +
                    "WHERE training_day_id=?";
            statement = connection.prepareStatement(sql);

            var dayOfWeek = trainingDay.getDay().toString();
            statement.setString(1, dayOfWeek);
            statement.setInt(2, trainingDay.getId());

            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public void deleteTrainingDay(int trainingDayId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            String sql = "DELETE FROM training_day WHERE training_day_id=?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, trainingDayId);
            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public List<Exercise> getExercises(int trainingDayId) throws Exception {
        List<Exercise> exercises = new ArrayList<>();
        boolean hasExercise = false;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            var sql = "SELECT * FROM exercise WHERE training_day_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, trainingDayId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var exerciseId = resultSet.getInt("exercise_id");
                var exerciseName = resultSet.getString("exercise_name");
                var numberOfReps = resultSet.getInt("number_of_reps");
                var numberOfSets = resultSet.getInt("number_of_sets");
                var workWeight = resultSet.getInt("work_weight");

                Exercise newExercise = new Exercise(exerciseId, exerciseName, numberOfReps, numberOfSets, workWeight);
                exercises.add(newExercise);
                hasExercise = true;
            }

            if (!hasExercise)
                throw new Exception("Could not find any exercise. Training day id: " + trainingDayId);

            return exercises;
        }
        finally {
            close(connection, statement, resultSet);
        }
    }

    @Override
    public void addExercise(Exercise exercise, int trainingDayId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            var sql = "INSERT INTO exercise " +
                    "(exercise_name, number_of_reps, number_of_sets, work_weight, training_day_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, exercise.getName());
            statement.setInt(2, exercise.getNumberOfReps());
            statement.setInt(3, exercise.getNumberOfSets());
            statement.setInt(4, exercise.getWorkWeight());
            statement.setInt(5, trainingDayId);

            statement.execute();
        }
        finally {
            close(connection, statement, null);
        }
    }

    @Override
    public Exercise getExercise(int exerciseId) throws Exception {
        Exercise exercise = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            var sql = "SELECT * FROM exercise WHERE exercise_id=?";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var exerciseName = resultSet.getString("exercise_name");
                var numberOfReps = resultSet.getInt("number_of_reps");
                var numberOfSets = resultSet.getInt("number_of_sets");
                var workWeight = resultSet.getInt("work_weight");

                exercise = new Exercise(exerciseId, exerciseName, numberOfReps, numberOfSets, workWeight);
            }
            else {
                throw new Exception("Could not find exercise id: " + exerciseId);
            }

            return exercise;
        }
        finally {
            close(connection, statement, resultSet);
        }
    }

    @Override
    public void updateExercise(Exercise exercise) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            var sql = "UPDATE exercise " +
                    "SET exercise_name=?, number_of_reps=?, number_of_sets=?, work_weight=? " +
                    "WHERE exercise_id=?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, exercise.getName());
            statement.setInt(2, exercise.getNumberOfReps());
            statement.setInt(3, exercise.getNumberOfSets());
            statement.setInt(4, exercise.getWorkWeight());
            statement.setInt(5, exercise.getId());

            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public void deleteExercise(int exerciseId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            var sql = "DELETE FROM exercise WHERE exercise_id=?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, exerciseId);
            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public void deleteAllTrainingDays(int trainingId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            var sql = "DELETE FROM training_day WHERE training_id=?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, trainingId);
            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    @Override
    public void deleteAllExercises(int trainingDayId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            var sql = "DELETE FROM exercise WHERE training_day_id=?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, trainingDayId);
            statement.execute();
        }
        finally {
            close(connection, statement);
        }
    }

    private void close(Connection connection, Statement statement) {
        try {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null)
                resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        close(connection, statement);
    }

}
