import { SET_CURRENT_USER, SIGNUP_USER, SIGNUP_USER_FAIL, LOGIN_USER_FAIL, UPDATE_PROFILE, LOGIN_IN, SIGN_UP,FETCH_ALL, UPDATE_BANK, UPDATE_BALANCE, SEND_MESSAGE, MARKET_PLACE, GET_BALANCE, BILL_TRANSACTION, ORDER_TRANSACTION} from '../constants/constant'
import isEmpty from 'lodash/isEmpty'

const initialState = {
    isAuthenticated: false,
    user: null,
    msg: null,
    allUser: null,
    balance: null,
    marketPrice: null,
    allBillTransaction: null,
    allOrderTransaction: null
};

const auth = (state= initialState, action) => {
    switch(action.type){
        case LOGIN_IN:
            return action.payload;
        case SIGN_UP:
            return [...state, action.payload];
        case SET_CURRENT_USER:
            return {
                isAuthenticated: !isEmpty(action.user),
                user: action.user
            };
        case SIGNUP_USER:
            return {
                isAuthenticated: false,
                user: {},
                msg: action.msg
            };
        case LOGIN_USER_FAIL:
            return {
                isAuthenticated: false,
                user: {},
                msg: action.msg
            };
        case SIGNUP_USER_FAIL:
            return {
                isAuthenticated: false,
                user: {},
                msg: action.msg
            };
        case UPDATE_PROFILE:
            return {
                ...state,
                user: action.payload.updated,
                allUser: action.payload.all
            };
            
            // return ;
        case FETCH_ALL:
            return {
                ...state,
                allUser: action.payload
            };
        case UPDATE_BANK: 
        case SEND_MESSAGE:
            return {
                ...state,
                message: action.payload
            }
        case GET_BALANCE: 
            return {
                ...state,
                balance: action.payload
            }
        case UPDATE_BALANCE:
            return {
                ...state,
                balance: action.payload
            }
        case MARKET_PLACE:
            return {
                ...state,
                marketPrice: action.payload
            }
        case BILL_TRANSACTION:
            return {
                ...state,
                allBillTransaction: action.payload
            }
        case ORDER_TRANSACTION:
            return {
                ...state,
                allOrderTransaction: action.payload
            }
        default: return state;
    }
}

export default auth;