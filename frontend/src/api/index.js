import axios from 'axios';
const API = axios.create({ baseURL: 'http://54.219.138.35:9000' });

export const fetchUsers = () => API.get('/users');
export const signupUser = (user) => API.post('/accounts/signup', user);
export const loginUser = (user) => API.post('/accounts/login', user);
export const updateUser = (newUser, userID) =>
	API.patch(`/accounts/update/${userID}`, newUser);
export const addBankInfo = (bank) => API.post('/accounts/bank', bank);
export const changeBalance = (balance) => API.post('accounts/balance/updatebalance', balance);
export const getBalance = (user) => API.get(`/accounts/balances?username=${user}`);
export const getMarketPlace = () => API.get('/allprice');




export const sendMessage = (message) => API.post('/accounts/message', message);

export const sendOrder = (order) => API.post(`/order?username=${order.username}&type=${order.orderType}&currency=${order.currency}&btc_quantity=${order.amount}&transaction_type=${order.transactionType}`);
export const sendLimitOrder = (order) => API.post(`/order?username=${order.username}&type=${order.orderType}&transaction_type=${order.transactionType}&currency=${order.currency}&price=${order.price}&btc_quantity=${order.amount}`);

// export const updateOrder = (order, user) => API.post('/accounts/updteOrder', order, user);
export const updateOrder = (order) => API.put('/accounts/updateOrder', order)

export const getOrders = (username) => API.get(`/order?username=${username}`);
export const getAllOrders = () => API.get(`/order/all`);

export const deleteOrder = (orderId) => API.get(`/order/delete?orderId=${orderId}`);

export const sendBill = (billInfo) => API.post('/bill/send', billInfo);
export const updateBill = (billInfo) => API.post('/bill/update', billInfo);
export const cancelBill = (billInfo) => API.post('/bill/cancel', billInfo);
export const rejectBill = (billInfo) => API.post('/bill/reject', billInfo);
export const fetchBill = (userId) => API.get(`bill?userid=${userId}`)
export const getOneBill = (billId) => API.get(`bill/id?billId=${billId}`)
export const payBill = (billId, paidType, paidAmount, transaction_fee) =>
  API.post(
    `bill/pay?billId=${billId}&paidType=${paidType}&paidAmount=${paidAmount}&transaction_fee=${transaction_fee}`
  );

export const fetchOrderTransaction = () => API.get('/order/transaction')
export const fetchBillTransaction = () => API.get('bill_transaction/all')
