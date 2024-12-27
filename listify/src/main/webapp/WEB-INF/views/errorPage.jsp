<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false" %>
<meta charset="UTF-8">
<title>Error - Listify</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="https://kit.fontawesome.com/8706b528d6.js" crossorigin="anonymous"></script>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light sticky-top" id="navbar">
	    <div class="container-fluid">
	        <a class="navbar-brand" disabled>
	            <img src="<c:url value="/resources/listify_logo.png"/>" alt="Logo" width="50" height="50"  class="d-inline-block align-text-top">
	        </a>
	        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
	            <span class="navbar-toggler-icon"></span>
	        </button>
	        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
	            <div class="navbar-nav">
	                <div class="nav-link active"><i class="fa-solid fa-user"></i> Hi, <%=session.getAttribute("username")%></div>
	                <a class="nav-link" aria-current="page" href="/listify/<%=session.getAttribute("username")%>/home"><i class="fa-solid fa-house"></i> Home</a>
	            	<a class="nav-link" aria-current="page" href="#" id="logout"><i class="fa-solid fa-right-from-bracket"></i>Logout</a>
	            </div>
	        </div>
	    </div>
	</nav>
	
	You are trying to access a reserved area without permissions!
	
</body>
</html>