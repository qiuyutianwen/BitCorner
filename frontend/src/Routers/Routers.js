import React from "react"
import {Route,Switch, Redirect} from "react-router-dom"
import Home from '../pages/Home'
import SignupPage from '../pages/userauth/SignupPage'
import LoginPage from '../pages/userauth/LoginPage'
import Profile from '../pages/Profile'
import Bank from '../pages/Bank'
import Sell from '../pages/Sell'
import Buy from '../pages/Buy'
import Dashboard from '../pages/Dashboard'
import Bills from '../pages/Bills'
import Report from '../pages/Report'
import Orders from '../pages/Orders'

const Routers = () => {
    return(
        <Switch>
            <Route exact path = '/'><Home /></Route>
            <Route path = '/signin'><LoginPage /></Route>
            <Route path = '/signup'><SignupPage /></Route>

            <Route path="/profile/personal" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Profile />
            )} />

            <Route path="/profile/bills" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Bills />
            )} />

            <Route path="/profile/orders" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Orders />
            )} />

            <Route path="/profile/report" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Report />
            )} />

            <Route path="/profile/bank" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Bank />
            )} />

            <Route path="/dashboard/sell" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Sell />
            )} /> 

            <Route path="/dashboard/buy" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Buy />
            )} /> 

            <Route path="/dashboard/board" render={()=>(
                !localStorage.getItem('jwtToken') ? (alert("You can't register if you are logged in!"), (<Redirect to="/"/>)) : <Dashboard />
            )} /> 
            
{/* 
            <Route path = '/profile/personal'><Profile /></Route>
            <Route path = '/profile/bills'><Bills /></Route>
            <Route path = '/profile/orders'><Orders /></Route>
            <Route path = '/profile/report'><Report /></Route>
            <Route path = '/dashboard/sell'><Sell /></Route>
            <Route path = '/dashboard/buy'><Buy /></Route>
            <Route path = '/dashboard/board'><Dashboard /></Route>
            <Route path = '/profile/bank'><Bank/></Route> */}

        </Switch>
    )
}

export default Routers