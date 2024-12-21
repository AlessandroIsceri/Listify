<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false" %>
<meta charset="UTF-8">
<title>Listify</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="https://kit.fontawesome.com/8706b528d6.js" crossorigin="anonymous"></script>
<script src="<c:url value="/resources/scripts/index.js" />"></script>
</head>
<body>

	<!-- Vertically centered modal -->
    <div class="modal d-block" id="incomesModal" tabindex="-1" aria-labelledby="incomesModal" aria-hidden="false">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title mx-auto d-block" id="incomesModal">
                <img src="<c:url value="/resources/listify_logo.png"/>" alt="Logo" width="90vh" height="90vh" ></h5>
            </div>
            <div class="modal-body">
                <h2 class="fs-5">Login</h2>
                <hr>
                <label for="email">Email: </label>
                <input class="form-control" type="text" id="email">
                <label for="password">Password:</label>
                <input class="form-control" type="password" id="password">
                <span id="loginError" style="color:red; display: none;">Error, wrong username or password</span>
                <span id="newAccountError" style="color:red; display: none;">Error, username already exists</span>
                <span id="newAccountErrorEmpty" style="color:red; display: none;">Error, empty username or password</span>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-primary" id="login" onclick="login()">Login</button>
              <button type="button" class="btn btn-primary" id="newAccount">Create a new account</button>
            </div>
          </div>
        </div>
    </div>
</body>
</html>