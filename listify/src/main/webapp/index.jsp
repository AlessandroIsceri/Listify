<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
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
                <div class="modal-header" style="border-bottom:0px solid black">
                    <h5 class="modal-title mx-auto d-block" id="incomesModal">
                    <img src="<c:url value="/resources/listify_logo.png"/>" alt="Logo" width="90vh" height="90vh" ></h5>
                    
                </div>
               	<ul class="nav nav-tabs" id="myTab" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="login-tab" data-bs-toggle="tab" data-bs-target="#login" type="button" role="tab" aria-controls="login" aria-selected="true">Login</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="register-tab" data-bs-toggle="tab" data-bs-target="#register" type="button" role="tab" aria-controls="register" aria-selected="false">Register</button>
                    </li>
                </ul>
                <div class="tab-content" id="myTabContent">
                    <div class="tab-pane fade show active" id="login" role="tabpanel" aria-labelledby="login-tab">
                        <div class="modal-body">
                            <label for="login-email">Email: </label>
                            <input class="form-control" type="email" id="login-email">
                            <label for="login-password">Password:</label>
                            <input class="form-control" type="password" id="login-password">
                        </div>
                        <div class="modal-footer">
                        	<span id="loginError" style="color:red; display: none;">Error: wrong username or password</span>
                            <button type="button" class="btn btn-primary" id="login" onclick="login()">Login</button>
                        </div>
                    </div>
                    <div class="tab-pane fade" id="register" role="tabpanel" aria-labelledby="register-tab">
                        <div class="modal-body">
                            <label for="register-email">Email: </label>
                            <input class="form-control" type="email" id="register-email">
                            <label for="register-username">Username: </label>
                            <input class="form-control" type="text" id="register-username">
                            <label for="register-password">Password:</label>
                            <input class="form-control" type="password" id="register-password">
                        </div>
                        <div class="modal-footer">
	                        <span id="newAccountError" style="color:red; display: none;">Error: username already exists or email already used</span>
	                		<span id="newAccountErrorEmpty" style="color:red; display: none;">Error: empty username or password</span>
                            <button type="button" class="btn btn-primary" id="newAccount" onclick="newAccount()">Register</button>
                        </div>
                    </div>
                </div>
                
                
            </div>
        </div>
    </div>
</body>
</html>