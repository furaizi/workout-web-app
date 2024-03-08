package com.example.trainingwebapp.controllers.commands;

import com.example.trainingwebapp.models.dto.Training;
import com.example.trainingwebapp.models.services.TrainingService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ListCommandHandler extends CommandHandler {

    public ListCommandHandler(TrainingService service) {
        super(service);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = getUserId(request);
        List<Training> trainings = trainingService.getFullTrainings(userId);
        request.setAttribute("trainings", trainings);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/training/index.jsp");
        dispatcher.forward(request, response);
    }
}
