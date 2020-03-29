<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script>
        function validate() {
            var name = $('#name').val();
            var pass = $('#pass').val();
            var massage = "";
            if (name === "") {
                massage += "name";
            }
            if (pass === "") {
                if (massage !== "") {
                    massage += " and ";
                }
                massage += "password";
            }
            if (massage !== "") {
                alert("Please enter: " + massage);
            } else {
                var user = {
                    username: name,
                    password: pass
                };
                response(user);
            }
            return false;
        }
        function response(user) {
            var url = "${pageContext.servletContext.contextPath}/registration";
            $.ajax({

                type: "POST",
                url: url,
                contentType: 'application/json',
                data: JSON.stringify(user),
                success: [function ($data) {
                    resultProcessing($data);
                }],
                dataType: 'json'
            });
        }
        function resultProcessing($data) {
            if ($data['status'] === 'valid') {
                alert("The user was registered successfully.");
                var url = "${pageContext.servletContext.contextPath}/";
                document.location.href = url;
            } else {
                alert("The user with this name exists on the server. Please choose other name.");
                document.dataForm.reset();
            }
        }
    </script>
</head>
<body>
<h1 style="text-align:center;">Registration</h1>
<form name="dataForm" class="container">
    <div>
        <label for="name">Name: </label>
        <input type="text" class="form-control" name="name" placeholder="enter the name" id="name">
    </div>
    <div>
        <label for="pass">Password: </label>
        <input type="password" class="form-control" name="password" placeholder="enter the password" id="pass">
    </div>
    <div>
        <br>
        <input type="submit" class="form-control" onclick="return validate();" value="Register">
    </div>
</form>
</body>
</html>