import React from 'react'
import { Row } from 'react-bootstrap';
import Listing from '../components/Listing';
import Order from "../components/Order"
import { useSelector } from 'react-redux';

const Buy = () => {
    const allOrder = useSelector((state) => state.order ? state.order.allOrder : null)
    
    let lists = allOrder ? allOrder.filter((l) => l.type === "BUY" && l.status === "Open") : null
    return (
        <>
        <Order type="BUY" />
        <div style={{margin: "0 auto"}}>
            <h3 style={{textAlign: "center"}}>Buy History</h3>
            {lists && lists.length === 0 ? <div>There is no buy order available</div>
            :
            <Row>
                {lists && lists.map((item, index) => {
                    return (
                        <Listing key={index} value={item}/>
                    )
                })}
            </Row>
            }
            {!lists ? <div>There is no sell order available</div> : null}
        </div>
        </>
    )
}

export default Buy