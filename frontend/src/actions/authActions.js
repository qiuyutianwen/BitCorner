import {
	SET_CURRENT_USER,
	LOGIN_USER_FAIL,
	SIGNUP_USER_FAIL,
	SIGNUP_USER,
	UPDATE_PROFILE,
	FETCH_ALL,
	CLEARALL,
	UPDATE_BANK,
	UPDATE_BALANCE,
	GET_BALANCE,
	SEND_MESSAGE,
	MARKET_PLACE,
	BILL_TRANSACTION,
	ORDER_TRANSACTION
} from '../constants/constant';
import setAuthorizationToken from '../utils/validations/setAuthorizationToken';
import * as api from '../api/index';
import { getFromOrders } from './orderActions';
import jwtEncode from 'jwt-encode'
export const loginUser = (user) => async (dispatch) => {
	try {
		console.log('userData', user);
			api.loginUser(user).then(
		response => { 
			if(response.data.status === 201){
				alert(response.data);
			}else{
				localStorage.setItem('header', response.headers.authorization)
				const token = jwtEncode(response.data, "cmpe275")
				localStorage.setItem('jwtToken', token);
				localStorage.setItem('id', response.data.id); 
				console.log("response",response);
				dispatch(setCurrentUser(response.data));
				dispatch(getFromOrders(response.data.username));
			}
		}).catch(error => {
			alert(error.response.data);
		})
	} catch (error) {
		console.log(error);
		alert(
			'Error occured!'
		);
	}
};

export const setCurrentUser = (user) => {
	return {
		type: SET_CURRENT_USER,
		user,
	};
};

export const clearAll = (info) => {
	return {
		type: CLEARALL,
		payload: info,
	};
};

export const logout = (history) => {
	return (dispatch) => {
		localStorage.removeItem('jwtToken');
		setAuthorizationToken(false);
		dispatch(clearAll({}));
		history.push('/');
	};
};

export const loginFail = (msg) => {
	return {
		type: LOGIN_USER_FAIL,
		msg,
	};
};

export const signupUser = (user, history) => async (dispatch) => {
	try {
		const { data} = await api.signupUser(user);
		console.log('sign up data:', data);
		dispatch(signUpUser({ success: 'Signup successfully!' }));
		history.push('/');
	} catch (error) {
		alert("Signup fail with duplicate email or nickname")
	}
};

export const signUpUserFail = (msg) => {
	return {
		type: SIGNUP_USER_FAIL,
		msg,
	};
};

export const signUpUser = (msg) => {
	return {
		type: SIGNUP_USER,
		msg,
	};
};

export const update = (userData, userID) => async (dispatch) => {
	try {
		console.log('userData', userData);
		const { data } = await api.updateUser(userData, userID);
		console.log('update', data);
		dispatch({ type: UPDATE_PROFILE, payload: data });
	} catch (error) {
		alert(error.response.data);
		console.log(error.message);
	}
};

export const addBank = (userBank, history) => async (dispatch) => {
	try {
		console.log('userBank', userBank);
		const { data } = await api.addBankInfo(userBank);
		console.log('update', data);
		await dispatch({ type: UPDATE_BANK, payload: data });
		alert("Add Bank Successfully!")
		history.push('/dashboard/board')
	} catch (error) {
		alert(error.response.data);
		console.log(error.message);
	}
};

export const getUsers = () => async (dispatch) => {
	try {
		const { data } = await api.fetchUsers();

		dispatch({ type: FETCH_ALL, payload: data });
	} catch (error) {
		console.log(error.message);
	}
};

export const updateBalance = (balanceInfo) => async (dispatch) => {
	try {
		const { data, status } = await api.changeBalance(balanceInfo);
		console.log("update balance", data, status);
		dispatch({ type: UPDATE_BALANCE, payload: data });
	} catch (error) {
		alert("You do not have enough balance to withdraw.")
		console.log(error.message);
	}
};

export const getFromBalance = (user) => async (dispatch) => {
	try {

		console.log('inside getFromBalance', user);
		const { data } = await api.getBalance(user);
		console.log('getFromBalance', data);
		dispatch({ type: GET_BALANCE, payload: data });
	} catch (error) {
		console.log(error.message);
	}
};

export const sendToMessage = (messageInfo) => async (dispatch) => {
	try {
		console.log('inside sendToMessage', messageInfo);
		const { data } = await api.sendMessage(messageInfo);
		console.log('sendToMessage', data);
		dispatch({ type: SEND_MESSAGE, payload: data });
	} catch (error) {
		console.log(error.message);
	}
};

export const getFromMarketPlace = () => async (dispatch) => {
	try {
		console.log('inside getFromMarketPlace', );
		const { data } = await api.getMarketPlace();
		console.log('getFromMarketPlace', data);
		dispatch({ type: MARKET_PLACE, payload: data });
	} catch (error) {
		console.log(error.message);
	}
};

export const getFromBillTransaction = () => async (dispatch) => {
	try {
		console.log('inside getFromBillTransaction', );
		const { data } = await api.fetchBillTransaction();
		console.log('getFromBillTransaction', data);
		dispatch({ type: BILL_TRANSACTION, payload: data });
	} catch (error) {
		console.log(error.message);
	}
};

export const getFromOrderTransaction = () => async (dispatch) => {
	try {
		console.log('inside getFromOrderTransaction', );
		const { data } = await api.fetchOrderTransaction();
		console.log('getFromOrderTransaction', data);
		dispatch({ type: ORDER_TRANSACTION, payload: data });
	} catch (error) {
		console.log(error.message);
	}
};



