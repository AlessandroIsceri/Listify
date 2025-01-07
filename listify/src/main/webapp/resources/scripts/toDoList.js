const URL_PREFIX = window.location.origin;
username = null
listId = null
draggedElement = null
pieChart = null

//initialize useful variables and pie chart
function init(){
	var today = new Date();
	document.getElementById("modalActivityExpDate").valueAsDate = new Date(today.setMonth(today.getMonth() + 1));
	username = document.getElementById("username").value
	listId = document.getElementById("list-id").value
	createPieChart()
}

//drag and drop functions
function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    draggedElement = ev.target
}

function insertAfter(newNode, existingNode) {
    existingNode.parentNode.insertBefore(newNode, existingNode.nextSibling);
}

async function updateCategory(ulId, draggedElementId){
	//get the new category
	if(ulId == "to-do-activities-ul"){
		category = "To Do"
	}else if(ulId == "in-progress-activities-ul"){
		category = "In Progress"
	}else{
		category = "Completed"
	}
			
	//send update request to the controller
	res = await fetch(URL_PREFIX + "/listify/API/" + username + "/updateToDoList/" + listId + "/updateActivityCategory/" + draggedElementId, {
		method: "PUT",
		headers: {
	        "Content-Type": "application/json",
	    },
	  	body: category,
	});
	if(res.status == 404){
		//error
		printToast("ERROR", "An error occured during the update", "bg-danger")
		return 404
	}else{
		//200 -> ok 
		
		//update HTML page
		li = document.getElementById(draggedElementId)
		hiddenInputs = li.querySelectorAll('input[type="hidden"]');
		hiddenInputs[3].value = category;
		
		//update the chart 
		if(ulId == "to-do-activities-ul"){
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] + 1;
		}else if(ulId == "in-progress-activities-ul"){
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] + 1;
		}else{
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] + 1;
		}
		
		if(draggedElementUlId == "to-do-activities-ul"){
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] - 1;
		}else if(draggedElementUlId == "in-progress-activities-ul"){
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] - 1;
		}else{
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] - 1;
		}
		
		pieChart.update(); 
		
		printToast("SUCCESS", "The activity has been updated correctly", "bg-success")
		return 200
	}
			
}

