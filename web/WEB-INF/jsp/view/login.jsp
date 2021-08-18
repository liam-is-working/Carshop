<%-- 
    Document   : login
    Created on : Aug 11, 2021, 8:29:20 PM
    Author     : ACER
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Log in</title>
    </head>
    <body>
        <h1>Login</h1>
        <form action="login" method="POST">
            UserID<input type="text" name="userID" value="" />
            Password<input type="password" name="password" value="" />
            <input type="submit" name="action" value="login" />
        </form>
        <a href="<c:url value="signup"/>">Sign up</a>
    </body>
</html>
