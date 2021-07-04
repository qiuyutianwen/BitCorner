import React from 'react'
import { Row } from 'react-bootstrap';
import Listing from '../components/Listing';
import { useSelector } from 'react-redux';

const Fulfilled = () => {
    const allOrder = useSelector((state) => state.order ? state.order.allOrder : null)
    
    let lists = allOrder ? allOrder.filter((l) => l.status === "Fulfilled") : null
    return (
        <>
        <div style={{margin: "0 auto"}}>
            <h3 style={{textAlign: "center"}}>Fulfilled Order</h3>
            {lists && lists.length === 0 ? <div>There is no buy order available</div>
            :
            <Row>
                {lists && lists.map((item, index) => {
                    return (
                        <Listing key={index} value={item} fulfilled={true}/>
                    )
                })}
            </Row>
            }
            {!lists ? <div>There is no sell order available</div> : null}
        </div>
        </>
    )
}

export default Fulfilled