const orderTableBody = document.getElementById("order-table-body");
var sessionId = localStorage.getItem("sessionID");
console.log(sessionId);
fetch(`http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/client/orders`, { method: "GET", headers: { "Content-Type": "application/json", Cookie: localStorage.getItem("sessionID") }, credentials: "include" })
	.then((response) => {
		if (response.ok) {
			return response.json();
		} else {
			throw new Error("Unable to get order history.");
		}
	})
	.then((orders) => {
		// display the orders in the table
		orders.forEach((order) => {
			const row = document.createElement("tr");
			console.log(order);
			row.innerHTML = `
                <td>${order.id}</td>
                <td>${order.productId.name}</td>
                <td>$${order.productId.price.toFixed(2)}</td>
                <td>${order.delivered}</td>
              `;
			orderTableBody.appendChild(row);
		});
	})
	.catch((error) => {
		console.error(error);
		orderTableBody.innerHTML = "<tr><td colspan='4'>An error occurred while getting order history.</td></tr>";
	});

const notificationsButton = document.getElementById("notification-btn");
const notificationsList = document.getElementById("notification-list");

notificationsButton.addEventListener("click", () => {
	fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/client/messages", { method: "GET", headers: { "Content-Type": "application/json", Cookie: localStorage.getItem("sessionID") }, credentials: "include" })
		.then((response) => {
			if (response.ok) {
				return response.json();
			} else {
				throw new Error("Unable to get notifications.");
			}
		})
		.then((messages) => {
			// display notifications in list
			notificationsList.innerHTML = "";
			messages.forEach((message) => {
				const item = document.createElement("li");
				item.textContent = message;
				notificationsList.appendChild(item);
			});
		})
		.catch((error) => {
			console.error(error);
			notificationsList.innerHTML = "<li>An error occurred while getting notifications.</li>";
		});
});




