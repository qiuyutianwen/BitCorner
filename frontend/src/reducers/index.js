import { combineReducers } from "redux";


import auth from './auth'
import order from './order'
import bill from './bill'

const reducers = combineReducers({
  auth: auth,
  order: order,
  bill: bill
});

export const rootReducer = (state, action) => {
  if (action.type === "CLEAR_ALL") {
    state = undefined;
  }

  return reducers(state, action);
};