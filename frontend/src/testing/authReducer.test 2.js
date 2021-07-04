import auth from '../reducers/auth'
import * as actions from '../actions/authActions'
import { SIGNUP_USER_FAIL } from '../constants/constant'

test('should save loading start indicator when action isLoadingProducts is dispatched given isLoadingProducts is true', () => {
    const msg ="Signup successfully!"
    const action = {type: SIGNUP_USER_FAIL, msg: "Signup successfully!"}
    const state = {
        isAuthenticated: false,
        user: {},
        msg: {}
    };
    const expected = { 
        isAuthenticated: false,
        user: {},
        msg: msg 
    }

  const result = auth(state, action)

  expect(result).toEqual(expected)
})