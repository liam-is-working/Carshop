<%-- 
    Document   : orderDetail
    Created on : Aug 16, 2021, 9:23:31 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Detail of order ${orderID}</title>
    </head>
    <body>
        <c:if test="${!empty productList}">
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>CategoryID</th>
                    </tr>
                </thead>

                <tbody>  
                    <c:forEach var="pro" items="${productList}">
                        <tr>
                            <td>${pro.productID}</td>
                            <td>${pro.productName}</td>
                            <td>${pro.price}</td>
                            <td>${pro.quantity}</td>
                            <td>${pro.categoryID}</td>
                        </tr>
                    </c:forEach>   
                </tbody>
            </table>
            <h3>Total: <c:out value="${totalPrice}"/></h3>
        </c:if>
    </body>
</html>
