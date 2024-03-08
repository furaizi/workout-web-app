package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.services.TrainingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCommandHandler extends CommandHandler {

    public AddCommandHandler(TrainingService service) {
        super(service);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Training training = TrainingParser.parse(request);
        int userId = getUserId(request);
        trainingService.addFullTraining(training, userId);

        new ListCommandHandler(trainingService).handle(request, response);
    }
}
