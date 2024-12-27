const URL_PREFIX = window.location.origin;
var username = null
var rowCount = null

//initialize useful variables
function init(){
	username = document.getElementById("username").value
	rowCount = document.getElementById("rowCount").value
}

//fill the form when the button for updating the name of a list is clicked
function fillForm(button){
	listId = button.parentElement.children[0].value
	listName = button.parentElement.children[1].value
	document.getElementById("modifyToDoListName").value = listName
	document.getElementById("modifyToDoListNameId").value = listId
}

//function to print a toast message to the user
function printToast(headerMessage, boydMessage, bgClass){
	document.getElementById("toast-header").innerHTML = headerMessage
	document.getElementById("toast-body").innerHTML = boydMessage
	document.getElementById("toast-img").classList.add(bgClass)
	document.getElementById("toast-div").classList.add("show")	
}

//function to send the update name request to the controller
async function updateListName(){
	listId = document.getElementById("modifyToDoListNameId").value
	newListName = document.getElementById("modifyToDoListName").value
	//send update request 
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + username + "/updateListName/" + listId,
		{
			method: "PUT",
			headers: {
		        "Content-Type": "application/json",
		    },
		  	body: newListName
		}
	));
	
	if(res.status == 200){
		//update HTML page
		document.getElementById("list-" + listId + "-name").innerHTML = newListName
		document.getElementById("list-" + listId + "-name-hidden").value = newListName
		
		printToast("SUCCESS", "The list has been updated correctly", "bg-success")
	}else{
		printToast("ERROR", "An error occured during the update", "bg-danger")
	}
}

async function deleteToDoList(button){
	td = button.parentElement
	listId = td.children[0].value
	//send delete request
	res = await fetch(new Request(URL_PREFIX + "/listify/API/" + username + "/deleteList/" + listId,
		{
			method: "DELETE",
		}
	));
	
	if(res.status == 200){
		//update HTML page
		tr = td.parentElement
		tbody = tr.parentElement
		tbody.removeChild(tr)
		rowCount--
		
		//create a success message for the user
		printToast("SUCCESS", "The list has been deleted correctly", "bg-success")
	}else{
		printToast("ERROR", "An error occured during the deletion", "bg-danger")
	}
}

async function addList(){
	listName = document.getElementById("newToDoListName").value
	
	//send request to create the new list /API/{username}/createList
	response = await fetch(URL_PREFIX + "/listify/API/" + username + "/createList", {
		method: "POST", 	
		headers: {
	        "Content-Type": "application/json",
	    },
	  	body: listName
	});
	newId = await response.json()
	
	if(newId != -1){ //the list has been created on the DB
		//update HTML page
		createTableRow(listName, newId)
		
		//reset form
		document.getElementById("newToDoListName").value = "";
		
		printToast("SUCCESS", "The list has been created correctly", "bg-success")
	}else{
		printToast("ERROR", "An error occured during the creation", "bg-danger")
	}
}

//create a new line in the table and fill it
function createTableRow(listName, newId){
	tbody = document.querySelectorAll('tbody')[0];	
	
	//create the row
	rowCount++
	tr = document.createElement("tr")
	th = document.createElement("th")
	th.setAttribute("scope","row")
	th.innerHTML = rowCount
	
	//list name cell
	tdName = document.createElement("td")
	aName = document.createElement("a")
	aName.href = "toDoList/" + newId
	aName.id = "list-" + newId + "-name"
	aName.innerHTML = listName
	
	//list operations cell
	tdOperations = document.createElement("td")
	
	//hidden elements
	inputId = document.createElement("input")
	inputId.setAttribute("type", "hidden")
	inputId.value = newId
	inputName = document.createElement("input")
	inputName.setAttribute("type", "hidden")
	inputName.id = "list-"+newId+"-name-hidden"
	inputName.value = listName
	
	//delete button
	deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.className = 'btn';
    deleteButton.onclick = function () {
        deleteToDoList(this);
    };

	//delete icon
    trashIcon = document.createElement('i');
    trashIcon.className = 'fa-solid fa-trash-can';
    trashIcon.style.color = '#DC3545';
	
	//edit button
	editButton = document.createElement('button');
    editButton.type = 'button';
    editButton.className = 'btn';
    editButton.setAttribute('data-bs-toggle', 'modal');
    editButton.setAttribute('data-bs-target', '#modifyToDoListModal');
    editButton.onclick = function () {
        fillForm(this);
    };

	//edit icon
	editIcon = document.createElement('i');
    editIcon.className = 'fa-solid fa-pen-to-square';
    editIcon.style.color = '#0D6EFD';

	//append all the elements created
	tdName.appendChild(aName)
	
	tdOperations.appendChild(inputId)	
	tdOperations.appendChild(inputName)
	
	deleteButton.appendChild(trashIcon)
	editButton.appendChild(editIcon)
	
	tdOperations.appendChild(deleteButton)
	tdOperations.appendChild(editButton)
	
	tr.appendChild(th)
	tr.appendChild(tdName)
	tr.appendChild(tdOperations)
	
	tbody.appendChild(tr)
}

async function logout(){
	response = await fetch(URL_PREFIX + "/listify/API/" + username + "/logout", {
			method: "POST"
	});
	if(response.status == 200){
		//redirect to login page
		window.location.href = URL_PREFIX + "/listify/";
	}else{
		printToast("ERROR", "An error occurred during the logout", "bg-danger")
	}
}