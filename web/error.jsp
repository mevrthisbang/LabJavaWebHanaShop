<%-- 
    Document   : error
    Created on : Jan 9, 2021, 10:27:02 AM
    Author     : mevrthisbang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous"/>
        <link href="css/style_1.css" rel="stylesheet"/>
    </head>
    <body>
        <div class="header_bottom" style="background-color: #F0EEEE;">
            <div class="container">
                <div class="row" >
                    <div class="col-lg-3 col-md-12 col-sm-12 col-xs-12">
                        <!-- logo start -->
                        <div class="logo"> <a href="MainController"><img src="img/logo.png" alt="logo" /></a> </div>
                        <!-- logo end -->
                    </div>
                    <div class="col-lg-9 col-md-12 col-sm-12 col-xs-12">
                        <!-- menu start -->
                        <div class="menu_side">
                            <div id="navbar_menu">
                                <ul class="first-ul">
                                    <li> <a class="active" href="MainController">Home</a>
                                    </li>
                            </div>
                            <!-- menu end -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container" style="margin-top: 50px;">
            <div class="center">
                <h1>Error</h1>
            </div>
            <div class="center">
                <p>
                    <font color="red">
                    ${requestScope.ERROR}
                    </font>
                </p>
            </div>
            <div class="center">
                <a href="MainController" class="btn btn-primary">Back to homepage</a>
            </div>
        </div>
    </body>
</html>
