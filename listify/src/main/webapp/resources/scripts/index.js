const URL_PREFIX = window.location.origin;

//function to clean the span that could be displayed
function cleanSpan(){
    document.getElementById("loginError").style.display = "none";
    document.getElementById("newAccountError").style.display = "none";
    document.getElementById("newAccountErrorEmpty").style.display = "none";
}

async function login(){
	cleanSpan();
	let email = document.getElementById("login-email").value;
	let password = document.getElementById("login-password").value;
	
	//send the login request to the controller
	response = await fetch(URL_PREFIX + "/listify/API/login/", {
	    method: "POST",
	    headers: {
	        "Content-Type": "application/json",
	    },
	    body: JSON.stringify({"email" : email, "password" : password}),
	});
	if(response.status == 404){
		document.getElementById("loginError").style.display = "block";	
	} else{
		username = await response.text()
		window.location.replace(URL_PREFIX + "/listify/" + username + "/home") //redirect to home page
	}
}

async function newAccount(){
	cleanSpan();
	let email = document.getElementById("register-email").value;
	let username = document.getElementById("register-username").value;
	let password = document.getElementById("register-password").value;
	
	//if one of the input is empty, error
	if(email.length == 0 || password.length == 0 || username.length == 0){
	    document.getElementById("newAccountErrorEmpty").style.display = "block";
	    return;
	}
	
	//send the register request to the controller
	const response = await fetch(URL_PREFIX + "/listify/API/register/", {
	    method: "POST",
	    headers: {
	        "Content-Type": "application/json",
	    },
	    body: JSON.stringify({"email" : email, "username":username, "password" : password}),
	});
	
	if (response.status == 201) {
		window.location.replace(URL_PREFIX + "/listify/" + username + "/home") //redirect to home page
	} else{
	    document.getElementById("newAccountError").style.display = "block";
	}
}  