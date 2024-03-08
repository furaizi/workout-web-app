<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.trainingwebapp.models.dto.Training" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>The plan of training</title>
    <link rel="stylesheet" href="training/style.css" type="text/css">
</head>
<body>


<c:set var="trainings" value="${requestScope.trainings}" />
<c:set var="training" value="${requestScope.training}" />
<c:set var="rowId" value="${requestScope.rowId}" />


    <a href="index.jsp">
        <input type="button" value="На головну" class="btn back-button"/>
    </a>

    <div class="button-container">
        <div>
            <a href="training/add-training.jsp">
                <input type="button" value="Додати тренування" class="add-button"/>
            </a>
        </div>

        <c:forEach var="tempTraining" items="${trainings}">
            <div>
                <form action="workout-web-app" method="POST">
                    <input type="submit" value="${tempTraining.name}" class="training-button"/>
                    <input type="hidden" name="command" value="LOAD"/>
                    <input type="hidden" name="trainingId" value="${tempTraining.id}"/>
                </form>
                <form action="workout-web-app" method="POST">
                    <button class="edit-button training">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil" viewBox="0 0 16 16">
                            <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325"/>
                        </svg>
                    </button>
                    <input type="hidden" name="command" value="LOAD_TO_UPDATE"/>
                    <input type="hidden" name="trainingId" value="${tempTraining.id}"/>
                </form>
                <form action="workout-web-app" method="POST">
                    <button class="delete-button training"
                            onclick="if (!(confirm('Ви точно хочете видалити це тренування?')))
                                        return false;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 16 16">
                            <path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/>
                        </svg>
                    </button>
                    <input type="hidden" name="command" value="DELETE"/>
                    <input type="hidden" name="trainingId" value="${tempTraining.id}"/>
                </form>
            </div>
        </c:forEach>

    </div>

    <br/>
    <br/>
        <c:if test="${training != null}">
            <p class="training-name-field">${training.name}</p>
        </c:if>
    <br/>
    <br/>

    <c:if test="${training != null}">
        <div class="wrapper">
            <div class="row-1 column-1">День</div>
            <div class="row-1 column-2">Вправа</div>
            <div class="row-1 column-3">Кількість підходів</div>
            <div class="row-1 column-4">Кількість повторювань</div>
            <div class="row-1 column-5">Робоча вага</div>

            <c:forEach var="trainingDay" items="${training.trainingDays}">

                <div class="row-${rowId} column-1">${trainingDay.day.toString("uk")}</div>
                <div class="row-${rowId} column-2">
                    <c:forEach var="exercise" items="${trainingDay.exercises}">
                        ${exercise.name} <br/>
                    </c:forEach>
                </div>
                <div class="row-${rowId} column-3">
                    <c:forEach var="exercise" items="${trainingDay.exercises}">
                        <div>${exercise.numberOfSets}</div>
                    </c:forEach>
                </div>
                <div class="row-${rowId} column-4">
                    <c:forEach var="exercise" items="${trainingDay.exercises}">
                        <div>${exercise.numberOfReps}</div>
                    </c:forEach>
                </div>
                <div class="row-${rowId} column-5">
                    <c:forEach var="exercise" items="${trainingDay.exercises}">
                        <div>${exercise.workWeight}</div>
                    </c:forEach>
                </div>
                <c:set var="rowId" value="${rowId + 1}" />

            </c:forEach>

            </c:if>

        </div>
      <div id="end"></div>

</body>
</html>
