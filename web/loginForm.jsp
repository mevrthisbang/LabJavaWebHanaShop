<%-- 
    Document   : loginForm
    Created on : Jan 10, 2021, 10:25:44 AM
    Author     : mevrthisbang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" >
        <script
            src="https://kit.fontawesome.com/64d58efce2.js"
            crossorigin="anonymous"
        ></script>
        <title>Login</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <div class="login-box">
        <img src="img/avatar.png" class="avatar">
        <h1>LOGIN</h1>
        <div>
            <font color="red">${requestScope.ERROR}</font>
            <form action="MainController" method="POST">
                <p>Username</p>
                <div class="textbox">
                    <input type="text" placeholder="Enter username" name="txtUsername" value="${param.txtUsername}"/>
                    <i class="fas fa-user fa-lg fa-fw" aria-hidden="true"></i>

                </div>
                <p >Password</p>
                <div class="textbox">
                    <input type="password" placeholder="Enter Password" name="txtPassword" value=""/>
                    <i class="fas fa-lock fa-lg fa-fw" aria-hidden="true"></i>

                </div>
                <input class="btn" value="Login" name="action" type="submit" />
                <a href="https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8084/SE141133_HanaShop/MainController?action=loginGmail&response_type=code
                   &client_id=939804373285-dosi8pg5biocp08k7id7d35o1iarj0fm.apps.googleusercontent.com&approval_prompt=force">Login by Gmail</a><br>
                Home Page?<a href="MainController"> Back to Home Page</a>
            </form>
        </div>


    </div>
</body>
</html>
