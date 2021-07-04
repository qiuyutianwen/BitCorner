import { SEND_BILL, UPDATE_BILL, GET_BILL } from "../constants/constant";
import * as api from "../api/index";

export const getBills = (userId) => async (dispatch) => {
  try {
    console.log("inside getBills", userId);
    const { data } = await api.fetchBill(userId);
    console.log("getBills", data);
    await dispatch({ type: GET_BILL, payload: data });
  } catch (error) {
    console.log(error.message);
  }
};

export const sendToBill = (billInfo) => async (dispatch) => {
  try {
    console.log("inside sendBill", billInfo);
    const { data } = await api.sendBill(billInfo);
    console.log("sendBill", data);
    await dispatch({ type: SEND_BILL, payload: data });
    alert("Success!");
  } catch (error) {
    if (error.response) {
      alert(error.response.data);
    }

    console.log(error.message);
  }
};

export const updateToBill = (billInfo) => async (dispatch) => {
  try {
    console.log("inside updateToBill", billInfo);
    const { data } = await api.updateBill(billInfo);
    console.log("updateToBill", data);
    await dispatch({ type: UPDATE_BILL, payload: data });
  } catch (error) {
    alert(error.response.data);
    console.log(error.message);
  }
};

export const cancelToBill = (billInfo) => async (dispatch) => {
  try {
    console.log("inside cancelToBill", billInfo);
    const { data } = await api.cancelBill(billInfo);
    console.log("cancelToBill", data);
    await dispatch({ type: UPDATE_BILL, payload: data });
  } catch (error) {
    alert(error.response.data);
    console.log(error.message);
  }
};

export const rejectToBill = (billInfo) => async (dispatch) => {
  try {
    console.log("inside reject", billInfo);
    const { data } = await api.rejectBill(billInfo);
    console.log("reject", data);
    await dispatch({ type: UPDATE_BILL, payload: data });
  } catch (error) {
    alert(error.response.data);
    console.log(error.message);
  }
};

export const payABill = (
  billId,
  paidType,
  paidAmount,
  transaction_fee
) => async (dispatch) => {
  try {
    console.log(
      "inside payBill",
      billId,
      paidType,
      paidAmount,
      transaction_fee
    );
    const { data } = await api.payBill(
      billId,
      paidType,
      paidAmount,
      transaction_fee
    );
    console.log("payBill", data);
    await dispatch({ type: UPDATE_BILL, payload: data });
  } catch (error) {
    alert(error.response.data);
    console.log(error.message);
  }
};
