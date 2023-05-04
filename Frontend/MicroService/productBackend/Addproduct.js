/*const addproduct = document.getElementById('addproduct');
addproduct.addEventListener("submit", (event) =>{
    event.preventDefault();

    /*const productname = document.getElementById("productname").value;//addproduct.productname.value;
    const productprice = document.getElementById("productprice").value;
    //const sellingCompanyID = addproduct.sellingCompanyID.value;
    const addproductRequest = {productname,productprice};
    const formData =  new FormData(addproduct)

    fetch("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/products/addProduct/1" , 
    {
        method:"POST",
        //headers: { "Content-Type":"application/json"},
        //body: JSON.stringify(addproductRequest)
        body:formData
    })
    .then((reponse) => {
        if (reponse.ok)
        {
            //alert('Product added successfully');
            return response.text();
        }throw new Error('Network response was not ok.');
        
        //  else{
        //     alert('Falied to add the product');
        // }
    })
    then(data => {
      console.log(data); // logs the response body to the console
    })
    .catch(error =>{console.error(error);
    alert('An error occurred while adding the product');
});
    
});

/*const addproducts = document.getElementById("addproduct")
addproducts.addEventListener("submit", (event) =>{
    event.preventDefault();
  const productsname = getElementById("productname").value;;//addproduct.productname.value;
  const productsprice = getElementById("productprice").value;

var xhr = new XMLHttpRequest();
xhr.open("POST", "http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/products/addProduct/1", true);
xhr.setRequestHeader("Content-Type", "application/json");
xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var response = JSON.parse(xhr.responseText);
      console.log(response);
    }
  };
  
  var data = { name: "productname", price: productsprice };
  var jsonData = JSON.stringify(data); 
  xhr.send(jsonData);
});*/

//View Product Buttom

/*const listProductsButton = document.getElementById('view-prpduct-button');
const productList = document.getElementById('product-list');

 listProductsButton.addEventListener('submit', () => {
  fetch('http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/products/productsList')
      .then(response => response.json())
      .then(clients => {
          const table = document.createElement('table');
          const header = table.insertRow(0);
          const IdHeader = header.insertCell(0);
          const nameHeader = header.insertCell(1);
          const piceHeader = header.insertCell(2);
          const companyIDHeader = header.insertCell(3);
          IdHeader.innerHTML = '<b>UserName</b>';
          nameHeader.innerHTML = '<b>password</b>';
          piceHeader.innerHTML = '<b>PhoneNumber</b>';
          companyIDHeader.innerHTML = '<b>Address</b>';

          clients.forEach(client => {
              const row = table.insertRow(-1);
              const idCell = row.insertCell(0);
              const nameCell = row.insertCell(1);
              const priceCell = row.insertCell(2);
              const company_IDCell = row.insertCell(3);

              idCell.innerText = client.id;
              nameCell.innerText = client.name;
              priceCell.innerText = client.proce;
              company_IDCell.innerText = client.company_ID;
          });

          productList.innerHTML = '';
          productList.appendChild(table);
      })
      .catch(error => {
          console.error(error);
          alert('An error occurred while retrieving products');
      });
});

//list products
const button = document.querySelector('button'); // replace 'button' with the ID or class of your HTML button element
button.addEventListener('click', () => {
  fetch('http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/products/productsList')
    .then(response => {
      if (response.ok) {
        return response.json(); // returns the response body as a JSON object
      }
      throw new Error('Network response was not ok.');
    })
    .then(data => {
      console.log(data); // logs the response data to the console
    })
    .catch(error => {
      console.error('There was a problem with the fetch operation:', error);
    });
});*/
// const form = document.getElementById('register-form');
// const message = document.getElementById('message');
// var myVar = localStorage.getItem("myVar");

// form.addEventListener('submit', (e) => {
//     e.preventDefault();
//     const username = form.username.value;
//     const price = form.price.value;

//     fetch(`http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/products/addProduct/${Number(myVar)}`, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({
//             username,
//             price

//         })
//     })
//          .then(response => response.text())
//         .then(data => {
//             message.textContent = data;
//             if (data === 'Success') {
//                 form.reset();
//                 window.location.href = "Viewpeoduct.html";
//             }
//         })
//         .catch(error => console.error(error));

// });

const addProductForm = document.getElementById("add-product-form");

addProductForm.addEventListener("submit", async (event) => {
	event.preventDefault();

	// Get the values of the input fields
	const name = addProductForm.name.value;
	const price = addProductForm.price.value;

	// Create an object with the data
	const productData = { name, price };

	// Make the HTTP POST request
	fetch(`http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/sellingCompany/addProduct/${name}/${price}`, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			Cookie: localStorage.getItem("sessionID"),
		},
		body: JSON.stringify(productData),
		credentials: "include",
	})
		.then((response) => response.text())
		.then((data) => {
			message.textContent = data;
			if (data === "Product added successfully") {
				addProductForm.reset();
				window.location.href = "ViewProduct.html";
			}
			// console.log(data);
		})
		.catch((error) => {
			console.error(error);
			// Handle any errors that occur during the request
		});
});
