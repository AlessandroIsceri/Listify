<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="<c:url value="/resources/scripts/toDoList.js" />"></script>
<link href="<c:url value="/resources/styles/toDoList.css" />" rel="stylesheet">
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
                	<a class="nav-link" aria-current="page" href="#" id="logout"><i class="fa-solid fa-right-from-bracket"></i>Logout</a>
                	<a class="nav-link" href="" aria-current="page" onclick="deleteUser()" id="deleteUser"><i class="fa-solid fa-user-slash"></i>Delete Account</a>
                </div>
            </div>
        </div>
    </nav>

	<!-- Vertically centered modal for new activities-->
    <div class="modal fade" id="newActivityModal" tabindex="-1" aria-labelledby="newActivityModal" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="newActivityModalh5">Add a new Activity</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <label for="modalActivityName">Name: </label><input class="form-control" type="text" id="modalActivityName">
                <label for="modalActivityPriority">Priority:</label>
                <select class="form-control" name="categories" id="modalActivityPriority">
                    <option value="1">1 (max priority)</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10 (min priority)</option>
                </select>
                <label for="modalActivityExpDate">Expiration date:</label>
                <input id="modalActivityExpDate" class="form-control" type="date">
                <label for="modalActivityCategory">Category:</label>
                <select class="form-control" name="categories" id="modalActivityCategory">
                    <option value="to-do-activities-ul">To Do</option>
                    <option value="in-progress-activities-ul">In Progress</option>
                    <option value="completed-activities-ul">Completed</option>
                </select>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-success" id="newActivityButton" data-bs-dismiss="modal" onclick="addActivity()">Add</button>
            </div>
          </div>
        </div>
    </div>
    
    <!-- Vertically centered modal for modifying an existing activity-->
    <div class="modal fade" id="modifyActivityModal" tabindex="-1" aria-labelledby="modifyActivityModal" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modifyActivityModalh5">modify an Activity</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <label for="modifyModalActivityName">Name: </label><input class="form-control" type="text" id="modifyModalActivityName">
                <label for="modifyModalActivityPriority">Priority:</label>
                <select class="form-control" name="categories" id="modifyModalActivityPriority">
                    <option value="1">1 (max priority)</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10 (min priority)</option>
                </select>
                <label for="modifyModalActivityExpDate">Expiration date:</label>
                <input id="modifyModalActivityExpDate" class="form-control" type="date">
                <input type="hidden" id="modifyModalActivityId">
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-danger" id="deleteActivityButton" data-bs-dismiss="modal" onclick="deleteActivity()">Delete</button>
              <button type="button" class="btn btn-success" id="modifyActivityButton" data-bs-dismiss="modal" onclick="updateActivity()">Update</button>
            </div>
          </div>
        </div>
    </div>
    
    <input type="hidden" value="${username}" id="username">
    <!-- html page -->
    <div class="container-fluid">
    	<input type="hidden" id="list-id" value="${list.id}">
    	<div class="row bg-light">
    		<div class="col-2">
	    		<div class="input-group input-group-lg">
					<input type="text" disabled class="form-control bg-light border-0" id="list-name" value="${list.name}" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-lg">
				</div>
			</div>
			<div class="col-6">
				<button class="btn btn-primary" id="addActivity" data-bs-toggle="modal" data-bs-target="#newActivityModal">Add Activity</button>
			</div>
    	</div>
    	<div class="alert alert-success alert-dismissible fade show d-none" role="alert" id="alert-box">
			<span id="alert-message"> You should check in on some of those fields below. </span>
			<button type="button" class="btn-close" onclick="removeAlertBox()"></button>
		</div>
    	<br>
        <div class="row justify-content-center">
        	<div class="col-3">
        		<br><br>
            	<canvas id="pieChart"></canvas>
            </div>
            <div class="col-3" ondrop="drop(event)" ondragover="allowDrop(event)">
                <ul class="list-group p-3 bg-light gap-2" id="to-do-activities-ul">
                	<h4>To-do Activities:</h4>
                    <c:forEach items="${list.toDoList}" var="activity">
                    	<c:if test="${activity.category == 'To Do'}">
				    		<li class="list-group-item border-0 rounded-pill" draggable="true" ondrag="drag(event)" id="${activity.id}" data-bs-toggle="modal" data-bs-target="#modifyActivityModal" onclick="fillForm(this)">
								<input type="hidden" value="${activity.name}">
								<input type="hidden" value="${activity.expirationDate}">
								<input type="hidden" value="${activity.priority}">
								<input type="hidden" value="To Do">
								<span><b>${activity.name}</b></span>
					  			<p><i class="fa-regular fa-clock"></i><span> ${activity.expirationDate}</span><br>
								<i class="fa-solid fa-exclamation"></i><span> Priority: ${activity.priority}</span></p>
				    		</li>
						</c:if>
					</c:forEach>
                </ul>
            </div>
            <div class="col-3" ondrop="drop(event)" ondragover="allowDrop(event)">
                <ul class="list-group p-3 bg-light gap-2" id="in-progress-activities-ul"> 
                	<h4>In-progress Activities:</h4>
                	<c:forEach items="${list.toDoList}" var="activity">
                    	<c:if test="${activity.category == 'In Progress'}">
				    		<li class="list-group-item border-0 rounded-pill" draggable="true" ondrag="drag(event)" id="${activity.id}" data-bs-toggle="modal" data-bs-target="#modifyActivityModal" onclick="fillForm(this)">
								<input type="hidden" value="${activity.name}">
								<input type="hidden" value="${activity.expirationDate}">
								<input type="hidden" value="${activity.priority}">
								<input type="hidden" value="In Progress">
								<span><b>${activity.name}</b></span>
					  			<p><i class="fa-regular fa-clock"></i><span> ${activity.expirationDate}</span><br>
								<i class="fa-solid fa-exclamation"></i><span> Priority: ${activity.priority}</span></p>
				    		</li>			
				    	</c:if>
					</c:forEach>   
                </ul>
            </div>
            <div class="col-3" ondrop="drop(event)" ondragover="allowDrop(event)">
                <ul class="list-group p-3 bg-light gap-2"  id="completed-activities-ul">
                	<h4>Completed Activities:</h4>
                	<c:forEach items="${list.toDoList}" var="activity">
                    	<c:if test="${activity.category == 'Completed'}">
				    		<li class="list-group-item border-0 rounded-pill" draggable="true" ondrag="drag(event)" id="${activity.id}" data-bs-toggle="modal" data-bs-target="#modifyActivityModal" onclick="fillForm(this)">
								<input type="hidden" value="${activity.name}">
								<input type="hidden" value="${activity.expirationDate}">
								<input type="hidden" value="${activity.priority}">
								<input type="hidden" value="Completed">
								<span><b>${activity.name}</b></span>
					  			<p><i class="fa-regular fa-clock"></i><span> ${activity.expirationDate}</span><br>
								<i class="fa-solid fa-exclamation"></i><span> Priority: ${activity.priority}</span></p>
				    		</li>
						</c:if>
					</c:forEach> 
                </ul>
            </div>
            
        </div>
    </div>

</body>
</html>