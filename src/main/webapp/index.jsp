<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Workout web app</title>
        <link href="style.css" type="text/css" rel="stylesheet">
    </head>
    <body>

        <h1>Training web app</h1>

        <div class="header-box">
            <div class="menu-box">
                <form action="training-web-app" method="POST">
                    <div class="menu-element">
                        <input type="submit" value="План тренувань" class="menu-element" />
                        <input type="hidden" name="command" value="LIST"/>
                    </div>
                </form>
                <div class="menu-element">Кнопка</div>
                <div class="menu-element">Кнопка</div>
                <div class="menu-element">Кнопка</div>
            </div>
            <div class="profile-box">
                <a href="profile/index.jsp">
                    <div class="profile">Мій профіль</div>
                </a>
            </div>
        </div>
    </body>
</html>