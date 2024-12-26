const URL_PREFIX = window.location.origin;
username = null
listId = null
draggedElement = null
pieChart = null

//function that retrieves new id for the activity from the server

function init(){
	document.getElementById("modalActivityExpDate").valueAsDate = new Date();
	username = document.getElementById("username").value
	listId = document.getElementById("list-id").value
	createPieChart()
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    draggedElement = ev.target
}

function insertAfter(newNode, existingNode) {
    existingNode.parentNode.insertBefore(newNode, existingNode.nextSibling);
}

async function drop(ev) {
    ev.preventDefault();
	parent = ev.target
	while(parent != null && parent.tagName != "DIV"){
		parent = parent.parentElement
	}
	target = ev.target
	while(target != null && target.tagName != "LI"){
		target = target.parentElement
	}
	ul = parent.childNodes[1]
	targetCategory = parent.childNodes[1].id
	draggedElementId = draggedElement.id
	if(target == null || parent == null){
		//empty target list
		draggedElementUlId = document.getElementById(draggedElementId).parentElement.id
		ul.appendChild(document.getElementById(draggedElementId))
		ulId = ul.id
		li = document.getElementById(draggedElementId)
		hiddenInputs = li.querySelectorAll('input[type="hidden"]');
		if(ulId == "to-do-activities-ul"){
			hiddenInputs[3].value = "To Do"
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] + 1;
		}else if(ulId == "in-progress-activities-ul"){
			hiddenInputs[3].value = "In Progress"
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] + 1;
		}else{
			hiddenInputs[3].value = "Completed"
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] + 1;
		}
		
		if(draggedElementUlId == "to-do-activities-ul"){
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] - 1;
		}else if(draggedElementUlId == "in-progress-activities-ul"){
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] - 1;
		}else{
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] - 1;
		}
		
		//update the chart 
		pieChart.update(); 
		
		category = hiddenInputs[3].value
		res = await fetch(URL_PREFIX + "/listify/API/" + username + "/updateList/" + listId + "/updateActivityCategory/" + draggedElementId, {
				method: "PUT",
				headers: {
			        "Content-Type": "application/json",
			    },
			  	body: category,
			}
		);
		
		if(res.status == 404){
			//error
			//create a success message for the user
			document.getElementById("toast-header").innerHTML = "ERROR"
			document.getElementById("toast-body").innerHTML = "An error occured during the update"
			document.getElementById("toast-img").classList.add("bg-danger")
			document.getElementById("toast-div").classList.add("show")	
			return 
		}else{
			//200 -> ok 
			//create a success message for the user
			document.getElementById("toast-header").innerHTML = "SUCCESS"
			document.getElementById("toast-body").innerHTML = "The activiy has been updated correctly"
			document.getElementById("toast-img").classList.add("bg-success")
			document.getElementById("toast-div").classList.add("show")	
		}
		
		return
	}
	
	targetId = target.id
	
    if(targetCategory == draggedElement.parentElement.id){ //same category
        if(targetId != draggedElementId){ //different elements
            //invert the position of the two elements
            const temp = document.createElement('div');
            // insert temp before first element
            ul.replaceChild(temp, target);
            // move first element in the position of the second one
            ul.replaceChild(target, draggedElement);
            // move the second element into the original position of the first one
            ul.replaceChild(draggedElement, temp);
        }
    }else{ //different categories
		ul = document.getElementById(targetCategory)
		ulId = ul.id
		draggedElementUlId = document.getElementById(draggedElementId).parentElement.id
		ul.appendChild(document.getElementById(draggedElementId))
        insertAfter(draggedElement, target)
		
		li = document.getElementById(draggedElementId)
		hiddenInputs = li.querySelectorAll('input[type="hidden"]');
		if(ulId == "to-do-activities-ul"){
			hiddenInputs[3].value = "To Do"
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] + 1;
		}else if(ulId == "in-progress-activities-ul"){
			hiddenInputs[3].value = "In Progress"
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] + 1;
		}else{
			hiddenInputs[3].value = "Completed"
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] + 1;
		}
		
		if(draggedElementUlId == "to-do-activities-ul"){
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] - 1;
		}else if(draggedElementUlId == "in-progress-activities-ul"){
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] - 1;
		}else{
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] - 1;
		}
		//update the chart 
		pieChart.update(); 
		
		//send the request to the controller
		//"/API/{username}/updateList/{listId}/updateActivityCategory/{activityId}"
		category = hiddenInputs[3].value
		res = await fetch(URL_PREFIX + "/listify/API/" + username + "/updateList/" + listId + "/updateActivityCategory/" + draggedElementId, {
				method: "PUT",
				headers: {
			        "Content-Type": "application/json",
			    },
			  	body: category,
			}
		);
		if(res.status == 404){
			//error
			//create a success message for the user
			document.getElementById("toast-header").innerHTML = "ERROR"
			document.getElementById("toast-body").innerHTML = "An error occured during the update"
			document.getElementById("toast-img").classList.add("bg-danger")
			document.getElementById("toast-div").classList.add("show")	
			return 
		}else{
			//200 -> ok 
			//create a success message for the user
			document.getElementById("toast-header").innerHTML = "SUCCESS"
			document.getElementById("toast-body").innerHTML = "The activiy has been updated correctly"
			document.getElementById("toast-img").classList.add("bg-success")
			document.getElementById("toast-div").classList.add("show")	
		}
    }
}

