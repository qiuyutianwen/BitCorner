import React from 'react'
import SignupForm from './SignupForm'
import {signupUser} from '../../actions/authActions'

const SignupPage = () => {
    return (
    <div>
       <SignupForm userSignupRequest={signupUser}/>
    </div>
    )
}

export default SignupPage;