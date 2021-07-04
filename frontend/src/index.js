import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {BrowserRouter as Router} from "react-router-dom"
import 'bootstrap/dist/css/bootstrap.min.css';
import {createStore, applyMiddleware, compose} from 'redux'
import {Provider} from 'react-redux'
import {rootReducer} from './reducers'
import logger from 'redux-logger'
import thunk from 'redux-thunk'
import jwtDecode from 'jwt-decode';
import {setCurrentUser} from './actions/authActions'

const storeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(
  rootReducer,
  storeEnhancers(applyMiddleware(thunk, logger))
);

if(localStorage.jwtToken){
  store.dispatch(setCurrentUser(jwtDecode(localStorage.jwtToken)))
}


ReactDOM.render(
  <Provider store={store}>
    <Router>
      <App />
    </Router>
  </Provider>
  ,
  document.getElementById('root')
)
