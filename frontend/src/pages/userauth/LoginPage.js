import React, { useState } from 'react'
import LoginForm from './LoginForm'
import { Button } from 'react-bootstrap';
import { Application } from '../../components/export';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTimes} from '@fortawesome/free-solid-svg-icons'

const LoginPage = () => {
    const [display, setDisplay] = useState("none");
    
    const toggleDisplay = () => {
        if(display === "none"){
            setDisplay("display")
        }else{
            setDisplay("none")
        }
    }
    
    return (
        <>
            <Button variant="primary" onClick={toggleDisplay}>Sign In</Button>
            <Application.Base display = {display}>
                <Application.Close toggleDisplay={toggleDisplay}><FontAwesomeIcon icon={faTimes} /></Application.Close>
                <LoginForm />
            </Application.Base>
        </>
        
    )
}

export default LoginPage;