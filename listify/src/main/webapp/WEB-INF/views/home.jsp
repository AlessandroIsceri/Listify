<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false" %>
<meta charset="UTF-8">
<title>HomePage - Listify</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="https://kit.fontawesome.com/8706b528d6.js" crossorigin="anonymous"></script>
<link href="<c:url value="/resources/styles/home.css" />">
<script src="<c:url value="/resources/scripts/home.js" />"></script>
<script src="<c:url value="/resources/scripts/utils.js" />"></script>
</head>
<body onload="init()">

	<!-- message box -->
	<div class="toast-container position-fixed bottom-0 end-0 p-3">
		<div class="toast" id="toast-div">
    		<div class="toast-header">
    			<div class="rounded me-2" id="toast-img" style="width: 20px; height: 20px;"></div>
      			<strong class="me-auto" id="toast-header"></strong>
      			<button type="button" class="btn-close" data-bs-dismiss="toast"></button>
    		</div>
    		<div class="toast-body" id="toast-body">
    		</div>
  		</div>
  	</div>

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
	                <div class="nav-link active"><i class="fa-solid fa-user"></i> Hi, ${username}</div>
	                <a class="nav-link" aria-current="page" href="/listify/${username}/home"><i class="fa-solid fa-house"></i> Home</a>
	            	<button class="nav-link" aria-current="page" onclick="logout()" id="logout"><i class="fa-solid fa-right-from-bracket"></i>Logout</button>
	            	<button class="nav-link" aria-current="page" onclick="deleteUser()" id="deleteUser"><i class="fa-solid fa-user-slash"></i>Delete Account</button>
	            </div>
	        </div>
	    </div>
	</nav>
	
	<!-- Vertically centered modal to create a new list-->
	<div class="modal fade" id="newToDoListModal" tabindex="-1" aria-labelledby="newToDoListModal" aria-hidden="true">
	    <div class="modal-dialog modal-dialog-centered">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="newToDoListModalh5">Create a new To Do List</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	        <div class="modal-body">
	            <label for="newToDoListName">Name: </label><input class="form-control" type="text" id="newToDoListName">
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-success" id="newToDoListButton" data-bs-dismiss="modal" onclick="addList()">Create</button>
	        </div>
	      </div>
	    </div>
	</div>
	
	<!-- Vertically centered modal for modifying an existing list-->
	<div class="modal fade" id="modifyToDoListModal" tabindex="-1" aria-labelledby="modifyToDoListModal" aria-hidden="true">
	    <div class="modal-dialog modal-dialog-centered">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="modifyToDoListModalh5">modify a To Do List</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	        <div class="modal-body">
	            <label for="modifyToDoListName">Name: </label><input class="form-control" type="text" id="modifyToDoListName">
	            <input type="hidden" id="modifyToDoListNameId">
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-success" id="modifyToDoListButton" data-bs-dismiss="modal" onclick="updateListName()">Update</button>
	        </div>
	      </div>
	    </div>
	</div>
	<input type="hidden" value="${username}" id="username">

	<div class="container-fluid">
		<div class="row bg-light">
    		<div class="col-2">
	    		<div class="input-group input-group-lg">
					<input type="text" disabled class="form-control bg-light border-0" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-lg" value="Your To Do Lists">
				</div>
			</div>
			<div class="col-6">
				<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#newToDoListModal">Add To Do List</button>
			</div>
    	</div>
    	<div class="alert alert-success alert-dismissible fade show d-none" role="alert" id="alert-box">
			<span id="alert-message"> </span>
			<button type="button" class="btn-close" onclick="removeAlertBox()"></button>
		</div>
	</div>
	<div class="container">
		<br>
		<table class="table table-hover">
			<thead>
			    <tr>
		  	    	<th scope="col">#</th>
		  	        <th scope="col">To-Do List Name</th>
			        <th scope="col">Operations</th>
			  	</tr>
			</thead>
		  	<tbody>
		  		<c:set var="count" value="1" scope="page" />
				<c:forEach items="${toDoLists}" var="list">
					<tr>
			     		<th scope="row">${count}</th>
			      		<td><a href="toDoList/${list.id}" id="list-${list.id}-name">${list.name}</a></td>
			      		<td>
			      			<input type="hidden" value="${list.id}">
			      			<input type="hidden" value="${list.name}" id="list-${list.id}-name-hidden">
			      			<button type="button" class="btn" onclick="deleteToDoList(this)">
			      				<i class="fa-solid fa-trash-can" style="color:#DC3545"></i>
			      			</button>
			      			<button type="button" class="btn" data-bs-toggle="modal" data-bs-target="#modifyToDoListModal" onclick="fillForm(this)">
			      				<i class="fa-solid fa-pen-to-square" style="color:#0D6EFD"></i>
			      			</button>
			      		</td>
		    		</tr>
		    	<c:set var="count" value="${count + 1}" scope="page"/>
				</c:forEach>
			</tbody>
		</table>
		<input type="hidden" id="rowCount" value="${count - 1}">
	</div>
</body>
</html>