<%-- 
    Document   : cart
    Created on : Aug 14, 2021, 9:28:16 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cart</title>
    </head>
    <body>
        <c:if test="${!empty cart}">
            <table border="1">
                <thead>
                    <tr>
                        <th>ProductID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Add 1</th>
                        <th>Remove 1</th>
                        <th>Delete all</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${cart}" var="record">

                        <tr>
                            <td>${record.key.productID}</td>
                            <td>${record.key.productName}</td>
                            <td>${record.key.price}</td>
                            <td>${record.value}</td>
                            <td>
                                <a href="<c:url value="cart?action=add">
                                       <c:param name="proID" value="${record.key.productID}"/>
                                       <c:param name="quantity" value="1"/>
                                   </c:url>">
                                    <button>Up</button>
                                </a>
                            </td>
                            <td>
                                <a href="<c:url value="cart?action=delete">
                                       <c:param name="proID" value="${record.key.productID}"/>
                                       <c:param name="quantity" value="1"/>
                                   </c:url>">
                                    <button>Down</button>
                                </a>
                            </td>
                            <td>
                                <a href="<c:url value="cart?action=deleteProduct">
                                       <c:param name="proID" value="${record.key.productID}"/>
                                   </c:url>">
                                    <button>Remove</button>
                                </a>
                            </td>
                        </tr>

                    </c:forEach>
                </tbody>
            </table>
            <h3>Total: <c:out value="${cart.getTotalPrice()}"/></h3>
            <a href="<c:url value="cart?action=clearAll"/>">
                <button>Clear all</button>
            </a>
                <form action="order" method="POST">
                <input type="text" name="address" value="${param["address"]}" />
                <input type="submit" name="action" value="submitOrder" />
            </form>
        </c:if>
        <c:if test="${empty cart}">
            You have not put any product in cart
            <br>
        </c:if>
        <c:out value="${addError}"/>
        <c:out value="${deleteError}"/>
        <c:out value="${checkOutError}"/>
        <c:out value="${checkOutSuccess}"/>



    </body>
</html>
