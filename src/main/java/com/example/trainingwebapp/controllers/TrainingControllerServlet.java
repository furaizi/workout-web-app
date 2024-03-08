package com.example.trainingwebapp.controllers;

import com.example.trainingwebapp.controllers.commands.*;
import com.example.trainingwebapp.models.dao.TrainingDAOImpl;
import com.example.trainingwebapp.models.services.*;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

import static com.example.trainingwebapp.controllers.commands.Command.*;

@WebServlet("/workout-web-app")
public class TrainingControllerServlet extends HttpServlet {

    private TrainingService trainingService;
    private HashMap<Command, CommandHandler> handlers;

    @Resource(name="jdbc/training_web_app")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            trainingService = new TrainingServiceImpl(new TrainingDAOImpl(dataSource));
            handlers = new HashMap<>() {{
                put(LIST, new ListCommandHandler(trainingService));
                put(ADD, new AddCommandHandler(trainingService));
                put(LOAD, new LoadCommandHandler(trainingService));
                put(DELETE, new DeleteCommandHandler(trainingService));
                put(UPDATE, new UpdateCommandHandler(trainingService));
                put(LOAD_TO_UPDATE, new LoadToUpdateCommandHandler(trainingService));
            }};
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

            if (!hasUserId(request))
                setCookie(request, response);

            var command = Command.get( request.getParameter("command") );
            handlers.get(command)
                    .handle(request, response);
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void setCookie(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer lastId = trainingService.getLastId();
        request.setAttribute("lastId", lastId);
        trainingService.addUser();

        RequestDispatcher dispatcher = request.getRequestDispatcher("/set-cookie");
        dispatcher.forward(request, response);
    }

    private boolean hasUserId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return false;

        return Arrays.stream(cookies)
                .anyMatch( cookie -> cookie.getName().equals("userId") );
    }

}
