import React from 'react'
import { Button, Table } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { updateToOrder } from '../actions/orderActions';
    

const Cancelled = () => {
    const allOrder = useSelector((state) => state.order ? state.order.allOrder : null)

    const dispatch = useDispatch()

    const reopen = async (input) => {
        let newInput = Object.assign({}, input);
        newInput['status'] = "Open"
        delete newInput['start_date']
        console.log("reopen", newInput);
        await dispatch(updateToOrder(newInput))
    }
    
    let lists = allOrder ? allOrder.filter((l) => l.status === "Cancelled") : null
    return (
        <>
        {/* <Order type="BUY" /> */}
        {lists && lists.length === 0 ? <div>There is no cancelled order history</div> : 
        <div style={{margin: "0 auto"}}>
            {lists ?
            <Table striped bordered hover>
                <thead>
                    <tr>
                    <th>#</th>
                    <th>Type</th>
                    <th>Price</th>
                    <th>Bitcoin</th>
                    <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {lists.map((l) => {
                        return (
                            <tr>
                                <td>{l.id}</td>
                                <td>{l.type}</td>
                                <td>{l.price}</td>
                                <td>{l.btc_quantity}</td>
                                <td><Button variant="link" style={{padding: "0px"}} onClick={() => reopen(l)}>Activate</Button></td>
                            </tr>
                        )
                    })}
                </tbody>
            </Table>
            :
            <div>There is no cancelled order history</div>
            }
        </div>
        }
        </>
    )
}

export default Cancelled

