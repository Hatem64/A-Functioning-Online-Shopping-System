const orderTableBody = document.getElementById("order-table-body");
//var myVar = localStorage.getItem("myVar");
var sessionId = localStorage.getItem("sessionID");
console.log(sessionId);
fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/sellingCompany/orders", { method: "GET", headers: { "Content-Type": "application/json", Cookie: localStorage.getItem("sessionID") }, credentials: "include" })
	.then((response) => {
		if (response.ok) {
			return response.json();
		} else {
			throw new Error("Unable to get the company products.");
		}
	})
	.then((products) => {
		// display the products in the table
		products.forEach((product) => {
			const row = document.createElement("tr");
			console.log(product);
			row.innerHTML = `
                <td>${product.productId.name}</td>
                <td>${product.productId.price}</td>
                <td>${product.clientId.username}</td>
                <td>${product.clientId.address}</td>
                <td>${product.clientId.phonenumber}</td>
                <td>${product.shippingCompId.name}</td>
                
              `;
			orderTableBody.appendChild(row);
		});
	})
	.catch((error) => {
		console.error(error);
		orderTableBody.innerHTML = "<tr><td colspan='4'>An error occurred while getting products.</td></tr>";
	});
