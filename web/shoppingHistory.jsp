<%-- 
    Document   : shoppingHistory
    Created on : Jan 14, 2021, 8:13:39 PM
    Author     : mevrthisbang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:if test="${sessionScope.USER!=null&& sessionScope.USER.role eq 'user'}" var="testRole">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Shopping History</title>
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
                                        <li><a href="cart.jsp">Cart</a></li>
                                            <c:url var="historyLink" value="MainController">
                                                <c:param name="action" value="shoppingHistory"/>
                                            </c:url>
                                        <li><a href="${historyLink}">Order History</a></li>
                                        <c:url var="suggestLink" value="MainController">
                                                <c:param name="action" value="suggest"/>
                                        </c:url>
                                        <li><a href="${suggestLink}">Suggest Food</a></li>
                                            <c:url var="logoutLink" value="MainController">
                                                <c:param name="action" value="Logout"/>
                                            </c:url>
                                        <li><a href="${logoutLink}">Logout</a></li>
                                </div>
                                <!-- menu end -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container" style="margin-top: 50px;">
                <div class="center">
                    <h1>Your Shopping History</h1>
                </div>
            </div>
            <div class="container" style="padding-top: 50px;">
                <div class="center">
                    <form action="MainController" method="POST">
                        Search: <input name="txtName" type="text" value="${param.txtName}"/>
                        Date: <input type="date" name="datePicker" value="${param.datePicker}"/>
                        <input type="submit" value="Search History" name="action" class="btn btn-primary"/>
                    </form>
                </div>
            </div>
            <div class="container" style="margin-top: 50px;">
                <div class="row" style="margin-top: 50px;">
                    <c:if test="${requestScope.SHOPPINGHISTORY!=null}" var="testNull">
                        <c:if test="${not empty requestScope.SHOPPINGHISTORY}" var="testEmpty">
                            <c:forEach items="${requestScope.SHOPPINGHISTORY}" var="map">
                                <div class="center">
                                    <table border="1" class="table table-bordered" style="width: 1000px; margin-right: 50px;">
                                        <tr>
                                            <th style="width: 200px;">Buyer's Detail</th>
                                            <td>${map.key.buyerName} - ${map.key.address} - ${map.key.phone}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="width: 200px;">List Order Product</th>
                                            <td>
                                                <c:forEach items="${map.value}" var="food" varStatus="counter">

                                                    ${counter.count}. ${food.name} - Quantity: ${food.quantity} - Price: ${food.price}$<br>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="width: 200px;">Payment Method</th>
                                            <td>${map.key.paymentMethod}</td>
                                        </tr>
                                        <tr>
                                            <th style="width: 200px;">Buy Date</th>
                                            <td>${map.key.buyDate}</td>
                                        </tr>
                                        <tr>
                                            <th style="width: 200px;">Status</th>
                                            <td>${map.key.status}</td>
                                        </tr>
                                    </table>
                                </div>
                            </c:forEach>
                        </c:if>
                        <c:if test="${!testEmpty}">
                            <div class="center">
                                <p>Do not found your order.</p>
                            </div>
                            <div class="center">
                                <a href="MainController" class="btn btn-primary">Back to shopping?</a>
                            </div>
                        </c:if>
                    </c:if>
                    <c:if test="${!testNull}">
                        <div class="center">
                            <p>Do not found your order</p>
                        </div>
                        <div class="center">
                            <a href="MainController" class="btn btn-primary">Back to shopping?</a>
                        </div>
                    </c:if>
                </div>
            </div>
        </body>
    </html>
</c:if>
<c:if test="${!testRole}">
    <c:set var="ERROR" value="You do not have permission to access this" scope="request"/>
    <%@include file="error.jsp"%>
</c:if>