async function addActivity(){
	//add the activity to HTML page
	ulId = document.getElementById("modalActivityCategory").value
    ul = document.getElementById(ulId)
	
    li = document.createElement("li")
    li.classList.add("list-group-item")
	li.classList.add("border-0")
	li.classList.add("rounded-pill")
    li.draggable = "true"
    li.ondrag = drag
    activityName = document.getElementById("modalActivityName").value
	priority = document.getElementById("modalActivityPriority").value
	date = document.getElementById("modalActivityExpDate").value
    
	if(ulId == "to-do-activities-ul"){
		category = "To Do";
	}else if(ulId == "in-progress-activities-ul"){
		category = "In Progress";
	}else{
		category = "Completed";
	}
	
	//request new id from application and set it
	response = await fetch(URL_PREFIX + "/listify/API/" + username + "/toDoList/" + listId + "/createActivity", {
			method: "POST",
			headers: {
		        "Content-Type": "application/json",
		    },
		  	body: JSON.stringify({
				"name": activityName,
				"priority": priority,
				"expirationDate": date,
				"category": category					
			}),
		}
	);
	newId = await response.json()
	if(newId != -1){
		li.id = newId
		
		li.setAttribute('data-bs-toggle', 'modal');
		li.setAttribute('data-bs-target', '#modifyActivityModal');
		li.addEventListener('click', function() {
		    fillForm(this); // Passa il riferimento all'elemento cliccato
		});
		
		span = document.createElement("span")
		span.innerHTML = "<b>"+activityName+"</b>"
		p = document.createElement("p")
		
		clockIcon = document.createElement("i")
		clockIcon.classList.add("fa-regular")
		clockIcon.classList.add("fa-clock")
		clockSpan = document.createElement("span")
		clockSpan.innerHTML = " " + date
		
		priorityIcon = document.createElement("i")
		priorityIcon.classList.add("fa-solid")
		priorityIcon.classList.add("fa-exclamation")
		prioritySpan = document.createElement("span")
		prioritySpan.innerHTML = " Priority: " + priority
		
		p.appendChild(clockIcon)
		p.appendChild(clockSpan)
		p.appendChild(document.createElement("br"))
		p.appendChild(priorityIcon)
		p.appendChild(prioritySpan)
		
		
		//add 3 input fields hidden <input type="hidden" value="${activity.name}">
		inputName = document.createElement("input")
		inputName.type = "hidden"
		inputName.value = activityName
		
		inputDate = document.createElement("input")
		inputDate.type = "hidden"
		inputDate.value = date
		
		inputPriority = document.createElement("input")
		inputPriority.type = "hidden"
		inputPriority.value = priority
		
		inputCategory = document.createElement("input")
		inputCategory.type = "hidden"
		if(ulId == "to-do-activities-ul"){
			inputCategory.value = "To Do"
		}else if(ulId == "in-progress-activities-ul"){
			inputCategory.value = "In Progress"
		}else{
			inputCategory.value = "Completed"
		}
		
		li.appendChild(inputName)
		li.appendChild(inputDate)
		li.appendChild(inputPriority)
		li.appendChild(inputCategory)
		li.appendChild(span)
		li.appendChild(p)
		ul.appendChild(li)
		
		//reset form
		document.getElementById("modalActivityCategory").value = "to-do-activities-ul"
		document.getElementById("modalActivityExpDate").valueAsDate = new Date();
		document.getElementById("modalActivityName").value = "";
		document.getElementById("modalActivityPriority").value = "1";
		
		//update the chart 
		if(ulId == "to-do-activities-ul"){
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] + 1;
		}else if(ulId == "in-progress-activities-ul"){
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] + 1;
		}else{
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] + 1;
		}
		
		pieChart.update();
		
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "SUCCESS"
		document.getElementById("toast-body").innerHTML = "The activiy has been created correctly"
		document.getElementById("toast-img").classList.add("bg-success")
		document.getElementById("toast-div").classList.add("show")	
		 	
	}
}

