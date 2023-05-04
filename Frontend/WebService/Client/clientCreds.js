const form = document.getElementById("register-form");
const message = document.getElementById("message");

form.addEventListener("submit", (e) => {
	e.preventDefault();
	const username = form.username.value;
	const password = form.password.value;
	const phonenumber = form.phone.value;
	const address = form.address.value;

	fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/client/register", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({
			username,
			password,
			address,
			phonenumber,
		}),
	})
		.then((response) => response.text())
		.then((data) => {
			message.textContent = data;
			if (data === "Client Registered") {
				form.reset();
				window.location.href = "clientSignIn.html";
			}
		})
		.catch((error) => console.error(error));
});
