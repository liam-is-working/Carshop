<%-- 
    Document   : signup
    Created on : Aug 11, 2021, 8:29:32 PM
    Author     : ACER
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign up</title>
    </head>
    <body>
        <h1>Sign up new account</h1>
        <c:forEach items="${errorList}" var="error">
            <c:out value="${error.getMessage()}"/>
            <br>
        </c:forEach>
        <form action="signup" method="POST">
            <input type="hidden" name="action" value="signup" />
            userID<input type="text" name="userID" value="${param['userID']}" />
            password<input type="password" name="password" value="" />
            FullName<input type="text" name="fullName" value="${param['fullName']}"  />
            <input type="submit" name="action" value="signup" />
        </form>
        <a href="<c:url value="login"/>">Login</a>
    </body>
</html>
