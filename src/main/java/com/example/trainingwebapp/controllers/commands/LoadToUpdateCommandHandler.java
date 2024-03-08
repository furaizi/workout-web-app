package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.services.TrainingService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoadToUpdateCommandHandler extends CommandHandler {


    public LoadToUpdateCommandHandler(TrainingService service) {
        super(service);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        loadTraining(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/training/update-training.jsp");
        dispatcher.forward(request, response);
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
