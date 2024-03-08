package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.dto.DayOfWeek;
import com.example.trainingwebapp.models.dto.Exercise;
import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.dto.TrainingDay;
import com.example.trainingwebapp.models.services.TrainingService;
import org.json.JSONArray;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class CommandHandler {

    protected static final Integer INITIAL_ROW_ID = 2;
    protected static final int SETS_INDEX = 0;
    protected static final int REPS_INDEX = 1;
    protected static final int WORK_WEIGHT_INDEX = 2;

    protected TrainingService trainingService;
    abstract public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public CommandHandler(TrainingService trainingService) {
        this.trainingService = trainingService;
    }


    protected int getUserId(HttpServletRequest request) {
        Cookie userIdCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("userId"))
                .findFirst()
                .orElseThrow();
        int userId = Integer.parseInt( userIdCookie.getValue() );

        return userId;
    }
}
