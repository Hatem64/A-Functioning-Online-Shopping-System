const signInForm = document.getElementById("login-form");
const errorMessage = document.getElementById("login-error-message");
signInForm.addEventListener("submit", (event) => {
	event.preventDefault();

	const username = signInForm.name.value;
	const password = signInForm.password.value;
	const logindata = { name: username, password: password };

	fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/sellingCompany/login", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(logindata),
		credentials: "include",
	})
		.then((response) => {
			console.log(response);
			if (response.status === 200) {
				response
					.json() // Parse the JSON data from the response body
					.then((data) => {
						console.log(data);
						localStorage.setItem("sessionID", data.sessionId);
						signInForm.reset();
						window.location.href = "ViewProduct.html";
					})
					.catch((error) => {
						console.error(error);
						errorMessage.textContent = "An error occurred while logging in";
					});
			} else if (response.status === 400) {
				window.location.href = "../Client/clientOrderHistory.html";

			}
			else {
				errorMessage.textContent = "Invalid username or password";
			}
		})

		.catch((error) => {
			console.error(error);
			errorMessage.textContent = "An error occurred while logging in";
		});
});
