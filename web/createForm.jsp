<%-- 
    Document   : createFrom
    Created on : Jan 9, 2021, 9:09:06 AM
    Author     : mevrthisbang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:if test="${sessionScope.USER!=null&& sessionScope.USER.role eq 'admin'}" var="testRole">
    <link class="jsbin" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous"/>
    <link href="css/style_1.css" rel="stylesheet"/>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Create Food Form</title>
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
                                        <li><a href="createForm.jsp">Create new Food</a></li>
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
                    <h1>Create Food Form</h1>
                </div>
                <form action="MainController?action=Create" method="POST" enctype="multipart/form-data">
                    <div class="row">

                        <div class="form-row">
                            <div class="form-group col-md-4">
                            </div>
                            <div class="center">
                                <div class="form-group col-md-4">
                                    <label for="inputName">Food name</label>
                                    <input type="text" class="form-control" id="inputName" value="${requestScope.FOOD.name}" name="txtName"/>
                                    <font color="red">
                                    ${requestScope.INVALID.nameError}
                                    </font>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-4">
                            </div>
                            <div class="center">
                                <div class="form-group col-md-4">
                                    <label for="inputPrice">Price</label>
                                    <input type="text" class="form-control" id="inputPrice" value="${requestScope.FOOD.price}" name="txtPrice"/>
                                    <font color="red">
                                    ${requestScope.INVALID.priceError}
                                    </font>
                                </div>
                            </div>

                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-4">
                            </div>
                            <div class="center">
                                <div class="form-group col-md-4">
                                    <label for="inputImg">Image</label>
                                    <input type="file" onchange="readURL(this);" accept="image/*" name="imgFood" class="form-control-file" id="inputImg" value="${requestScope.FOOD.price}" name="txtPrice"/>

                                    <img id="blah" src="img/logo.png" width="150px" height="200px"/><br>
                                    <font color="red">
                                    ${requestScope.INVALID.imgError}
                                    </font>
                                </div>

                            </div>

                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-4">
                            </div>
                            <div class="center">
                                <div class="form-group col-md-4">
                                    <label for="inputDescription">Description</label>
                                    <input type="text" class="form-control" id="inputDescription" value="${requestScope.FOOD.description}" name="txtDescription"/>
                                    <font color="red">
                                    ${requestScope.INVALID.descriptionError}
                                    </font>
                                </div>
                            </div>

                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-4">
                            </div>
                            <div class="center">
                                <div class="form-group col-md-4">
                                    <label for="inputQuantity">Quantity</label>
                                    <input type="text" class="form-control" id="inputQuantity" value="${requestScope.FOOD.quantity}" name="txtQuantity"/>
                                    <font color="red">
                                    ${requestScope.INVALID.quantityError}
                                    </font>
                                </div>
                            </div>

                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-4">
                            </div>
                            <div class="center">
                                <div class="form-group col-md-4">
                                    <label for="inputCategory">Category</label><br>
                                    <select name="cboCategory" id="inputCategory">
                                        <c:if test="${applicationScope.listCategories!=null}">
                                            <c:forEach items="${applicationScope.listCategories}" var="category">
                                                <option value="${category.id}" <c:if test="${requestScope.FOOD.category eq category.id}">selected="true"</c:if>>${category.name}</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                    <font color="red">
                                    </font>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="container">
                        <div class="row">
                            <div class="col text-center">
                                <input type="submit" class="btn btn-large btn-primary" value="Create" name="action" style="margin-top: 30px;"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </body>
    </html>
    <script>
        function readURL(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    $('#blah')
                            .attr('src', e.target.result)
                            .width(150)
                            .height(200);
                };

                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</c:if>
<c:if test="${!testRole}">
    <c:set var="ERROR" value="You do not have permission to access this" scope="request"/>
    <%@include file="error.jsp"%>
</c:if>
