import React, { useState } from 'react'
import { useEffect } from 'react';
import { Row, ListGroup} from 'react-bootstrap';
import { useSelector } from 'react-redux';

const Others = () => {
    const allOrders = useSelector((state) => state.order ? state.order.admin : null)
    const [orders,setOrders] = useState()

    useEffect(() => {
        if(allOrders)
            setOrders(allOrders)
    }, [allOrders])

    return (
        <> 
            <div style={{margin: "0 auto", width: "60%"}}>
                <Row className="justify-content-center">
                    <h4>System Others</h4>
                </Row>
                <ListGroup>
                    {orders ? orders.map((item, index) => {
                        // console.log("item", item);
                        return (
                            <ListGroup.Item>
                                <h6>Order ID: {item.ID}</h6>
                                {item.username} 
                                {item.start_date}<br/>
                                <h6>type: {item.type}</h6>
                                order: {item.transaction_type}<br/>
                                currency: {item.currency}<br/>
                                bitcoin: {item.btc_quantity}<br/>
                                price: {item.price}<br/>
                                status: {item.status}<br/>
                            </ListGroup.Item>
                        )
                    }): null}
                </ListGroup>
            </div>
        </>
    )
}

export default Others