<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false" %>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
<script src="<c:url value="/resources/scripts/toDoList.js" />"></script>
<script src="https://kit.fontawesome.com/8706b528d6.js" crossorigin="anonymous"></script>
</head>
<body onload="init()">

	<!-- Vertically centered modal for new activities-->
    <div class="modal fade" id="newActivityModal" tabindex="-1" aria-labelledby="newActivityModal" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="newActivityModal">Add a new Activity</h5>
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
              <button type="button" class="btn btn-primary" id="newActivityButton" data-bs-dismiss="modal" onclick="addActivity()">Add</button>
            </div>
          </div>
        </div>
    </div>
    
    <!-- Vertically centered modal for modifying an existing activity-->
    <div class="modal fade" id="modifyActivityModal" tabindex="-1" aria-labelledby="modifyActivityModal" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modifyActivityModal">modify an Activity</h5>
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
              <button type="button" class="btn btn-primary" id="modifyActivityButton" data-bs-dismiss="modal" onclick="updateActivity()">Update</button>
            </div>
          </div>
        </div>
    </div>
    
    <!-- html page -->
    <div class="container">
    	Hi ${username}!
    	${list.name} to-do list: <br>
        <button class="btn btn-success w-100" id="addActivity" data-bs-toggle="modal" data-bs-target="#newActivityModal">+</button>
        <div class="row">
            <div class="col-4" ondrop="drop(event)" ondragover="allowDrop(event)">
                To-do Activities:
                <ul class="list-group" id="to-do-activities-ul">
                    <c:forEach items="${list.toDoList}" var="activity">
                    	<c:if test="${activity.category == 'To Do'}">
				    		<li class="list-group-item" draggable="true" ondrag="drag(event)" id="${activity.id}" data-bs-toggle="modal" data-bs-target="#modifyActivityModal" onclick="fillForm(this)">
								<input type="hidden" value="${activity.name}">
								<input type="hidden" value="${activity.expirationDate}">
								<input type="hidden" value="${activity.priority}">
								<input type="hidden" value="to-do">
								<h5>${activity.name}</h5>
					  			<p><i class="fa-regular fa-clock"></i><span> ${activity.expirationDate}</span><br>
								<i class="fa-solid fa-exclamation"></i><span> Priority: ${activity.priority}</span></p>
				    		</li>
						</c:if>
					</c:forEach>
                </ul>
            </div>
            <div class="col-4" ondrop="drop(event)" ondragover="allowDrop(event)">
                In-progress Activities:
                <ul class="list-group" id="in-progress-activities-ul"> 
                	<c:forEach items="${list.toDoList}" var="activity">
                    	<c:if test="${activity.category == 'In Progress'}">
				    		<li class="list-group-item" draggable="true" ondrag="drag(event)" id="${activity.id}" data-bs-toggle="modal" data-bs-target="#modifyActivityModal" onclick="fillForm(this)">
								<input type="hidden" value="${activity.name}">
								<input type="hidden" value="${activity.expirationDate}">
								<input type="hidden" value="${activity.priority}">
								<input type="hidden" value="in-progress">
								<h5>${activity.name}</h5>
					  			<p><i class="fa-regular fa-clock"></i><span> ${activity.expirationDate}</span><br>
								<i class="fa-solid fa-exclamation"></i><span> Priority: ${activity.priority}</span></p>
				    		</li>			
				    	</c:if>
					</c:forEach>   
                </ul>
            </div>
            <div class="col-4" ondrop="drop(event)" ondragover="allowDrop(event)">
                Completed-activities
                <ul class="list-group"  id="completed-activities-ul">
                	<c:forEach items="${list.toDoList}" var="activity">
                    	<c:if test="${activity.category == 'Completed'}">
				    		<li class="list-group-item" draggable="true" ondrag="drag(event)" id="${activity.id}" data-bs-toggle="modal" data-bs-target="#modifyActivityModal" onclick="fillForm(this)">
								<input type="hidden" value="${activity.name}">
								<input type="hidden" value="${activity.expirationDate}">
								<input type="hidden" value="${activity.priority}">
								<input type="hidden" value="completed">
								<h5>${activity.name}</h5>
					  			<p><i class="fa-regular fa-clock"></i><span> ${activity.expirationDate}</span><br>
								<i class="fa-solid fa-exclamation"></i><span> Priority: ${activity.priority}</span></p>
				    		</li>
						</c:if>
					</c:forEach> 
                </ul>
            </div>
        </div>
        <button type="button" class="btn btn-success w-100" id="saveChanges" onclick="sendUpdateRequest()">Save Changes</button>
    </div>

</body>
</html>