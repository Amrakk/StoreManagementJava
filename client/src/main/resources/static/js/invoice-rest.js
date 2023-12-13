const baseURL = 'http://localhost:8080/api';

const branch = {
    branchId: "B001",
    name: "Ho Chi Minh",
    address: "19 Nguyen Huu Tho",
};

const user = {
    _id: "656c99b0bd41d878152987ad",
    email: "test@gmail.com",
    username: "test",
    password: "$2a$10$ch07yaBUZjMltdYBcLBkgeNJ8PF1.JAJa/RXKMX3x7u5YGXQOY5Qy",
    status: "LOCKED",
    role: "OWNER",
    avatar: "https://i.ibb.co/Yp749vZ/images-3.png",
};

const orderRequest = {
    branchId: branch.branchId,
    uid: user._id,
};

let oid = sessionStorage.getItem('currentOID');

// Search data
const debounce = (func, delay) => {
    let timeoutId;
    return function () {
        const context = this;
        const args = arguments;
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => func.apply(context, args), delay);
    };
};

// Create order with status PENDING
async function createOrder() {
    try {
        let response = await axios.post('http://localhost:8080/api/transactions/create',  orderRequest, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        console.log('Order created successfully:', response.data);
        oid = response.data.data[0].oid;
        sessionStorage.setItem('currentOID', oid)

        $('.card.QA_table').attr('data-id', oid);
    } catch (error) {
        console.error('Error creating order:', error);
        window.location.href = '/error';
    }
};

const handleSearchAndUpdateList = async text => {
    try {
        // API can be changed later...
        let response = await axios.get(
            `http://localhost:8080/api/transactions/search-name?productName=${text}`
        );

        let products = response.data.data;

        if (products == null || products.length === 0) {
            // API can be changed later...
            response = await axios.get(
                `http://localhost:8080/api/transactions/search-barcode?barcode=${text}`
            );
            products = response.data.data;
        }

        displaySearchResults(products);
    } catch (e) {
        console.log(e);
    }
};

const displaySearchResults = results => {
    results.forEach(result => {
        const resultDiv = document.createElement("div");
        resultDiv.setAttribute("data-id", result.pid);
        resultDiv.classList.add(
            "list-group-item-action",
            "list-group-item-light",
            "pt-3"
        );
        resultDiv.innerHTML = `
                                <div class="product-item">
                                    <img src="${result.illustrator}" alt="Product Image" class="product-image">
                                    <div class="product-details">
                                        <h3 class="product-name">${result.name}</h3>
                                        <p class="product-price">$${result.retailPrice}</p>
                                    </div>
                                </div>
                            `;
        searchResultsContainer.appendChild(resultDiv);
    });
};
// End Search Data

async function fetchProductByIdAndAddToLocalStorage(id) {
    try {
        // API can be changed later...
        let response = await axios.get(
            `http://localhost:8080/api/transactions/product/${id}`
        );

        let product = response.data.data;

        let orderedProduct = {
            pid: id,
            oid,
            quantity: 1,
            importedPrice: product.importedPrice,
            retailPrice: product.retailPrice,
        };

        let productsInLocalStorage =
            JSON.parse(localStorage.getItem(`products-${oid}`)) || [];
        let isProductExist = productsInLocalStorage.some(
            (item) => item.pid === orderedProduct.pid
        );

        if (!isProductExist) {
            productsInLocalStorage.push(orderedProduct);
        } else {
            const existingProduct = productsInLocalStorage.find(item => item.pid === id);
            existingProduct.quantity += 1;
        }

        localStorage.setItem(
            `products-${oid}`,
            JSON.stringify(productsInLocalStorage)
        );

        updateTableFromLocalStorage(oid);

        searchInput.value = '';
        searchResultsContainer.innerHTML = '';
    } catch (error) {
        console.error("Error fetching product:", error);
    }
}

function updateTableFromLocalStorage(oid) {
    const tableBody = document.querySelector('.table tbody');
    tableBody.innerHTML = '';

    const productsInLocalStorage = JSON.parse(localStorage.getItem(`products-${oid}`)) || [];

    let total = 0;
    productsInLocalStorage.forEach(async (prd, index) => {
        // API can be changed later...
        let response = await axios.get(
            `http://localhost:8080/api/transactions/product/${prd.pid}`
        );
    
        let product = response.data.data[0];

        const row = document.createElement('tr');
        row.classList.add('data-id', prd.pid);

        const productTotal = (product.retailPrice || 0) * (prd.quantity || 0);
        total += productTotal;

        row.innerHTML = `
            <td class="center">${index + 1}</td>
            <td class="left strong">${product.name}</td>
            <td class="right">$${(product.retailPrice || 0).toFixed(2)}</td>
            <td class="center">${prd.quantity || 0}</td>
            <td class="right">$${productTotal.toFixed(2)}</td>
        `;

        tableBody.appendChild(row);
        wholeTotal.innerHTML = `$${total.toFixed(2)}`;
    });
}

async function getCustomerByPhone(phone) {
    try {
        let response = await axios.get(`${baseURL}/customer?phone=${phone}`);
        
        return response.data.data;
    } catch (error) {
        console.log("Fetching error: " + error);
        window.location.href = '/error';
    }
}

async function updateOrderProduct() {
    console.log(wholeTotal);
    let order = {
        oid,
        customer,
        totalPrice: wholeTotal,
        orderProducts: localStorage.getItem(`products-${oid}`)
    }

    try {
        let response = await axios.post(`${baseURL}/transactions/order/${oid}`, order, {
            headers: {
                'Content-Type' : 'application/json'
            }
        });

        let updatedOrder = response.data.data;
        console.log(updatedOrder);
    } catch (error) {
        console.log(error);
    }
}

let currentDateTag = document.querySelector(".current-date");
const currentDate = new Date().toLocaleDateString("en-GB");
currentDateTag.innerHTML = currentDate;

const searchInput = document.getElementById("search-product-by-name");
const searchResultsContainer = document.getElementById("searchlist");
const wholeTotal = document.querySelector('.whole-total');

if (!oid) {
    $(document).ready(() => {
        createOrder();
    });
}

updateTableFromLocalStorage(oid);

searchInput.addEventListener(
    "input",
    debounce(() => {
        searchResultsContainer.innerHTML = "";
        const searchTerm = searchInput.value.trim();

        if (searchTerm !== '') {
            handleSearchAndUpdateList(searchTerm);
        }
    }, 300)
);

$("#searchlist").on("click", ".list-group-item-action", function() {
    const productId = $(this).data('id');
    
    fetchProductByIdAndAddToLocalStorage(productId);
});

$('.confirm-payment').on('click', async e => {
    let phone = $('#customer-phone-number').val();
    let customer = getCustomerByPhone(phone);

    if (!customer) {
        alert("Invalid customer");    
    }
});

$('.btn-create-customer').on('click', e => {
    alert('HELLO');
});