async function drop(ev) {
    ev.preventDefault();
	//get parent element of targetted element (div)
	parent = ev.target
	while(parent != null && parent.tagName != "DIV"){
		parent = parent.parentElement
	}
	
	//get li targetted element
	target = ev.target
	while(target != null && target.tagName != "LI"){
		target = target.parentElement
	}
	
	ul = parent.childNodes[1]
	targetCategory = parent.childNodes[1].id
	draggedElementId = draggedElement.id
	
	if(target == null || parent == null){ //empty target list
		draggedElementUlId = document.getElementById(draggedElementId).parentElement.id
		ulId = ul.id
		if(await updateCategory(ulId, draggedElementId) == 200){
			ul.appendChild(document.getElementById(draggedElementId)) //append the li to the empty ul
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
		
		if(await updateCategory(ulId, draggedElementId) == 200){
			ul.appendChild(document.getElementById(draggedElementId))
	        insertAfter(draggedElement, target) //adjust the order of the moved li
		}
    }
}

async function addActivity(){
	//get Activity values
	ulId = document.getElementById("modalActivityCategory").value
    ul = document.getElementById(ulId)
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
	
	if(activityName.length == 0){
		printToast("ERROR", "The activity name is empty!", "bg-danger")
		return 
	}
	
	//request new id from application and set it (and create the activity on the DB)
	response = await fetch(URL_PREFIX + "/listify/API/" + username + "/updateToDoList/" + listId + "/createActivity", {
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
	if(response.status == 201){
		newId = await response.json()
		//update HTML page
		createLiElement(ul, newId, activityName, date, priority, category)
		
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
		
		printToast("SUCCESS", "The activity has been created correctly", "bg-success")
	}else{
		printToast("ERROR", "An error occured during the creation", "bg-danger")
	}
}

function createLiElement(ul, newId, activityName, date, priority, category){
	//create li element
	li = document.createElement("li")
    li.classList.add("list-group-item")
	li.classList.add("border-0")
	li.classList.add("rounded-pill")
    li.draggable = "true"
    li.ondrag = drag
	li.id = newId
	
	li.setAttribute('data-bs-toggle', 'modal');
	li.setAttribute('data-bs-target', '#modifyActivityModal');
	li.addEventListener('click', function() {
	    fillForm(this);
	});
	
	//create span and paragraph for the li element
	span = document.createElement("span")
	span.innerHTML = "<b>"+activityName+"</b>"
	p = document.createElement("p")
	
	
	//icons
	clockIcon = document.createElement("i")
	clockIcon.classList.add("fa-regular")
	clockIcon.classList.add("fa-clock")
	clockSpan = document.createElement("span")
	
	if(date == ""){
		clockSpan.innerHTML = " No expiration date"
	}else{
		clockSpan.innerHTML = " " + date
	}
	
	priorityIcon = document.createElement("i")
	priorityIcon.classList.add("fa-solid")
	priorityIcon.classList.add("fa-exclamation")
	prioritySpan = document.createElement("span")
	if(priority == -1){
		prioritySpan.innerHTML = " No priority"
	}else{
		prioritySpan.innerHTML = " Priority: " + priority
	}
	
	
	//append the elements to the paragraph
	p.appendChild(clockIcon)
	p.appendChild(clockSpan)
	p.appendChild(document.createElement("br"))
	p.appendChild(priorityIcon)
	p.appendChild(prioritySpan)
	
	//add 4 input fields hidden
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
	inputCategory.value = category
	
	li.appendChild(inputName)
	li.appendChild(inputDate)
	li.appendChild(inputPriority)
	li.appendChild(inputCategory)
	li.appendChild(span)
	li.appendChild(p)
	ul.appendChild(li)
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
	//get activity values
	name = document.getElementById("modifyModalActivityName").value
	priority = document.getElementById("modifyModalActivityPriority").value
	expDate = document.getElementById("modifyModalActivityExpDate").value
	id = document.getElementById("modifyModalActivityId").value
	li = document.getElementById(id)
	
	ulId = li.parentElement.id
	if(ulId == "to-do-activities-ul"){
		category = "To Do"
	}else if(ulId == "in-progress-activities-ul"){
		category = "In Progress"
	}else{
		category = "Completed"
	}
	
	if(name.length == 0){
		printToast("ERROR", "The activity name is empty!", "bg-danger")
		return 
	}
	
	//send update request to the db
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + username + "/updateToDoList/" + listId + "/updateActivity/" + id,
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
	if(res.status == 404){
		//error
		printToast("ERROR", "An error occured during the update", "bg-danger")
		return 
	}else{
		//200 -> ok 
		
		//update HTML page
		li.children[4].innerHTML = "<b>"+name+"</b>";
		
		if(expDate == ""){
			li.children[5].children[1].innerHTML = " No expiration date"
		}else{
			li.children[5].children[1].innerHTML = " " + expDate
		}
			
		if(priority == -1){
			li.children[5].children[4].innerHTML = " No priority"
		}else{
			li.children[5].children[4].innerHTML = " Priority: " + priority
		}
		
		hiddenInputs = li.querySelectorAll('input[type="hidden"]')
		hiddenInputs[0].value = name
		hiddenInputs[1].value = expDate
		hiddenInputs[2].value = priority
		
	    printToast("SUCCESS", "The activity has been updated correctly", "bg-success")
	}
}

async function deleteActivity(){
	id = document.getElementById("modifyModalActivityId").value
	
	//send request to the controller
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + username + "/updateToDoList/" + listId + "/deleteActivity/" + id,
		{
			method: "DELETE"
		}
	));
	if(res.status == 404){
		printToast("ERROR", "An error occured during the deletion", "bg-danger")
		return 
	}else{
		//200 -> ok 
		//update the chart and the HTML page
		li = document.getElementById(id)
		ulId = li.parentElement.id
		
		if(ulId == "to-do-activities-ul"){
			pieChart.data.datasets[0].data[0] = pieChart.data.datasets[0].data[0] - 1;
		}else if(ulId == "in-progress-activities-ul"){
			pieChart.data.datasets[0].data[1] = pieChart.data.datasets[0].data[1] - 1;
		}else{
			pieChart.data.datasets[0].data[2] = pieChart.data.datasets[0].data[2] - 1;
		}
		pieChart.update(); 
		document.getElementById(id).remove();
		
		printToast("SUCCESS", "The activity has been deleted correctly", "bg-success")
	}
}

//function to create the pie chart
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