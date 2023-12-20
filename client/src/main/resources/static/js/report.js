const baseURL = "http://localhost:8080/api";

function calculateTotalProducts(orderProducts) {
    return orderProducts ? orderProducts.length : 0;
}

$(document).ready(function () {
    async function fetchDataAndPopulateTable(timeline, startDate, endDate) {
        try {
            let url = `${baseURL}/reports/sale-results?timeline=${timeline}`;
            if (startDate && endDate) {
                url += `&startDate=${startDate}&endDate=${endDate}`;
            }

            const response = await axios.get(url);
    
            $('#sales-results-table tbody').empty();
    
            const orders = response.data.data[0].orders;

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

            console.log(orders);
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
                        <td>$${totalRevenue}</td>
                        <td>$${totalCost}</td>
                        <td>$${profit}</td>
                    </tr>
                `;
            $('#profit-table tbody').append(newRow);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }    

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
    
        console.log(startDate);
        console.log(endDate);
    
        fetchDataAndPopulateTable('custom', startDate, endDate);
    });        

    fetchDataAndPopulateTable('today');

    $('#sales-results-table tbody').on('click', 'tr', function() {
        $(this).addClass('table-active');

        const orderId = $(this).data('id');
        window.location.href = `/orders/${orderId}`;
    });
});

