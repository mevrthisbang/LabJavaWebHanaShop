<%-- 
    Document   : cart
    Created on : Jan 10, 2021, 9:59:58 AM
    Author     : mevrthisbang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:if test="${sessionScope.USER!=null&& sessionScope.USER.role eq 'user'}" var="testRole">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Cart</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous"/>
            <link href="css/style_1.css" rel="stylesheet"/>
        </head>
        <style>
            #formSubmit{
                display: none;
            }
        </style>
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
                    <h1>Your Cart</h1>
                </div>
                <div class="row" style="margin-top: 50px;">
                    <c:if test="${sessionScope.CART!=null}" var="testNull">
                        <c:if test="${not empty sessionScope.CART.cart}" var="testEmpty">
                            <div class="center">
                                <form action="MainController" method="POST">
                                    <table border="1" class="table table-bordered" style="width: 800px; margin-right: 50px; height: 310px;">
                                        <thead>
                                            <tr>
                                                <th scope="col">Food name</th>
                                                <th scope="col">Amount</th>
                                                <th scope="col">Price</th>
                                                <th scope="col">Total</th>
                                                <th scope="col"></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${sessionScope.CART.cart.values()}" var="food">
                                                <tr>
                                                    <td>
                                                        ${food.name}
                                                    </td>
                                                    <td>
                                                        <input type="number" value="${food.quantity}" name="txtQuantity" style="width: 60px;"/><br>
                                                        <font color="red">${food.description}</font>
                                                        <input type="hidden" value="${food.id}" name="txtID"/>
                                                    </td>
                                                    <td>
                                                        ${food.price}$
                                                    </td>
                                                    <td>
                                                        <div id="total">
                                                            <fmt:formatNumber type="number" maxFractionDigits="3" value="${food.quantity*food.price}"/>$
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" name="chkRemove" value="${food.id}"/>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <tr>
                                                <td></td>
                                                <td>
                                                    <div class="center">
                                                        <input type="submit" name="action" value="Update Cart" class="btn btn-primary"/>
                                                    </div>
                                                </td>
                                                <td></td>
                                                <td>
                                                    <fmt:formatNumber type="number" maxFractionDigits="3" value="${sessionScope.CART.total}"/>$
                                                </td>
                                                <td>
                                                    <input type="submit" value="Remove" name="action" class="btn btn-primary" onclick="return confirm({'Are you sure you want to remove all selected food from cart?'}; )"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </form>
                                <div class="right">
                                    <p>Payment Method </p><select id="selectedForm" onchange="selectedPayment()">
                                        <option value="Cash">Cash</option>
                                        <option value="PayPal">PayPal</option>
                                    </select><br>
                                    <form action="MainController" method="POST" id="formInput">
                                        <p>Fullname:</p> 
                                        <font color="red">${requestScope.INVALID.fullnameError}</font>
                                        <input type="text" value="${sessionScope.USER.fullname}" name="txtFullname" class="form-field"/><br>
                                        <p>Phone:</p>
                                        <font color="red">${requestScope.INVALID.phoneError}</font>
                                        <input type="text" value="${sessionScope.USER.phone}" name="txtPhone"/><br>
                                        <p>Address:</p> 
                                        <font color="red">${requestScope.INVALID.addressError}</font>
                                        <input type="text" value="${sessionScope.USER.address}" name="txtAddress"/><br>
                                        <input type="hidden" name="paymentMethod" value="Cash"/>
                                        <div class="center"><input type="submit" value="Confirm Order" name="action" class="btn btn-primary" style="margin-top: 20px;"/></div>
                                    </form>
                                    <form action="MainController" method="POST" id="formSubmit">
                                        <input type="hidden" name="paymentMethod" value="PayPal"/>
                                        <div class="center"><input type="submit" value="Confirm Order" name="action" class="btn btn-primary" style="margin-top: 20px;"/></div>
                                    </form>
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <c:if test="${!testEmpty}">
                        <div class="center">
                            <p>There's nothing in cart.</p>
                        </div>
                        <div class="center">
                            <a href="MainController" class="btn btn-primary">Back to shopping?</a>
                        </div>
                    </c:if>
                </c:if>
                <c:if test="${!testNull}">
                    <div class="center">
                        <p>There's nothing in cart.</p>
                    </div>
                    <div class="center">
                        <a href="MainController" class="btn btn-primary">Back to shopping?</a>
                    </div>
                </c:if>
            </div>

        </body>
        <script>
            function selectedPayment(){
            var x = document.getElementById("selectedForm").value;
            if (x === "Cash"){
            document.getElementById("formInput").style.display = "block";
            document.getElementById("formSubmit").style.display = "none";
            } else{
            document.getElementById("formInput").style.display = "none";
            document.getElementById("formSubmit").style.display = "block";
            }
            }
        </script>
    </html>
</c:if>
<c:if test="${!testRole}">
    <c:set var="ERROR" value="You do not have permission to access this" scope="request"/>
    <%@include file="error.jsp"%>
</c:if>

