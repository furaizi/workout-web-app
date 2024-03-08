package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.dto.DayOfWeek;
import com.example.trainingwebapp.models.dto.Exercise;
import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.dto.TrainingDay;
import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TrainingParser {

    private static final int SETS_INDEX = 0;
    private static final int REPS_INDEX = 1;
    private static final int WORK_WEIGHT_INDEX = 2;

    public static Training parse(HttpServletRequest request) throws Exception {
        String trainingName = request.getParameter("trainingName");
        List<TrainingDay> trainingDays = new ArrayList<>();

        JSONArray rowsAndExercisesJSON = new JSONArray(request.getParameter("rowsAndExercises"));
        HashMap<Integer, Integer> rowsAndExercises = JSONtoMap(rowsAndExercisesJSON);

        for (var pair : rowsAndExercises.entrySet()) {
            Integer rowId = pair.getKey();
            Integer maxExerciseId = pair.getValue();

            if (!hasTrainingDay(request, rowId))
                continue;

            TrainingDay newTrainingDay = parseTrainingDayFromForm(request, rowId, maxExerciseId);
            trainingDays.add(newTrainingDay);
        }

        return new Training(trainingName, trainingDays);
    }

    private static HashMap<Integer, Integer> JSONtoMap(JSONArray jsonMap) {
        HashMap<Integer, Integer> normalMap = new HashMap<>();
        for (int i = 0; i < jsonMap.length(); i++) {
            JSONArray pair = jsonMap.getJSONArray(i);
            Integer key = (Integer) pair.get(0);
            Integer value = (Integer) pair.get(1);
            normalMap.put(key, value);
        }

        return normalMap;
    }

    private static boolean hasTrainingDay(HttpServletRequest request, Integer rowId) throws Exception {
        var dayOfWeek = generateHTMLName("dayOfWeek", rowId);
        return request.getParameter(dayOfWeek) != null;
    }

    private static String generateHTMLName(String baseName, Integer rowId, Integer exerciseId) {
        return generateHTMLName(baseName, rowId) + "_" + exerciseId;
    }

    private static String generateHTMLName(String baseName, Integer rowId) {
        return baseName + "_" + rowId;
    }

    private static TrainingDay parseTrainingDayFromForm(HttpServletRequest request, Integer rowId, Integer maxExerciseId) throws Exception {
        var dayOfWeekParameterName = generateHTMLName("dayOfWeek", rowId);
        String dayOfWeek = request.getParameter(dayOfWeekParameterName);
        List<Exercise> exercises = new ArrayList<>();

        for (int exerciseId = 1; exerciseId <= maxExerciseId; exerciseId++) {
            if (!hasExercise(request, rowId, exerciseId))
                continue;

            Exercise newExercise = parseExerciseFromForm(request, rowId, exerciseId);
            exercises.add(newExercise);
        }

        return new TrainingDay(DayOfWeek.getByString(dayOfWeek), exercises);
    }

    private static boolean hasExercise(HttpServletRequest request, Integer rowId, Integer exerciseId) throws Exception {
        var exerciseFieldName = generateHTMLName("exerciseField", rowId, exerciseId);
        return request.getParameter(exerciseFieldName) != null;
    }

    private static Exercise parseExerciseFromForm(HttpServletRequest request, Integer rowId, Integer exerciseId) throws Exception {
        var exerciseParameterName = generateHTMLName("exerciseField", rowId, exerciseId);
        var numberParameterName = generateHTMLName("numberField", rowId, exerciseId);

        String exerciseName = request.getParameter(exerciseParameterName);
        int[] numberFields = Arrays.stream( request.getParameterValues(numberParameterName) )
                .mapToInt(Integer::parseInt)
                .toArray();

        var numberOfSets = numberFields[SETS_INDEX];
        var numberOfReps = numberFields[REPS_INDEX];
        var workWeight = numberFields[WORK_WEIGHT_INDEX];

        return new Exercise(exerciseName, numberOfReps, numberOfSets, workWeight);
    }
}
