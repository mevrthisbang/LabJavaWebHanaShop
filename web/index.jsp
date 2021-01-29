<%-- 
    Document   : index
    Created on : Jan 4, 2021, 5:20:05 PM
    Author     : mevrthisbang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
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

        <c:if test="${requestScope.listFood!=null}">
            <c:if test="${not empty requestScope.listFood}" var="testEmpty">
                <div class="section" style="padding-top: 50px;">
                    <div class="container">
                        <div class="row">
                            <c:forEach items="${requestScope.listFood}" var="food">
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
                                            </div>
                                        </div>

                                    </form>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <c:if test="${requestScope.noOfPages>1}">
                    <div class="container">
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
                                <a href="${prevLink}" class="btn btn-large btn-primary" style="min-width: 50px;">Previous</a>
                            </c:if>
                            <c:forEach begin="1" end="${requestScope.noOfPages}" var="i">
                                <c:choose>
                                    <c:when test="${requestScope.currentPage eq i}">
                                        <a class="btn btn-large" style="background-color: #5a6268; min-width: 50px;">${i}</a>
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
                                        <a href="${pageLink}" class="btn btn-large btn-primary" style="min-width: 50px;">${i}</a>
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
                                <a href="${nextLink}" class="btn btn-large btn-primary" style="min-width: 50px;">Next</a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </c:if>
            <c:if test="${!testEmpty}">
                <div class="container" style="margin-top: 50px;">
                    <div class="center">
                        <h1>Not found any food!</h1>
                    </div>
                </div>
            </c:if>
        </c:if>

    </body>
</html>
