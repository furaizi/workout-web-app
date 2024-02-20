<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update training</title>
    <link rel="stylesheet" href="training/style.css" type="text/css">
    <script src="training/script.js"></script>
</head>
<body>

<c:set var="training" value="${requestScope.training}" />

<form action="training-web-app" method="POST">
    <a href="training/index.jsp">
        <input type="submit" value="Назад" class="back-button" />
    </a>
    <input type="hidden" name="command" value="LIST" />
</form>

<br/>
<form action="training-web-app" method="POST">

    <input type="hidden" name="command" value="UPDATE" />
    <input type="hidden" id="map" name="rowsAndExercises" value="" />
    <input type="hidden" name="trainingId" value="${training.id}" />


    <input type="text" name="trainingName" value="${training.name}" class="training-name-field" placeholder="Назва тренування"/>

    <div class="wrapper">
        <div class="row-1 column-1">День</div>
        <div class="row-1 column-2">Вправа</div>
        <div class="row-1 column-3">Кількість підходів</div>
        <div class="row-1 column-4">Кількість повторювань</div>
        <div class="row-1 column-5">Робоча вага</div>

        <div class="row-2 column-1">
            День тижня:
            <br/>
            <button class="delete-button day" onclick="deleteTrainingDay()">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 16 16">
                    <path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/>
                </svg>
            </button>
            <br/>
            <select name="dayOfWeek_2">
                <option value="Monday">Понеділок</option>
                <option value="Tuesday">Вівторок</option>
                <option value="Wednesday">Середа</option>
                <option value="Thursday">Четвер</option>
                <option value="Friday">П'ятниця</option>
                <option value="Saturday">Субота</option>
                <option value="Sunday">Неділя</option>
            </select>
        </div>
        <div class="row-2 column-2">
            <div class="exercise-field-and-delete">
                <input type="text" name="exerciseField_2_1" class="exercise-field"/>
                <button class="delete-button exercise" onclick="deleteExercise()">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 16 16">
                        <path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/>
                    </svg>
                </button>
            </div>
            <input type="button" value="Додати вправу" class="add-button exercise" onclick="addExercise()"/>
        </div>
        <div class="row-2 column-3">
            <input type="text" name="numberField_2_1" class="number-field"/>
        </div>
        <div class="row-2 column-4">
            <input type="text" name="numberField_2_1" class="number-field"/>
        </div>
        <div class="row-2 column-5">
            <input type="text" name="numberField_2_1" class="number-field"/>
        </div>

        <script>
            window.addEventListener('load', () => {
                <c:forEach var="trainingDay" items="${training.trainingDays}">
                    addTrainingDay(`${trainingDay.day.toString()}`);
                    deleteTheOnlyOneExercise();
                    <c:forEach var="exercise" items="${trainingDay.exercises}">
                        addExerciseToLastDay(`${exercise.toJSON()}`);
                    </c:forEach>
                </c:forEach>
                deleteFirstTrainingDay();
            });
        </script>

    </div>

    <input type="button" value="Додати тренувальний день" class="add-button training-day" onclick="addTrainingDay()"/>

    <input type="submit" value="Зберегти" class="add-button training" onclick="setRowsAndExercisesMap()"/>

</form>

<div id="end"></div>

</body>
</html>