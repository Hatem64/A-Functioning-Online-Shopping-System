const signInForm = document.getElementById("login-form");
const errorMessage = document.getElementById("login-error-message");

signInForm.addEventListener("submit", (event) => {
	event.preventDefault();

	const username = signInForm.userName.value;
	// const password = signInForm.password.value;

	fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/shippingCompany/login", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ name: username }),
	})
		.then((response) => response.text())
		.then((data) => {
			const responseNumber = parseInt(data);
			errorMessage.textContent = data;
			if (!Number.isNaN(responseNumber)) {
				localStorage.clear();
				console.log(responseNumber);
				localStorage.setItem("id", responseNumber.toString());
				signInForm.reset();
				window.location.href = "shippingCompany.html";
			} else {
				errorMessage.textContent = "Invalid username or password";
			}
		})
		.catch((error) => {
			console.error(error);
			errorMessage.textContent = "An error occurred while logging in";
		});
});
