draggedElement = null
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
		document.getElementById(targetCategory).appendChild(document.getElementById(draggedElementId))
        if(target != undefined){ //if there is at least one element in target list
            insertAfter(draggedElement, target)
        }
    }
}

function addActivity(){
	//add the activity to HTML page
	ulId = document.getElementById("modalActivityCategory").value
    ul = document.getElementById(ulId)
	
	//<li data-bs-toggle="modal" data-bs-target="#modifyActivityModal" onclick="fillForm(this)">

    li = document.createElement("li")
    li.classList.add("list-group-item")
    li.draggable = "true"
    li.ondrag = drag
    activityName = document.getElementById("modalActivityName").value
    li.id = activityName
	li.setAttribute('data-bs-toggle', 'modal');
	li.setAttribute('data-bs-target', '#modifyActivityModal');
	li.addEventListener('click', function() {
	    fillForm(this); // Passa il riferimento all'elemento cliccato
	});
	
	
	h5 = document.createElement("h5")
	h5.innerHTML = activityName
	p = document.createElement("p")
	clockIcon = document.createElement("i")
	clockIcon.classList.add("fa-regular")
	clockIcon.classList.add("fa-clock")
	clockSpan = document.createElement("span")
	clockSpan.innerHTML = " " + document.getElementById("modalActivityExpDate").value
	
	priorityIcon = document.createElement("i")
	priorityIcon.classList.add("fa-solid")
	priorityIcon.classList.add("fa-exclamation")
	prioritySpan = document.createElement("span")
	prioritySpan.innerHTML = " Priority: " + document.getElementById("modalActivityPriority").value
	
	p.appendChild(clockIcon)
	p.appendChild(clockSpan)
	p.appendChild(document.createElement("br"))
	p.appendChild(priorityIcon)
	p.appendChild(prioritySpan)
	
	li.appendChild(h5)
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
	document.getElementById("modifyModalActivityName").value = li.children[0].innerHTML;
	document.getElementById("modifyModalActivityPriority").value = li.children[1].children[4].innerHTML.substring(11)
	document.getElementById("modifyModalActivityExpDate").value = li.children[1].children[1].innerHTML.trim();
}

function updateActivity(){
	
}