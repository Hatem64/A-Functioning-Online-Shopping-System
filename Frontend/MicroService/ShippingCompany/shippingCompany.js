var shippingCompanyId = localStorage.getItem("id");
const deliverButtons = [];
const productTableBody = document.getElementById("product-table-body");
console.log(shippingCompanyId);
fetch(`http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/shippingCompany/orders/notDelivered/${shippingCompanyId}`)
	.then((response) => {
		if (response.ok) {
			return response.json();
		} else {
			throw new Error("Unable to get products.");
		}
	})
	.then((data) => {
		console.log(data);
		// Loop through each order and display its properties
		data.forEach((order) => {
			const row = document.createElement("tr");
			console.log(order);
			row.innerHTML = `
                <td>${order.id}</td>
                <td>${order.clientId.username}</td>
                <td>${order.productId.name}</td>
                <td><button class="deliver-button" data-id="${order.id}">Deliver</button></td>
              `;
			productTableBody.appendChild(row);
			deliverButtons.push(row.querySelector(".deliver-button"));
		});

		deliverButtons.forEach((button) => {
			button.addEventListener("click", () => {
				const orderId = button.getAttribute("data-id");

				fetch(`http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/shippingCompany/submitOrder/${orderId}`, {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
					},
					// body: JSON.stringify({
					// 	// add any additional data to send in the body
					// }),
				})
					.then((response) => {
						if (response.ok) {
							alert("Product delivered successfully!");
							window.location.reload();
						} else {
							alert("deliveryfailed.");
						}
					})
					.catch((error) => {
						console.error(error);
						alert("An error occurred while making the purchase.");
					});
			});
		});
	})
	.catch((error) => {
		// console.error(error);
		console.error(error);
		productTableBody.innerHTML = "<tr><td colspan='3'>An error occurred while getting products.</td></tr>";
	});
