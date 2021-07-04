import React, {useState} from 'react'
import { useEffect } from 'react';
import { Form, Button } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { updateToOrder } from '../actions/orderActions';

const Detail = (props) => {
    const dispatch = useDispatch()

    const [sellOrder, setSellOrder] = useState({
        currency: "",
        price: "",
        btc_quantity: "",
        transaction_type: ""
    })

    useEffect(()=>{
        if(props.detail){
            console.log("props.detail", props.detail);
            setSellOrder(props.detail)
            console.log("sellOrder", sellOrder);
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.detail])

    let currencies = ["EUR", "USD", "GBP", "INR", "CNY"]
    let currencyOption = [];
    currencyOption.push(<option value={sellOrder.currency} key={100}>{sellOrder.currency}</option>)
    currencies = currencies.filter((c) => c !== sellOrder.currency)
    // eslint-disable-next-line array-callback-return
    currencies.map((item, index) => {
        currencyOption.push(<option value={item} key={index}>{item}</option>)
    });

    const handleUpdate = async() => {
        delete sellOrder['start_date'];
        if(sellOrder.transaction_type === "Market Order"){
            // setSellOrder({...sellOrder, price: 1000})
            sellOrder['price'] = (parseFloat(localStorage.getItem('price')) * sellOrder.btc_quantity).toString()
        }
        await dispatch(updateToOrder(sellOrder))
        alert("Update order successfully!")
    }

    const cancelOrder = async () => {
        delete sellOrder['start_date'];
        sellOrder['status'] = 'Cancelled'
        console.log("send update", sellOrder);
        await dispatch(updateToOrder(sellOrder))
        // alert("Cancel order successfully!")
    }

    return (
        <>
            <Form>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Amount of BitCoin</Form.Label>
                    <Form.Control value={sellOrder.btc_quantity} onChange={(e) => setSellOrder({ ...sellOrder, btc_quantity: e.target.value })} type="text" placeholder="Enter BTC Amount" />
                </Form.Group>

                <div className="form-group">
                    <label htmlFor="currency">Sell Currency</label>
                    <select
                        name="currency"
                        id="currency"
                        value={sellOrder.currency}
                        className="form-control"
                        onChange={(e) => setSellOrder({...sellOrder, currency: e.target.value})}>
                        {currencyOption}
                    </select>
                </div>
                <p>Sell Type</p>
                <div key="inline-radio" className="mb-3">
                    <Form.Check inline name="group1" value="Market Order" onClick={(e)=> setSellOrder({...sellOrder, transaction_type: e.target.value})} label="Market Order" type="radio" id="inline-radio-1" checked={sellOrder.transaction_type === "Market Order" ? "checked" : null}/>
                    <Form.Check inline name="group1" value="Limit Order" onClick={(e)=> setSellOrder({...sellOrder, transaction_type: e.target.value})} label="Limit Order" type="radio" id="inline-radio-1" checked={sellOrder.transaction_type === "Limit Order" ? "checked" : null}/>
                </div>
                {sellOrder.transaction_type === "Limit Order" ? 
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Limit Order</Form.Label>
                    <Form.Control value={sellOrder.price} onChange={(e) => setSellOrder({ ...sellOrder, price: e.target.value })} type="number" placeholder="Enter Limit Price" />
                </Form.Group> : null}
                <Button variant="primary" onClick={() => handleUpdate()}>
                    Edit
                </Button>
                <Button variant="danger" onClick={() => cancelOrder()} className="ml-3">
                    Cancel
                </Button>
            </Form>
        </>
    );
}

export default Detail