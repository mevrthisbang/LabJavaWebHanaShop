<%-- 
    Document   : admin
    Created on : Jan 9, 2021, 9:04:50 AM
    Author     : mevrthisbang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:if test="${sessionScope.USER!=null&& sessionScope.USER.role eq 'admin'}" var="testRole">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Admin Hompage</title>
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
            <div class="center"><font color="red"><font color="red">Welcome admin, ${sessionScope.USER.fullname}</font></font></div>

            <div class="container" style="padding-top: 50px;">
                <div class="center">
                    <form action="MainController" method="POST">
                        Search: <input name="txtSearch" type="text" value="${param.txtSearch}"/>
                        Range of Money: <input name="txtFrom" type="number" step="any" value="${param.txtFrom}" style="width:60px;"/>-<input name="txtTo" type="number" step="any" value="${param.txtTo}" style="width:60px;"/>
                        <select name="cboCategory">
                            <option value="">-Category-</option>
                            <c:if test="${applicationScope.listCategories!=null}">
                                <c:forEach items="${applicationScope.listCategories}" var="category">
                                    <option value="${category.id}" <c:if test="${param.cboCategory eq category.id}">selected="true"</c:if>>${category.name}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                        <input type="submit" value="Search" name="action" class="btn btn-primary"/>
                    </form>
                </div>
            </div>
            <div class="container" style="margin-top: 50px;">
                <c:if test="${requestScope.listFood!=null}">
                    <c:if test="${not empty requestScope.listFood}" var="testEmpty">
                        <div class="center">
                            <form action="MainController" method="POST">
                                <table border="1" class="table table-bordered" style="width: 1000px; margin-right: 50px; height: 400px;">
                                    <thead>
                                        <tr>
                                            <th scope="col">Name</th>
                                            <th scope="col">Image</th>
                                            <th scope="col">Price</th>
                                            <th scope="col">Quantity</th>
                                            <th scope="col"></th>
                                            <th scope="col"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${requestScope.listFood}" var="food">

                                            <tr>
                                                <td>${food.name}</td>
                                                <td>
                                                    <img alt="Food Image" src="${food.img}" width="100px" height="100px"/>
                                                </td>
                                                <td>
                                                    ${food.price}$
                                                </td>
                                                <td>${food.quantity}</td>
                                                <td>
                                                    <input type="checkbox" id="chkDelete" name="chkCheckDelete" value="${food.id}"/>
                                                </td>
                                                <td>
                                                    <div class="center">
                                                        <c:url var="editLink" value="MainController">
                                                            <c:param name="action" value="Edit"/>
                                                            <c:param name="id" value="${food.id}"/>
                                                            <c:param name="page" value="${requestScope.currentPage}"/>
                                                            <c:param name="txtSearch" value="${param.txtSearch}"/>
                                                            <c:param name="txtFrom" value="${param.txtFrom}"/>
                                                            <c:param name="txtTo" value="${param.txtTo}"/>
                                                            <c:param name="cboCategory" value="${param.cboCategory}"/>
                                                        </c:url>
                                                        <a href="${editLink}" class="btn" style="background-color: #000">Edit</a>
                                                    </div>

                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr>
                                            <td></td>
                                            <td></td>
                                            <td>
                                            </td>
                                            <td></td>
                                            <td>
                                                <div class="center">
                                                    <input type="submit" value="Delete" name="action" class="btn btn-large btn-primary" onclick="return confirm('Are you sure to delete all selected items?')"/>
                                                </div>
                                                <input type="hidden" name="page" value="${param.page}"/>
                                                <input type="hidden" name="txtSearch" value="${param.txtSearch}"/>
                                                <input type="hidden" name="txtFrom" value="${param.txtFrom}"/>
                                                <input type="hidden" name="txtTo" value="${param.txtTo}"/>
                                                <input type="hidden" name="cboCategory" value="${param.cboCategory}"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                        <c:if test="${requestScope.noOfPages>1}">
                            <div class="center">
                                <c:if test="${requestScope.currentPage>1}">
                                    <c:url var="prevLink" value="MainController">
                                        <c:param name="action" value="Search"/>
                                        <c:param name="page" value="${requestScope.currentPage-1}"/>
                                        <c:param name="txtSearch" value="${param.txtSearch}"/>
                                        <c:param name="txtFrom" value="${param.txtFrom}"/>
                                        <c:param name="txtTo" value="${param.txtTo}"/>
                                        <c:param name="cboCategory" value="${param.cboCategory}"/>
                                    </c:url>
                                    <a href="${prevLink}" class="btn btn-primary" style="min-width: 50px">Previous</a>
                                </c:if>

                                <c:forEach begin="1" end="${requestScope.noOfPages}" var="i">
                                    <c:choose>
                                        <c:when test="${requestScope.currentPage eq i}">
                                            <a class="btn btn-primary" style="background-color: #707070; min-width: 50px;">${i}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <c:url var="pageLink" value="MainController">
                                                <c:param name="action" value="Search"/>
                                                <c:param name="page" value="${i}"/>
                                                <c:param name="txtSearch" value="${param.txtSearch}"/>
                                                <c:param name="txtFrom" value="${param.txtFrom}"/>
                                                <c:param name="txtTo" value="${param.txtTo}"/>
                                                <c:param name="cboCategory" value="${param.cboCategory}"/>
                                            </c:url>
                                            <a href="${pageLink}" class="btn btn-primary" style="min-width: 50px">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>


                                <c:if test="${requestScope.currentPage<requestScope.noOfPages}">
                                    <c:url var="nextLink" value="MainController">
                                        <c:param name="action" value="Search"/>
                                        <c:param name="page" value="${requestScope.currentPage+1}"/>
                                        <c:param name="txtSearch" value="${param.txtSearch}"/>
                                        <c:param name="txtFrom" value="${param.txtFrom}"/>
                                        <c:param name="txtTo" value="${param.txtTo}"/>
                                        <c:param name="cboCategory" value="${param.cboCategory}"/>
                                    </c:url>
                                    <a href="${nextLink}" class="btn btn-primary" style="min-width: 50px">Next</a>
                                </c:if>
                            </div>
                        </c:if>
                    </c:if>
                    <c:if test="${!testEmpty}">
                        <h2>Not found any food!!!</h2>
                    </c:if>
                </c:if>
            </div>
        </body>
    </html>
</c:if>
<c:if test="${!testRole}">
    <c:set var="ERROR" value="You do not have permission to access this" scope="request"/>
    <%@include file="error.jsp"%>
</c:if>

