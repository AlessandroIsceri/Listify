const URL_PREFIX = window.location.origin;
var username = null
var rowCount = null

function init(){
	username = document.getElementById("username").value
	rowCount = document.getElementById("rowCount").value
}

function fillForm(button){
	listId = button.parentElement.children[0].value
	listName = button.parentElement.children[1].value
	document.getElementById("modifyToDoListName").value = listName
	document.getElementById("modifyToDoListNameId").value = listId
}

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
		document.getElementById("list-" + listId + "-name").innerHTML = newListName
		document.getElementById("list-" + listId + "-name-hidden").value = newListName
		
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "SUCCESS"
		document.getElementById("toast-body").innerHTML = "The list name has been updated correctly"
		document.getElementById("toast-img").classList.add("bg-success")
		document.getElementById("toast-div").classList.add("show")	
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
		tr = td.parentElement
		tbody = tr.parentElement
		tbody.removeChild(tr)
		rowCount--
		
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "SUCCESS"
		document.getElementById("toast-body").innerHTML = "The list has been deleted correctly"
		document.getElementById("toast-img").classList.add("bg-success")
		document.getElementById("toast-div").classList.add("show")	
	}
}

async function addList(){
	listName = document.getElementById("newToDoListName").value
	
	//send request to create the new list /API/{username}/createList
	response = await fetch(URL_PREFIX + "/listify/API/" + username + "/createList", 
		{
			method: "POST", 	
			headers: {
		        "Content-Type": "application/json",
		    },
		  	body: listName});
	newId = await response.json()
	if(newId != -1){
		//the list has been created on the DB
		//create a new line in the table and fill it
		tbody = document.querySelectorAll('tbody')[0];	
		/* 
		<tr>
	 		<th scope="row">${count}</th>
	  		<td><a href="toDoList/${list.id}" id="list-${list.id}-name">${list.name}</a></td>
	  		<td>
	  			<input type="hidden" value="${list.id}">
	  			<input type="hidden" value="${list.name}">
	  			<button type="button" class="btn" onclick="deleteToDoList(this)">
	  				<i class="fa-solid fa-trash-can" style="color:#DC3545"></i>
	  			</button>
	  			<button type="button" class="btn" data-bs-toggle="modal" data-bs-target="#modifyToDoListModal" onclick="fillForm(this)">
	  				<i class="fa-solid fa-pen-to-square" style="color:#0D6EFD"></i>
	  			</button>
	  		</td>
		</tr>
		*/
		rowCount++
		tr = document.createElement("tr")
		th = document.createElement("th")
		th.setAttribute("scope","row")
		th.innerHTML = rowCount
		tdName = document.createElement("td")
		aName = document.createElement("a")
		aName.href = "toDoList/" + newId
		aName.id = "list-" + newId + "-name"
		aName.innerHTML = listName
		
		tdOperations = document.createElement("td")
		inputId = document.createElement("input")
		inputId.setAttribute("type", "hidden")
		inputId.value = newId
		
		inputName = document.createElement("input")
		inputName.setAttribute("type", "hidden")
		inputName.value = listName
		
		deleteButton = document.createElement('button');
	    deleteButton.type = 'button';
	    deleteButton.className = 'btn';
	    deleteButton.onclick = function () {
	        deleteToDoList(this);
	    };

	    trashIcon = document.createElement('i');
	    trashIcon.className = 'fa-solid fa-trash-can';
	    trashIcon.style.color = '#DC3545';
		
		editButton = document.createElement('button');
	    editButton.type = 'button';
	    editButton.className = 'btn';
	    editButton.setAttribute('data-bs-toggle', 'modal');
	    editButton.setAttribute('data-bs-target', '#modifyToDoListModal');
	    editButton.onclick = function () {
	        fillForm(this);
	    };

		editIcon = document.createElement('i');
	    editIcon.className = 'fa-solid fa-pen-to-square';
	    editIcon.style.color = '#0D6EFD';

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
		
		//reset form
		document.getElementById("newToDoListName").value = "";
		
		//create a success message for the user
		document.getElementById("toast-header").innerHTML = "SUCCESS"
		document.getElementById("toast-body").innerHTML = "The list has been created correctly"
		document.getElementById("toast-img").classList.add("bg-success")
		document.getElementById("toast-div").classList.add("show")
	}
}