import { 
    SEND_ORDER,
    UPDATE_ORDER,
    GET_ORDERS,
    ALL_ORDERS
} from '../constants/constant'

const initialState = {
    msg: null,
    allOrder: null,
    admin: null
};

const order = (state= initialState, action) => {
    switch(action.type){
        case SEND_ORDER:
            return {
                ...state,
                msg: action.payload
            }
        case GET_ORDERS: 
            return {
                ...state,
                allOrder: action.payload,
            }
        case UPDATE_ORDER:
            return {
                ...state,
                allOrder: state.allOrder.map((o) => (o.id === action.payload.id ? action.payload : o)),
                msg: action.payload
                //posts.map((post) => (post._id === action.payload._id ? action.payload : post));
            }
        case ALL_ORDERS:
            return {
                ...state,
                admin: action.payload
            }
        default: return state;
    }
}

export default order;