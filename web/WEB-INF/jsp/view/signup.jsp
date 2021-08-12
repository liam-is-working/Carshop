<%-- 
    Document   : signup
    Created on : Aug 11, 2021, 8:29:32 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign up</title>
    </head>
    <body>
        <h1>Sign up new account</h1>
        <form action="signup">
            <input type="hidden" name="action" value="signup" />
            userID<input type="text" name="userID" value="" />
            password<input type="password" name="password" value="" />
            FullName<input type="text" name="FullName" value="" />
            <input type="submit" value="Sign up" />
        </form>
    </body>
</html>
