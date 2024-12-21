const URL_PREFIX = window.location.origin;

//function to clean the span that could be displayed
function cleanSpan(){
    document.getElementById("loginError").style.display = "none";
    document.getElementById("newAccountError").style.display = "none";
    document.getElementById("newAccountErrorEmpty").style.display = "none";
}

async function login(){
	cleanSpan();
	let email = document.getElementById("email").value;
	let password = document.getElementById("password").value;
	
	//encrypt password
	//let encryptedPassword = hex_sha512(password);
	
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