const form = document.getElementById("login-form");
const errorMessage = document.getElementById("login-error-message");

form.addEventListener("submit", (event) => {
    event.preventDefault();

    const username = form.username.value;
    const password = form.password.value;

    fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/admin/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
    })
        .then(response => response.text())
        .then((data) => {
            if (data === 'Logged in Successfully') {
                alert('Signed in Successfully');
                window.location.href = "adminHomePage.html";
            } else {
                errorMessage.textContent = "Invalid username or password";
            }
        })
        .catch((error) => {
            console.error(error);
            errorMessage.textContent = "An error occurred while logging in";
        });
});