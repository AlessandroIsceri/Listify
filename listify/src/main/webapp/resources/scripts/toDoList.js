const URL_PREFIX = window.location.origin;
draggedElement = null

//function that retrieves new id for the activity from the server
async function getNewId(){
    let response = await fetch(URL_PREFIX + "/listify/API/getNewId/");
    return response.json();
}

function init(){
	document.getElementById("modalActivityExpDate").valueAsDate = new Date();
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

function drop(ev) {
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
		ul.appendChild(document.getElementById(draggedElementId))
		ulId = ul.id
		li = document.getElementById(draggedElementId)
		hiddenInputs = li.querySelectorAll('input[type="hidden"]');
		if(ulId == "to-do-activities-ul"){
			hiddenInputs[3].value = "To Do"
		}else if(ulId == "in-progress-activities-ul"){
			hiddenInputs[3].value = "In Progress"
		}else{
			hiddenInputs[3].value = "Completed"
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
		ul.appendChild(document.getElementById(draggedElementId))
        //if(target != undefined){ //if there is at least one element in target list
        insertAfter(draggedElement, target)
		
		li = document.getElementById(draggedElementId)
		hiddenInputs = li.querySelectorAll('input[type="hidden"]');
		if(ulId == "to-do-activities-ul"){
			hiddenInputs[3].value = "To Do"
		}else if(ulId == "in-progress-activities-ul"){
			hiddenInputs[3].value = "In Progress"
		}else{
			hiddenInputs[3].value = "Completed"
		}
        //}
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
    
	//request new id from application and set it
	newId = await getNewId()
	li.id = newId
	
	li.setAttribute('data-bs-toggle', 'modal');
	li.setAttribute('data-bs-target', '#modifyActivityModal');
	li.addEventListener('click', function() {
	    fillForm(this); // Passa il riferimento all'elemento cliccato
	});
	
	span = document.createElement("span")
	span.innerHTML = "<b>"+activityName+"</b>"
	p = document.createElement("p")
	
	date = document.getElementById("modalActivityExpDate").value
	clockIcon = document.createElement("i")
	clockIcon.classList.add("fa-regular")
	clockIcon.classList.add("fa-clock")
	clockSpan = document.createElement("span")
	clockSpan.innerHTML = " " + date
	
	priority = document.getElementById("modalActivityPriority").value
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
	
}

function fillForm(li){
	//autofill form when updating an activity
	hiddenInputs = li.querySelectorAll('input[type="hidden"]');
	document.getElementById("modifyModalActivityName").value = hiddenInputs[0].value
	document.getElementById("modifyModalActivityExpDate").value = hiddenInputs[1].value
	document.getElementById("modifyModalActivityPriority").value = hiddenInputs[2].value
	document.getElementById("modifyModalActivityId").value = li.id
}

function updateActivity(){
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
}

async function sendUpdateRequest(){
	alert = document.getElementById("alert-box")
	//first, update list name
	listId = document.getElementById("list-id").value
	newListName = document.getElementById("list-name").value;
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + "aless" + "/updateListName/" + listId,
			{
				method: "PUT",
				headers: {
			        "Content-Type": "application/json",
			    },
			  	body: newListName,
			}
	));
	if(res.status == 404){
		//error
		document.getElementById("alert-message").innerHTML = "An error occured during the update"
		return 
	}
	
	//create a JSON array containing all the activities with realtive informations
	/*
		[	
			{
			"id" : activityId
			"name" : name,
			"priority" : priority,
			"expDate" : expDate,
			"category" : category
		    }, 
			... 
		] 
	*/
	JSON_array = []
	lis = document.querySelectorAll('li');
	for(i = 0; i < lis.length; i++){
		curLi = lis[i]
		hiddenInputs = curLi.querySelectorAll('input[type="hidden"]');
		JSON_array.push({
			"id" : curLi.id,
			"name" : hiddenInputs[0].value,
			"expirationDate" : hiddenInputs[1].value,
			"priority" : hiddenInputs[2].value,
			"category" : hiddenInputs[3].value
		})	
	}
	
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + "aless" + "/updateList/" + listId,
		{
			method: "PUT",
			headers: {
		        "Content-Type": "application/json",
		    },
		  	body: JSON.stringify(JSON_array),
		}
	));
	if(res.status == 404){
		//error
		document.getElementById("alert-message").innerHTML = "An error occured during the update"
		return 
	}else{
		//200 -> ok 
		document.getElementById("alert-message").innerHTML = "Updated correctly!"
	}
	//show the alert
	alert.classList.remove('d-none');
}

function deleteActivity(){
	id = document.getElementById("modifyModalActivityId").value
	document.getElementById(id).remove();
}

function removeAlertBox(){
	alert = document.getElementById("alert-box")
	alert.classList.add('d-none');
}
