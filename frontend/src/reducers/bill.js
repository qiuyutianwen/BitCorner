import { 
    SEND_BILL,
    UPDATE_BILL,
    GET_BILL
} from '../constants/constant'

const initialState = {
    msg: null,
    allBill: null,
};

const bill = (state= initialState, action) => {
    switch(action.type){
        case SEND_BILL:
            return {
                ...state,
                allBill: [...state.allBill, action.payload]
            }
        case UPDATE_BILL:
            return {
                ...state,
                allBill: state.allBill.map((o) => (o.id === action.payload.id ? action.payload : o)),
            }
        case GET_BILL: 
            return {
                ...state,
                allBill: action.payload
            }
        default: return state;
    }
}

export default bill;