import * as actions from '../actions/authActions'
import {SET_CURRENT_USER} from '../constants/constant'

test('should dispatch setCurrentUser action with fetched user info', () => {
  const user = []
  const expected = {
    type: SET_CURRENT_USER,
    user
  }
  
  const result = actions.setCurrentUser(user)

  expect(result).toEqual(expected)
})
