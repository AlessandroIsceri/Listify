async function logout(){
	//send logout request to the controller
	response = await fetch(URL_PREFIX + "/listify/API/" + username + "/logout", {
			method: "POST"
	});
	if(response.status == 200){
		//redirect to login page
		location.href = URL_PREFIX + "/listify/";
	}else{
		printToast("ERROR", "An error occurred during the logout", "bg-danger")
	}
}

//function to delete an account
async function deleteUser(){
	response = await fetch(URL_PREFIX + "/listify/API/" + username + "/deleteUser", {
		method: "DELETE"
	});
	if(response.status == 200){
		//redirect to login page
		location.href = URL_PREFIX + "/listify/";
	}else{
		printToast("ERROR", "An error occurred during the account deletion", "bg-danger")
	}
}

//function to print a toast message to the user
function printToast(headerMessage, boydMessage, bgClass){
	document.getElementById("toast-header").innerHTML = headerMessage
	document.getElementById("toast-body").innerHTML = boydMessage
	document.getElementById("toast-img").classList.add(bgClass)
	document.getElementById("toast-div").classList.add("show")	
}