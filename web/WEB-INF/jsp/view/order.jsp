<%-- 
    Document   : order
    Created on : Aug 16, 2021, 9:16:39 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order List</title>
    </head>
    <body>
        <c:if test="${!empty orderList}">
            <table border="1">
                <thead>
                    <tr>
                        <c:if test="${user.roleID==2}">
                            <th>UserID</th>
                            </c:if>
                        <th>OrderID</th>
                        <th>Address</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orderList}" var="order">
                        <tr>
                            <c:if test="${user.roleID==2}">
                            <th>${order.userID}</th>
                            </c:if>
                            <td>
                                <a href="<c:url value="order?action=orderDetail"><c:param name="orderID" value="${order.orderID}"/></c:url>">${order.orderID}</a>
                                </td>
                                <td>${order.address}</td>
                            <td>${order.orderDate}</td>
                        </tr>
                    </c:forEach>

                </tbody>
            </table>
        </c:if>
        <c:if test="${empty orderList}">
            You have not made any order
        </c:if>
    </body>
</html>
