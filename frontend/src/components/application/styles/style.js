import styled from 'styled-components';
import { Link as ReachRouterLink } from 'react-router-dom';

export const Container = styled.div`
    display :${props => props.display};
    position:fixed;
    width: 100vw;
    height: 100vh;
    background-color: black;
    opacity: 0.2;
    top:0px;
    
    @media (max-width: 800px) {
        width:100%;
        height: 100%;
        top:0px;
        bottom:0px;
        background-color:  initial;
    }

 
`;

export const Error = styled.div`

`;

export const Base = styled.div`
    display: ${props => props.display};
    position:fixed;
    top: 5vh;
    left: 30vw;
    height: 90vh;
    background-color: white;
    border-radius: 5px;
    margin: auto;
    width: 600px;
    opacity:2;
    border: 1px solid #cccccc;
    text-align:center;
    padding: 60px 68px 40px;
    z-index:5;
    @media (max-width: 800px) {
        top: 0;
        left: 0;
        width: 100%;
        
    }

    @media (min-width: 1800px) {
        width: 700px;
        left: 35vw;
    }
    
`;

export const Group = styled.div`
    display: ${props => props.display};
    position:fixed;
    top: 5vh;
    left: 15vw;
    height: 90vh;
    background-color: white;
    border-radius: 5px;
    margin: 0 auto;
    width: 900px;
    opacity:2;
    border: 1px solid #cccccc;
    text-align:center;
    padding: 60px 68px 40px;
    z-index:5;
    @media (max-width: 900px) {
        top: 0;
        left: 0;
        width: 100%;
        
    }

    @media (min-width: 1800px) {
        width: 700px;
        left: 35vw;
    }
    
`;

export const Title = styled.h1`
    color:#3870ff;
    font-size:2rem;


    @media (max-width: 800px) {
        
    }
    @media (min-width: 1800px) {

        font-size:2rem;
    }
    
`;

export const Text = styled.p`

`;
export const InputField = styled.div`

`;

export const TextSmall = styled.h2`
 
`;

export const Link = styled(ReachRouterLink)`
    color: #fff;
    text-decoration: none;
    
    &:hover {
        text-decoration: underline;
    }
`;
export const InputArea = styled.form`
    padding: 3rem;
    display:flex;
    flex-direction:column;
    width:100%;
    margin:0 auto;
    height: 80%;
    align-items: center;
    overflow: ${props => props.Scroll};
    overflow-x: hidden;
    @media (max-width: 800px) {
        width:100%;
      
    }

`
export const Input = styled.input`
    background: #ebeced;
    border: 1px solid #bababa;
    outline:none;
    border-radius: 4px;
    color: black;
    width:300px;
    height: 50px;
    line-height: 50px;
    padding: 5px 20px;
    margin-bottom: 20px;
    
    &:last-of-type {
        margin-bottom: 30px;
    }
    @media (max-width: 800px) {
        width:200px;
        height: 30px;
        
    }
`;

export const TextArea = styled.textarea`
    background: #ebeced;
    border: 1px solid #bababa;
    border-radius: 4px;
    color: black;
    line-height: 30px;
    padding: 5px 20px;
    outline:none;
    width:450px;
    max-height:200px;
    min-height:150px;
    margin: auto 0;
 
`;

export const Submit = styled.button`
    position:absolute;
    background: #e50914;
    border-radius: 4px;
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 12px;
    padding: 16px;
    width:200px;
    color: white;
    cursor: pointer;
    border:none;
    bottom: 5%;
    &:disabled {
        opacity: 0.5;
    }

    &:hover {background-color: #ff8c92}

    &:active {
  background-color: #ff8c92;
  box-shadow: 0 5px #948e8e;
  transform: translateY(4px);
}
    
`;
export const Close = styled.div`
    background: white;
    width:20px;
    height:30px;
    position:absolute;
    top:10px;
    right:20px;
    color: black;
    cursor: pointer;
    font-size:30px;
    z-index:5;
    &:disabled {
        opacity: 0.5;
    }

    
`;

export const Select = styled.select`
    position: relative;
    font-size: 20px;

    background: #d4d4d4;
    color:grey;
    text-shadow:0 1px 0 rgba(0,0,0,0.4);
    padding: 10px 30px;
    margin-bottom: 20px;
    border-radius: 5px;
`

export const Option = styled.option`
    background: white;
    border-radius: 4px;
    border: 0;
    color: black;
    height: 50px;
    line-height: 50px;
    padding: 5px 20px;
    margin-bottom: 20px;
    
    &:last-of-type {
        margin-bottom: 30px;
    }
`;