const baseURL = "http://localhost:8080/api";
const urlParts = window.location.pathname.split('/');
const oid = urlParts[urlParts.length - 1];

const getOrderByOid = async oid => {
    try {
        const response = await axios.get(`${baseURL}/transactions/orders/${oid}`);

        if (response.status === 200 && response.data.data && response.data.data.length > 0) {
            return response.data.data[0];
        } else {
            console.log("Failed to fetch order or order not found");
            return null;
        }
    } catch (error) {
        console.error("Error fetching order:", error);
        return null;
    }
};

const getProductByPid = async pid => {
    try {
        const response = await axios.get(`${baseURL}/transactions/product/${pid}`);
        return response.data.data[0];
    } catch (error) {
        console.error("Error fetching product details:", error);
        return null;
    }
};

const placeOrder = async (oid) => {
    try {
        const response = await axios.get(`${baseURL}/transactions/orders/${oid}/cash/success`);
        console.log(response);

        if (response.status === 200) {
            return response.data.message;
        } else {
            console.error('Failed to place order:', response.status);
            return null;
        }
    } catch (error) {
        console.error('Error placing order:', error);
        return null;
    }
};

let currentOrder;
(async () => {
    currentOrder = await getOrderByOid(oid);

    if (currentOrder) {
        console.log("Current Order details:", currentOrder);

        document.getElementById("oidValue").textContent = currentOrder.oid;
        document.getElementById("customerNameValue").textContent = currentOrder.customer.name;
        document.getElementById("userNameValue").textContent = currentOrder.user.username;
        document.getElementById("dateCreatedValue").textContent = formatDate(currentOrder.createdAt);

        const productTableBody = document.getElementById("product-table-body");
        currentOrder.orderProducts.forEach(async product => {
            const productDetails = await getProductByPid(product.pid);
            console.log(productDetails);

            const row = document.createElement("tr");

            const productCell = document.createElement("td");
            const productImage = document.createElement("img");
            productImage.src = productDetails.illustrator;
            productImage.alt = "Product Image";
            productImage.height = 52;
            const productName = document.createElement("p");
            productName.className = "d-inline-block align-middle mb-0 product-name f_s_16 f_w_600 color_theme2";
            productName.textContent = productDetails.name;
            productCell.appendChild(productImage);
            productCell.appendChild(productName);

            const quantityCell = document.createElement("td");
            quantityCell.textContent = product.quantity;

            const totalCell = document.createElement("td");
            totalCell.textContent = formatCurrency(product.quantity * product.retailPrice);

            row.appendChild(productCell);
            row.appendChild(quantityCell);
            row.appendChild(totalCell);

            productTableBody.appendChild(row);
        });

        document.getElementById("total-payment").textContent = formatCurrency(currentOrder.totalPrice);

    } else {
        console.log("Failed to fetch current order or order not found");
        window.location.href = '/error';
    }
})();

const formatDate = dateString => {
    const date = new Date(dateString);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
};

const formatCurrency = amount => `$${amount.toFixed(2)}`;

$("#cash-received").on("input", function() {
    let totalPrice = parseFloat($('#total-payment').text().replace('$', ''));
    let cashReceived = parseFloat($(this).val());
    
    $('#cash-return').val((cashReceived - totalPrice).toFixed(2));
});

$('#place-order').on('click', async () => {
    try {
        const cashReceived = parseFloat($('#cash-received').val());
        const totalPrice = parseFloat($('#total-payment').text().replace('$', ''));

        if (isNaN(cashReceived) || cashReceived < totalPrice) {
            alert('Cash received must be equal to or greater than the total amount.');
            return;
        }

        $('#place-order').prop('disabled', true);

        const orderPlaced = await placeOrder(oid);
        console.log(orderPlaced);

        if (orderPlaced) {
            alert(orderPlaced);
            window.location.href = `/Home`;
        } else {
            alert("FAILED");
        }

    } catch (error) {
        console.error('Error placing order:', error);
    } finally {
        $('#place-order').prop('disabled', false);
    }
});
