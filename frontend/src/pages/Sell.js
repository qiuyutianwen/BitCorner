import React from 'react'
import { Row } from 'react-bootstrap';
import Order from '../components/Order'
import Listing from '../components/Listing';
import { useSelector } from 'react-redux';

const Sell = () => {
    const allOrder = useSelector((state) => state.order ? state.order.allOrder : null)

    let lists = allOrder ? allOrder.filter((l) => l.type === "SELL" && l.status === "Open") :null
    return (
        <>
        <div style={{margin: "0 auto"}}>
            <Order type="SELL" />
            <h3 style={{textAlign: "center"}}>Sell History</h3>
            {lists && lists.length === 0 ? <div>There is no sell order available</div>
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

export default Sell