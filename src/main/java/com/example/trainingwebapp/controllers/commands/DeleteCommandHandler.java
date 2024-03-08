package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.services.TrainingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteCommandHandler extends CommandHandler {

    public DeleteCommandHandler(TrainingService service) {
        super(service);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        int trainingId = Integer.parseInt( request.getParameter("trainingId") );

        if (trainingService.userHasTraining(userId, trainingId))
            trainingService.deleteFullTraining(trainingId);

        new ListCommandHandler(trainingService).handle(request, response);
    }
}
