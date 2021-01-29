<%-- 
    Document   : suggestPageGuest
    Created on : Jan 18, 2021, 6:47:39 PM
    Author     : mevrthisbang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:if test="${sessionScope.USER!=null&&sessionScope.USER.role eq 'admin'}" var="testRole">
    <c:set var="ERROR" value="You do not permission to do this." scope="request"/>
    <%@include file="error.jsp" %>
</c:if>
<c:if test="${!testRole}">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Suggest Food</title>
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
                                            <c:url var="suggestLink" value="MainController">
                                                <c:param name="action" value="suggest"/>
                                            </c:url>
                                        <li><a href="${suggestLink}">Suggest Food</a></li>
                                        <li><a href="loginForm.jsp">Login</a></li>
                                </div>
                                <!-- menu end -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${requestScope.listFoodPurchased!=null}">
                <c:if test="${not empty requestScope.listFoodPurchased}" var="testEmpty">
                    <div class="section" style="padding-top: 50px;">
                        <div class="container">
                            <div class="row">
                                <h1>Purchased Many Times</h1>
                            </div>
                            <div class="row">
                                <c:forEach items="${requestScope.listFoodPurchased}" var="food">
                                    <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12 margin_bottom_30_all">
                                        <form action="MainController" method="POST">

                                            <div class="product_list" style="background-color: #F0EEEE; min-height: 350px;">
                                                <div class="product_img"> <img class="img-responsive" src="${food.img}" alt="" style="width: 500px; height: 200px;"> </div>
                                                <div class="product_detail_btm">
                                                    <div class="center">
                                                        <h4>${food.name}</h4>
                                                    </div>
                                                    <div class="product_price">
                                                        <p><span class="new_price">${food.price}$</span></p>
                                                    </div>
                                                </div>
                                                <div class="center">
                                                    <input type="submit" name="action" value="Add to Cart" class="btn btn-primary" style="margin-top: 20px;"/>
                                                    <input type="hidden" name="tagPage" value="OfSuggest"/>
                                                    <input type="hidden" value="${food.id}" name="txtID"/>
                                                    <input type="hidden" value="${param.page}" name="page"/>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:if>
        </body>
    </html>
</c:if>
