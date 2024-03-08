package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.services.TrainingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoadCommandHandler extends CommandHandler {


    public LoadCommandHandler(TrainingService service) {
        super(service);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        loadTraining(request);
        new ListCommandHandler(trainingService).handle(request, response);
    }

    private void loadTraining(HttpServletRequest request) throws Exception {
        int userId = getUserId(request);
        int trainingId = Integer.parseInt( request.getParameter("trainingId") );

        if (trainingService.userHasTraining(userId, trainingId)) {
            Training training = trainingService.getFullTraining(trainingId);
            request.setAttribute("rowId", INITIAL_ROW_ID);
            request.setAttribute("training", training);
        }
    }
}
