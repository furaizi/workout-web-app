package com.example.trainingwebapp.controller;

import com.example.trainingwebapp.model.dao.TrainingDAOImpl;
import com.example.trainingwebapp.model.dto.DayOfWeek;
import com.example.trainingwebapp.model.dto.Exercise;
import com.example.trainingwebapp.model.dto.Training;
import com.example.trainingwebapp.model.dto.TrainingDay;
import com.example.trainingwebapp.model.service.TrainingService;
import com.example.trainingwebapp.model.service.TrainingServiceImpl;
import org.json.JSONArray;
import org.json.JSONString;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

@WebServlet("/workout-web-app")
public class TrainingControllerServlet extends HttpServlet {

    private static final Integer INITIAL_ROW_ID = 2;
    private static final int SETS_INDEX = 0;
    private static final int REPS_INDEX = 1;
    private static final int WORK_WEIGHT_INDEX = 2;

    private TrainingService trainingService;

    @Resource(name="jdbc/training_web_app")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            trainingService = new TrainingServiceImpl(new TrainingDAOImpl(dataSource));
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html");

            if (!hasUserId(request)) {
                Integer lastId = trainingService.getLastId();
                request.setAttribute("lastId", lastId);
                trainingService.addUser();

                RequestDispatcher dispatcher = request.getRequestDispatcher("/set-cookie");
                dispatcher.forward(request, response);
            }
            
            String command = request.getParameter("command");
            if (command == null)
                command = "";
            
            switch (command) {
                case "LIST" -> listTrainings(request, response);
                case "ADD" -> addTraining(request, response);
                case "LOAD" -> loadTraining(request, response);
                case "UPDATE" -> updateTraining(request, response);
                case "DELETE" -> deleteTraining(request, response);
                case "LOAD_TO_UPDATE" -> loadToUpdateTraining(request, response);
                default -> listTrainings(request, response);
            }
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listTrainings(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        List<Training> trainings = trainingService.getFullTrainings(userId);
        request.setAttribute("trainings", trainings);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/training/index.jsp");
        dispatcher.forward(request, response);
    }

    private void addTraining(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Training training = parseTrainingFromForm(request);
        int userId = getUserId(request);
        trainingService.addFullTraining(training, userId);

        listTrainings(request, response);
    }

    private void loadTraining(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        int trainingId = Integer.parseInt( request.getParameter("trainingId") );

        if (trainingService.userHasTraining(userId, trainingId)) {
            Training training = trainingService.getFullTraining(trainingId);
            request.setAttribute("rowId", INITIAL_ROW_ID);
            request.setAttribute("training", training);
        }

        listTrainings(request, response);
    }

    private void loadToUpdateTraining(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        int trainingId = Integer.parseInt( request.getParameter("trainingId") );

        if (trainingService.userHasTraining(userId, trainingId)) {
            Training training = trainingService.getFullTraining(trainingId);
            request.setAttribute("rowId", INITIAL_ROW_ID);
            request.setAttribute("training", training);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/training/update-training.jsp");
        dispatcher.forward(request, response);
    }

    private void updateTraining(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        int trainingId = Integer.parseInt( request.getParameter("trainingId") );

        if (trainingService.userHasTraining(userId, trainingId)) {
            Training training = parseTrainingFromForm(request);
            training.setId(trainingId);
            trainingService.updateFullTraining(training);
        }

        listTrainings(request, response);
    }

    private void deleteTraining(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        int trainingId = Integer.parseInt( request.getParameter("trainingId") );

        if (trainingService.userHasTraining(userId, trainingId))
            trainingService.deleteFullTraining(trainingId);

        listTrainings(request, response);
    }


    private Training parseTrainingFromForm(HttpServletRequest request) throws Exception {
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


    private boolean hasUserId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return false;

        return Arrays.stream(cookies)
                .anyMatch( cookie -> cookie.getName().equals("userId") );
    }

    private int getUserId(HttpServletRequest request) {
        Cookie userIdCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("userId"))
                .findFirst()
                .orElseThrow();
        int userId = Integer.parseInt( userIdCookie.getValue() );

        return userId;
    }

    private HashMap<Integer, Integer> JSONtoMap(JSONArray jsonMap) {
        HashMap<Integer, Integer> normalMap = new HashMap<>();
        for (int i = 0; i < jsonMap.length(); i++) {
            JSONArray pair = jsonMap.getJSONArray(i);
            Integer key = (Integer) pair.get(0);
            Integer value = (Integer) pair.get(1);
            normalMap.put(key, value);
        }

        return normalMap;
    }

    private TrainingDay parseTrainingDayFromForm(HttpServletRequest request, Integer rowId, Integer maxExerciseId) throws Exception {
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

    private Exercise parseExerciseFromForm(HttpServletRequest request, Integer rowId, Integer exerciseId) throws Exception {
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

    private boolean hasExercise(HttpServletRequest request, Integer rowId, Integer exerciseId) throws Exception {
        var exerciseFieldName = generateHTMLName("exerciseField", rowId, exerciseId);
        return request.getParameter(exerciseFieldName) != null;
    }

    private boolean hasTrainingDay(HttpServletRequest request, Integer rowId) throws Exception {
        var dayOfWeek = generateHTMLName("dayOfWeek", rowId);
        return request.getParameter(dayOfWeek) != null;
    }

    private String generateHTMLName(String baseName, Integer rowId, Integer exerciseId) {
        return generateHTMLName(baseName, rowId) + "_" + exerciseId;
    }

    private String generateHTMLName(String baseName, Integer rowId) {
        return baseName + "_" + rowId;
    }
}