function fillForm(li){
	//autofill form when updating an activity
	hiddenInputs = li.querySelectorAll('input[type="hidden"]');
	document.getElementById("modifyModalActivityName").value = hiddenInputs[0].value
	document.getElementById("modifyModalActivityExpDate").value = hiddenInputs[1].value
	document.getElementById("modifyModalActivityPriority").value = hiddenInputs[2].value
	document.getElementById("modifyModalActivityId").value = li.id
}

async function updateActivity(){
	name = document.getElementById("modifyModalActivityName").value
	priority = document.getElementById("modifyModalActivityPriority").value
	expDate = document.getElementById("modifyModalActivityExpDate").value
	id = document.getElementById("modifyModalActivityId").value
	li = document.getElementById(id)
	li.children[4].innerHTML = "<b>"+name+"</b>";
	li.children[5].children[4].innerHTML = " Priority: " + priority
	li.children[5].children[1].innerHTML = " " + expDate
	hiddenInputs = li.querySelectorAll('input[type="hidden"]');
	hiddenInputs[0].value = name
	hiddenInputs[1].value = expDate
	hiddenInputs[2].value = priority
	
	ulId = li.parentElement.id
	//update the chart 
	if(ulId == "to-do-activities-ul"){
		category = "To Do"
	}else if(ulId == "in-progress-activities-ul"){
		category = "In Progress"
	}else{
		category = "Completed"
	}
	
	//send update request to the db
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + username + "/updateList/" + listId + "/updateActivity/" + id,
		{
			method: "PUT",
			headers: {
		        "Content-Type": "application/json",
		    },
		  	body: JSON.stringify({
				"name":name,
				"priority":priority,
				"expirationDate":expDate,
				"category": category
			}),
		}
	));
	console.log(res)
	if(res.status == 404){
		//error
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "ERROR"
		document.getElementById("toast-body").innerHTML = "An error occured during the update"
		document.getElementById("toast-img").classList.add("bg-danger")
		document.getElementById("toast-div").classList.add("show")	
		return 
	}else{
		//200 -> ok 
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "SUCCESS"
		document.getElementById("toast-body").innerHTML = "The activiy has been updated correctly"
		document.getElementById("toast-img").classList.add("bg-success")
		document.getElementById("toast-div").classList.add("show")	
	}
}

async function deleteActivity(){
	id = document.getElementById("modifyModalActivityId").value
	li = document.getElementById(id)
	ulId = li.parentElement.id
	//update the chart 
	if(ulId == "to-do-activities-ul"){
		pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] - 1;
	}else if(ulId == "in-progress-activities-ul"){
		pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] - 1;
	}else{
		pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] - 1;
	}
	pieChart.update(); 
	document.getElementById(id).remove();
	
	//send request to the controller
	///API/{username}/updateList/{listId}/deleteActivity/{activityId}
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + username + "/updateList/" + listId + "/deleteActivity/" + id,
		{
			method: "DELETE"
		}
	));
	if(res.status == 404){
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "ERROR"
		document.getElementById("toast-body").innerHTML = "An error occured during the deletion"
		document.getElementById("toast-img").classList.add("bg-danger")
		document.getElementById("toast-div").classList.add("show")
		return 
	}else{
		//200 -> ok 
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "SUCCESS"
		document.getElementById("toast-body").innerHTML = "The activiy has been deleted correctly"
		document.getElementById("toast-img").classList.add("bg-success")
		document.getElementById("toast-div").classList.add("show")
	}
}

function removeAlertBox(){
	alert = document.getElementById("alert-box")
	alert.classList.add('d-none');
}

function createPieChart(){
	ctx = document.getElementById('pieChart');
	
	ulToDo = document.getElementById("to-do-activities-ul")
	numberToDoActivities = ulToDo.children.length - 1 //-1 because every ul has h4 as first element (we want to count only the lis)
	
	ulInProgress = document.getElementById("in-progress-activities-ul")
	numberInProgressActivities = ulInProgress.children.length - 1
	
	ulCompleted = document.getElementById("completed-activities-ul")
	numberCompletedActivities = ulCompleted.children.length - 1
	
	pieChart = new Chart(ctx, {
	    type: 'pie',
	    data: {
			labels: [
			    'To-Do',
			    'In-Progress',
			    'Completed'
			  ],
			  datasets: [{
			    label: 'Activity Number',
			    data: [numberToDoActivities, numberInProgressActivities, numberCompletedActivities],
			    backgroundColor: [
			      '#DC3545',
			      '#0D6EFD',
			      '#198754'
			    ],
			    hoverOffset: 4
			  }]
	    }
	});
}
