package com.example.trainingwebapp.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;


@WebServlet("/set-cookie")
public class SetCookieServlet extends HttpServlet {

    private static final int YEAR_IN_SECONDS = 60 * 60 * 24 * 365;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer lastId = (Integer) request.getAttribute("lastId");
        Integer newUserId = lastId + 1;
        Cookie userIdcookie = new Cookie("userId", newUserId.toString());
        userIdcookie.setMaxAge(YEAR_IN_SECONDS);

        Cookie[] oldCookies = request.getCookies();
        Cookie[] updatedCookies = Arrays.copyOf(oldCookies, oldCookies.length + 1, oldCookies.getClass());
        updatedCookies[oldCookies.length] = userIdcookie;

        oldCookies = updatedCookies;
        response.addCookie(userIdcookie);
        response.sendRedirect("/workout-web-app");
    }
}
