import {
	SEND_ORDER,
    UPDATE_ORDER,
	GET_ORDERS,
	DELETE_ORDER,
	ALL_ORDERS,
} from '../constants/constant';
import * as api from '../api/index';

export const send = (orderInfo) => async (dispatch) => {
	try {
		console.log('inside send', orderInfo);
		const { data } = await api.sendOrder(orderInfo);
		console.log('send', data);
		await dispatch({ type: SEND_ORDER, payload: data });
		alert("Place a new order successfully")
	} catch (error) {
		alert(error.response.data);
		console.log("error", error.response.data);
	}
};

export const sendLimit = (orderInfo) => async (dispatch) => {
	try {
		console.log('inside send', orderInfo);
		const { data } = await api.sendLimitOrder(orderInfo);
		console.log('send', data);
		await dispatch({ type: SEND_ORDER, payload: data });
		alert("Place a new order successfully")
	} catch (error) {
		alert(error.response.data);
		console.log(error.message);
	}
};


export const updateToOrder = (orderInfo) => async (dispatch) => {
	try {
		console.log('inside updateToOrder', orderInfo);
		console.log("orderInfo",orderInfo);
		const { data } = await api.updateOrder(orderInfo);
		console.log('updateToOrder', data);
		await dispatch({ type: UPDATE_ORDER, payload: data });
		alert("Update order successfully!");
	} catch (error) {
		alert(error.response.data);
		console.log(error.message);
	}
};

export const getFromOrders = (username) => async (dispatch) => {
	try {
		console.log('inside getFromOrders', username);
		const { data } = await api.getOrders(username);
		console.log('getFromOrders', data);
		await dispatch({ type: GET_ORDERS, payload: data });
	} catch (error) {
		alert(error.response.data);
		console.log(error.message);
	}
};

export const deleteFromOrders = (orderId) => async (dispatch) => {
	try {
		console.log('inside deleteFromOrders', orderId);
		const { data } = await api.deleteOrder(orderId);
		console.log('deleteFromOrders', data);
		await dispatch({ type: DELETE_ORDER, payload: data });
	} catch (error) {
		alert(error.response.data);
		console.log(error.message);
	}
};

export const allFromOrders = () => async (dispatch) => {
	try {
		console.log('inside allFromOrders', );
		const { data } = await api.getAllOrders();
		console.log('allFromOrders', data);
		await dispatch({ type: ALL_ORDERS, payload: data });
	} catch (error) {
		alert(error.response.data);
		console.log(error.message);
	}
};

