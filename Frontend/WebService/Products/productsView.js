const productTableBody = document.getElementById("product-table-body");
const buyButtons = [];
var sessionId = localStorage.getItem("sessionID");
// console.log(sessionId);
// fetch all available products
fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/products/productsList")
	.then((response) => {
		if (response.ok) {
			return response.json();
		} else {
			throw new Error("Unable to get products.");
		}
	})
	.then((products) => {
		// display the products in the table
		products.forEach((product) => {
			const row = document.createElement("tr");
			row.innerHTML = `
                <td>${product.name}</td>
                <td>${product.sellingCompId.name}</td>
                <td>$${product.price.toFixed(2)}</td>
                <td><button class="buy-button" id="${product.id}">Buy</button></td>
            `;
			productTableBody.appendChild(row);
			buyButtons.push(row.querySelector(".buy-button"));
		});

		// add event listener to each Buy button
		buyButtons.forEach((button) => {
			button.addEventListener("click", () => {
				const productId = event.target.id;
				// var sessionId = localStorage.getItem("sessionID");

				fetch(`http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/client/buy/${productId}`, {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						"Cookie": localStorage.getItem("sessionID")
					},
					credentials: "include"
					// body: JSON.stringify({
					// 	// add any additional data to send in the body
					// }),
				})
					.then(response => response.text())
					.then((data) => {
						console.log(data);
						if (data === "Order placed") {
							alert("Purchase successful!");
							location.reload();
						} else {
							alert("Purchase failed.");
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
		console.error(error);
		productTableBody.innerHTML = "<tr><td colspan='3'>An error occurred while getting products.</td></tr>";
	});
