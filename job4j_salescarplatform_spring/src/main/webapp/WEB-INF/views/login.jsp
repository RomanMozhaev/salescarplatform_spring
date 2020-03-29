<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Log In</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body>

<h1 style="text-align:center;">Login</h1>
<div>
    <c:if test='${param.containsKey("error")}'>
        <p style="text-align:center;"> Unable to login. Check your username and password.</p>
    </c:if>
</div>
<p style="text-align:center;">New here? Click
    <a href="${pageContext.servletContext.contextPath}/registration">here</a> to register.</p>
<form name="dataForm" class="container" method="POST" action="${pageContext.servletContext.contextPath}/login"
      id="loginForm">
    <div>
        <label for="username">Username: </label>
        <input type="text" class="form-control" name="username" id="username"/>
    </div>
    <div>
        <label for="password">Password: </label>
        <input type="password" class="form-control" name="password" id="password"/>
    </div>
    <div>
        <br>
        <input type="submit" class="form-control" value="Login">
    </div>
</form>
</body>
</html>