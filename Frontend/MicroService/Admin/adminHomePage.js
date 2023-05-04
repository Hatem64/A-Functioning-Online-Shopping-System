const createProductCompanyAccountsForm = document.getElementById('create-product-company-accounts-form');
const createShippingCompaniesForm = document.getElementById('create-shipping-companies-form');

createProductCompanyAccountsForm.addEventListener('submit', (event) => {
    event.preventDefault();

    const companyNames = document.getElementById('company-names').value.split(',');
    const passwordLength = document.getElementById('password-length').value;

    const accountCreationRequests = {};
    for (const companyName of companyNames) {
        accountCreationRequests[companyName.trim()] = parseInt(passwordLength);
    }

    fetch('http://localhost:11780/Server_2-1.0-SNAPSHOT/api/admin/createProductCompanyAccount', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(accountCreationRequests)
    })
        .then(response => {
            if (response.status === 204) {
                createProductCompanyAccountsForm.reset();
                alert('Accounts created successfully!');
            } else {
                alert('Failed to create accounts');
            }
        })
        .catch(error => {
            console.error(error);
            alert('An error occurred while creating accounts');
        });
});

createShippingCompaniesForm.addEventListener('submit', (event) => {
    event.preventDefault();

    const companyName = document.getElementById('company-name').value;
    const geographicCoverage = document.getElementById('geographic-coverage').value;

    const company = {
        [companyName.trim()]: geographicCoverage.trim()
    };

    fetch('http://localhost:11780/Server_2-1.0-SNAPSHOT/api/admin/createShippingCompany', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(company)
    })
        .then(response => {
            if (response.status === 204) {
                createShippingCompaniesForm.reset();
                alert('Company created successfully!');
            } else {
                alert('Failed to create company');
            }
        })
        .catch(error => {
            console.error(error);
            alert('An error occurred while creating company');
        });
});

const shippingCompaniesButton = document.querySelector('#list-shipping-companies-btn');
const shippingCompaniesContainer = document.querySelector('#shipping-companies-container');
let shippingCompaniesVisible = false;

shippingCompaniesButton.addEventListener('click', () => {
    if (shippingCompaniesVisible) {
        shippingCompaniesContainer.style.display = 'none';
        shippingCompaniesVisible = false;
    } else {
        fetch('http://localhost:11780/Server_2-1.0-SNAPSHOT/api/admin/listShippingCompanies')
            .then(response => response.json())
            .then(data => {
                shippingCompaniesContainer.innerHTML = '';
                for (const [key, value] of Object.entries(data)) {
                    shippingCompaniesContainer.innerHTML += `<p>${key}: ${value}</p>`;
                }
                shippingCompaniesContainer.style.display = 'block';
                shippingCompaniesVisible = true;
            })
            .catch(error => console.error(error));
    }
});

const productCompaniesButton = document.querySelector('#list-selling-companies-btn');
const productCompaniesContainer = document.querySelector('#selling-companies-container');
let productCompaniesVisible = false;

productCompaniesButton.addEventListener('click', () => {
    if (productCompaniesVisible) {
        productCompaniesContainer.style.display = 'none';
        productCompaniesVisible = false;
    } else {
        fetch('http://localhost:11780/Server_2-1.0-SNAPSHOT/api/admin/listSellingCompanies')
            .then(response => response.json())
            .then(data => {
                productCompaniesContainer.innerHTML = '';
                for (const [key, value] of Object.entries(data)) {
                    productCompaniesContainer.innerHTML += `<p>${key}: ${value}</p>`;
                }
                productCompaniesContainer.style.display = 'block';
                productCompaniesVisible = true;
            })
            .catch(error => console.error(error));
    }
});

shippingCompaniesContainer.style.minHeight = '50px';
productCompaniesContainer.style.minHeight = '50px';

const listClientsButton = document.getElementById('list-clients-button');
const clientList = document.getElementById('client-list');
let isListVisible = false;

listClientsButton.addEventListener('click', () => {
    if (isListVisible) {
        clientList.style.display = 'none';
        listClientsButton.innerText = 'List Users';
        isListVisible = false;
    } else {
        fetch('http://localhost:11780/Server_2-1.0-SNAPSHOT/api/admin/listClients')
            .then(response => response.json())
            .then(clients => {
                const table = document.createElement('table');
                const header = table.insertRow(0);
                const nameHeader = header.insertCell(0);
                const passwordHeader = header.insertCell(1);
                const phoneNumberHeader = header.insertCell(2);
                const addressHeader = header.insertCell(3);

                nameHeader.innerHTML = '<b>Username</b>';
                passwordHeader.innerHTML = '<b>Password</b>';
                phoneNumberHeader.innerHTML = '<b>Phone Number</b>';
                addressHeader.innerHTML = '<b>Address</b>';

                clients.forEach(client => {
                    const row = table.insertRow(-1);

                    const usernameCell = row.insertCell(0);
                    const passwordCell = row.insertCell(1);
                    const phoneCell = row.insertCell(2);
                    const addressCell = row.insertCell(3);

                    usernameCell.innerText = client.username;
                    passwordCell.innerText = client.password;
                    phoneCell.innerText = client.phonenumber;
                    addressCell.innerText = client.address;
                });

                clientList.innerHTML = '';
                clientList.appendChild(table);
            })
            .catch(error => {
                console.error(error);
                alert('An error occurred while retrieving clients');
            });

        listClientsButton.innerText = 'Hide Users';
        clientList.style.display = 'block';
        isListVisible = true;
    }
});