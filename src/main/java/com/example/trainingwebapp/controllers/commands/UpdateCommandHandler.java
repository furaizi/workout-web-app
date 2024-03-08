package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.services.TrainingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateCommandHandler extends CommandHandler {

    public UpdateCommandHandler(TrainingService service) {
        super(service);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        int trainingId = Integer.parseInt( request.getParameter("trainingId") );

        if (trainingService.userHasTraining(userId, trainingId)) {
            Training training = TrainingParser.parse(request);
            training.setId(trainingId);
            trainingService.updateFullTraining(training);
        }

        new ListCommandHandler(trainingService).handle(request, response);
    }
}
