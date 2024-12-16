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
    if(ev.target.parentElement.tagName != "DIV"){ //c'è almeno un elemento nella categoria target
        targetCategory = ev.target.parentElement.id //id of the target category
        ul = ev.target.parentElement  //parent element (ul)
        targetId = ev.target.id //id of the target element
        target = ev.target //target element
        
    }else{ //nessun elemento nella categoria target (targetId = null, target = null)
        targetCategory = ev.target.childNodes[1].id
        targetId = null
        target = null
        ul = ev.target
    }
    draggedElementId = draggedElement.id
    
    if(targetCategory == draggedElement.parentElement.id){ //se sono nella stessa categoria
        if(targetId != draggedElementId){
            //invert the position of the two elements
            const temp = document.createElement('div');
            // Inserisci il temp prima del primo elemento
            ul.replaceChild(temp, target);
            // Sposta il primo elemento nella posizione del secondo
            ul.replaceChild(target, draggedElement);
            // Sposta il secondo elemento nella posizione originale del primo
            ul.replaceChild(draggedElement, temp);
        }
    }else{ //se sono in categorie diverse
        document.getElementById(targetCategory).appendChild(document.getElementById(draggedElementId))
        if(target != null){ //se c'è almeno un elemento nella lista target
            insertAfter(draggedElement, target)
        }
    }
}

function addActivity(){
    ul = document.getElementById("to-do-activities-ul")
    //<li class="list-group-item" draggable="true" ondrag="drag(event)" id="preparo github">preparo github</li>
    li = document.createElement("li")
    li.classList.add("list-group-item")
    li.draggable = "true"
    li.ondrag =  drag
    activityName = document.getElementById("modalActivityName").value
    li.id = activityName
    li.innerHTML = activityName
    ul.appendChild(li)
}