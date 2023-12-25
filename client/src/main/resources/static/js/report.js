const baseURL = "http://localhost:8080/api";

function getCookie(name) {
    const cookieValue = document.cookie
        .split('; ')
        .find(row => row.startsWith(name + '='))
        .split('=')[1];

    return cookieValue ? decodeURIComponent(cookieValue) : null;
}

const token = getCookie('token');

if (!token) {
    window.location.href = "/auth/login";
}

const [header, payload, signature] = token.split('.');
const decodedPayload = atob(payload.replace(/_/g, '/').replace(/-/g, '+'));
const claims = JSON.parse(decodedPayload);
const role = claims.role;

if (role !== 'ADMIN' && role !== 'OWNER') {
    $('#profit-table').addClass('d-none');
} else {
    $('#profit-table').removeClass('d-none');
}

async function getProductByPID(pid) {
    try {
        let response = await axios.get(`${baseURL}/transactions/product/${pid}`);

        return response.data.data[0];
    } catch (error) {
        console.log(error);
    }
}

function calculateTotalProducts(orderProducts) {
    return orderProducts ? orderProducts.reduce((total, product) => total + (product.quantity || 0), 0) : 0;
}

async function fetchDataAndPopulateTable(timeline, startDate, endDate) {
    try {
        let url = `${baseURL}/reports/sale-results?timeline=${timeline}`;
        if (startDate && endDate) {
            url += `&startDate=${startDate}&endDate=${endDate}`;
        }

        const response = await axios.get(url);

        $('#sales-results-table tbody').empty();

        const orders = response.data.data[0].orders;
        orders.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));

        orders.forEach(order => {
            const formattedDate = new Intl.DateTimeFormat('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric',
                timeZone: 'UTC'
            }).format(new Date(order.createdAt));

            const formattedTotalPrice = new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD'
            }).format(order.orderStatus === 'COMPLETED' ? order.totalPrice : 0);

            const newRow = `
                <tr data-id="${order.oid}">
                    <td>${formattedDate}</td>
                    <td>${order.orderStatus === 'COMPLETED' ? calculateTotalProducts(order.orderProducts) : 0}</td>
                    <td>${order.orderStatus === 'COMPLETED' ? formattedTotalPrice : 'N/A'}</td>
                    <td>${order.orderStatus}</td>
                </tr>
            `;
            $('#sales-results-table tbody').append(newRow);
        });

        const allOrderProducts = orders.flatMap(order => order.orderStatus === 'COMPLETED' ? order.orderProducts : []);
        const productQuantityMap = new Map();
        
        allOrderProducts.forEach(orderProduct => {
            const productId = orderProduct.pid;
            const quantity = orderProduct.quantity;
        
            if (productQuantityMap.has(productId)) {
                productQuantityMap.set(productId, productQuantityMap.get(productId) + quantity);
            } else {
                productQuantityMap.set(productId, quantity);
            }
        });

        const productQuantityArray = Array.from(productQuantityMap, ([productId, quantity]) => ({ productId, quantity }));        
        productQuantityArray.sort((a, b) => b.quantity - a.quantity);
        
        const top5Products = productQuantityArray.slice(0, 5);

        const productDetailsPromises = top5Products.map(async product => {
            const productDetails = await getProductByPID(product.productId);
            return {
                name: productDetails ? productDetails.name : 'N/A',
                amountSold: product.quantity
            };
        });

        const topProductDetails = await Promise.all(productDetailsPromises);

        const topProductTableBody = $('#top-product-table tbody');
        topProductTableBody.empty();

        topProductDetails.forEach((product, index) => {
            const newRow = `
                <tr>
                    <td>${index + 1}</td>
                    <td>${product.name}</td>
                    <td>${product.amountSold}</td>
                </tr>
            `;
            topProductTableBody.append(newRow);
        });

        const userRevenueMap = new Map();

        orders.forEach(order => {
            const userName = order.user.username;
            const userEmail = order.user.email;
            const totalPrice = parseFloat(order.totalPrice);
        
            if (userRevenueMap.has(userEmail)) {
                const existingEntry = userRevenueMap.get(userEmail);
                existingEntry.totalPrice += totalPrice;
                existingEntry.userName = userName;
        
                userRevenueMap.set(userEmail, existingEntry);
            } else {
                userRevenueMap.set(userEmail, {
                    userName,
                    userEmail,
                    totalPrice
                });
            }
        });

        const userRevenueArray = Array.from(userRevenueMap.values());
        console.log(userRevenueArray);

        userRevenueArray.sort((a, b) => b.totalRevenue - a.totalRevenue);

        const top5Employees = userRevenueArray.slice(0, 5);

        const topEmployeeTableBody = $('#top-employee-table tbody');
        topEmployeeTableBody.empty();
        
        top5Employees.forEach((employee, index) => {
            const newRow = `
                <tr>
                    <td>${index + 1}</td>
                    <td>${employee.userName}</td>
                    <td>${employee.userEmail}</td>
                    <td>${employee.totalPrice.toFixed(2)}</td>
                </tr>
            `;
            topEmployeeTableBody.append(newRow);
        });
        
        const totalRevenue = orders.reduce((acc, order) => {
            if (!order.orderProducts || order.orderProducts.length === 0 || order.orderStatus !== 'COMPLETED') {
                return acc;
            }
        
            const orderCost = order.orderProducts.reduce((orderAcc, product) => {
                return orderAcc + (product.retailPrice * product.quantity);
            }, 0);
        
            return acc + orderCost;
        }, 0);

        const totalCost = orders.reduce((acc, order) => {
            // Kiểm tra nếu orderProducts không tồn tại hoặc không có sản phẩm nào
            if (!order.orderProducts || order.orderProducts.length === 0 || order.orderStatus !== 'COMPLETED') {
                return acc;
            }
        
            const orderCost = order.orderProducts.reduce((orderAcc, product) => {
                return orderAcc + (product.importPrice * product.quantity);
            }, 0);
        
            return acc + orderCost;
        }, 0);
        const profit = totalRevenue - totalCost;

        $('#profit-table tbody').empty();
        const newRow = `
                <tr>
                    <td>$${totalRevenue.toFixed(2)}</td>
                    <td>$${totalCost.toFixed(2)}</td>
                    <td>$${profit.toFixed(2)}</td>
                </tr>
            `;
        $('#profit-table tbody').append(newRow);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

$(document).ready(async () => {
    await fetchDataAndPopulateTable('today');

    $('#date-range-select').on('change', function () {
        let selectedTimeline = $(this).val();

        if (selectedTimeline === 'custom') {
            $('#customDateRange').removeClass('d-none');
        } else {
            $('#customDateRange').addClass('d-none');
            fetchDataAndPopulateTable(selectedTimeline, "", "");
        }
    });

    $('#search-custom-date').on('click', function () {
        const startDate = new Date($('#startDate').val() + 'T00:00:00Z').toISOString();
        const endDate = new Date($('#endDate').val() + 'T23:59:59Z').toISOString();
    
        fetchDataAndPopulateTable('custom', startDate, endDate);
    });        

    $('#sales-results-table tbody').on('click', 'tr', function() {
        $(this).addClass('table-active');

        const orderId = $(this).data('id');
        window.location.href = `/orders/${orderId}`;
    });
});
