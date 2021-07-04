import React, { useState } from 'react'
import { useEffect } from 'react';
import { Tabs, Tab, Row, ListGroup, Col} from 'react-bootstrap';
import { useSelector } from 'react-redux';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css"
import 'react-datepicker/dist/react-datepicker-cssmodules.min.css'

const Report = () => {
    const allBillTransaction = useSelector((state) => state.auth.allBillTransaction ? state.auth.allBillTransaction : null)
    const allOrderTransaction = useSelector((state) => state.auth.allOrderTransaction ? state.auth.allOrderTransaction : null)
    const user = useSelector((state) => state.auth ? state.auth.user : null)

    const [key,setKey] = useState('user')
    const [filtered, setFiltered] = useState([])
    const [transactions,setTransactions] = useState()
    const [billTransactions,setBillTransactions] = useState()
    const [billFiltered, setBillFiltered] = useState([])
    const [search, setSearch] = useState({
        startDate: new Date(Date.now() - 1000 * 60 * 60 * 24 * 10),
        endDate: new Date(Date.now() + 1000 * 60 * 60 * 24 * 10)
    })

    useEffect(() => {
        if(allBillTransaction){
            setBillTransactions(allBillTransaction)
            setBillFiltered(allBillTransaction.filter((t) => t.receiver_id === user.id || t.sender_id === user.id))
        }
        if(allOrderTransaction){
            setTransactions(allOrderTransaction)
            setFiltered(allOrderTransaction.filter((o) => o.buy_id_1 === user.id || o.buy_id_2 === user.id || o.sell_id_1 === user.id || o.sell_id_2 === user.id))
        }
        console.log("check", search.startDate < search.endDate);
        if(search.startDate > search.endDate){
            setSearch({...search, endDate: search.startDate})
            alert("Cannot set the start date after end date")
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [allBillTransaction, allOrderTransaction, search])

    const setSection = (input) => {
        if(input === "user"){
            console.log("inside filter", input);
            const orderAfter = allBillTransaction ? allOrderTransaction.filter((o) => o.buy_id_1 === user.id || o.buy_id_2 === user.id || o.sell_id_1 === user.id || o.sell_id_2 === user.id) :null
            console.log("orderAfter", orderAfter);
            setFiltered(orderAfter)

            const billAfter = billTransactions ? allBillTransaction.filter((t) => t.receiver_id === user.id || t.sender_id === user.id) :null
            console.log("billAfter", billAfter);
            setBillFiltered(billAfter)
        }
        setKey(input)
    }

    return (
        <> 
        <div style={{width: "60%", margin: "0 auto"}}>
            <Row className="justify-content-between">
                <h6>Search: </h6>
                <div>
                    Start: <DatePicker selected={search.startDate} onChange={date => setSearch({...search, startDate: date})} />
                </div>
                <div>
                    End: <DatePicker selected={search.endDate} onChange={date => {setSearch({...search, endDate: date}) }} />
                </div>
            </Row>
            <Tabs
            id="controlled-tab-example"
            activeKey={key}
            onSelect={(k) => setSection(k)}
            style={{margin: "auto 0"}}
            >
                <Tab eventKey="user" title="user">
                    <div style={{margin: "0 auto", width: "100%"}}>
                        <Row className="justify-content-center">
                            <Col>
                                <h4>User Order Report</h4>
                            </Col>
                            <Col>
                                <h4>User Bill Report</h4>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <ListGroup>
                                    {filtered && filtered.length !== 0 ? filtered.filter(t => new Date(t.transaction_date).getTime() > search.startDate.getTime() && new Date(t.transaction_date).getTime() < search.endDate.getTime()).map((item, index) => {
                                        console.log("order item", item);
                                        return (
                                            <ListGroup.Item key={index}>
                                                ID #{item.id}<br/>
                                                <strong>Transaction Price: {item.transaction_price} USD</strong><br/>
                                                Transaction Date: {item.transaction_date}<br/>
                                                {item.MarketMaker_involved==="true" ? 
                                                <>
                                                Involved Type: {item.Involved_type}<br/>Involved Quantity: {item.Involved_quantity}
                                                </> : null}
                                            </ListGroup.Item>
                                        )
                                    }): <div>There is no user order report</div>}
                                </ListGroup>
                            </Col>
                            <Col>
                                <ListGroup>
                                    {billFiltered && billFiltered.length !== 0 ? billFiltered.filter(t => new Date(t.start_date).getTime() > search.startDate.getTime() && new Date(t.start_date).getTime() < search.endDate.getTime()).map((item, index) => {
                                        // console.log("item", item);
                                        return (
                                            <ListGroup.Item key={index}>
                                                ID #{item.id}: <strong>{item.sender_nickname}</strong> sent to <strong>{item.receiver_nickname}</strong><br/>
                                                date: {item.start_date}<br/>
                                                status: {item.status}<br/>
                                                <strong>amount: {item.amount} {item.currency}<br/></strong>
                                                {item.transaction_fee !== 0 ? <p>fee: {item.transaction_fee} {item.transaction_fee_currency_type.toUpperCase()}</p> : null}
                                            </ListGroup.Item>
                                        )
                                    }): <div>There is no user bill report</div>}
                                </ListGroup>
                            </Col>
                        </Row>
                    </div>
                </Tab>
                <Tab eventKey="system" title="system">
                    <div style={{margin: "0 auto", width: "100%"}}>
                        <Row className="justify-content-center">
                            <Col>
                                <h4>System Order Report</h4>
                            </Col>
                            <Col>
                                <h4>System Bill Report</h4>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <ListGroup>
                                    {/* {console.log("transactions",transactions)} */}
                                    {transactions && transactions.length !== 0 ? transactions.filter(t => new Date(t.transaction_date).getTime() > search.startDate.getTime() && new Date(t.transaction_date).getTime() < search.endDate.getTime()).map((item, index) => {
                                        // console.log("item", item);
                                        return (
                                            <ListGroup.Item key={index}>
                                                ID #{item.id}<br/>
                                                <strong>Transaction Price: {item.transaction_price} USD</strong><br/>
                                                Transaction Date: {item.transaction_date}<br/>
                                                {item.MarketMaker_involved==="true" ? 
                                                <>
                                                Involved Type: {item.Involved_type}<br/>Involved Quantity: {item.Involved_quantity}
                                                </> : null}
                                            </ListGroup.Item>
                                        )
                                    }): <div>There is no system order report</div>}
                                </ListGroup>
                            </Col>
                            <Col>
                                <ListGroup>
                                    {billTransactions && billTransactions.length !== 0 ? billTransactions.filter(t => new Date(t.start_date).getTime() > search.startDate.getTime() && new Date(t.start_date).getTime() < search.endDate.getTime()).map((item, index) => {
                                        // console.log("item", item);
                                        return (
                                            <ListGroup.Item key={index}>
                                                ID #{item.id}: <strong>{item.sender_nickname}</strong> sent to <strong>{item.receiver_nickname}</strong><br/>
                                                date: {item.start_date}<br/>
                                                status: {item.status}<br/>
                                                <strong>amount: {item.amount} {item.currency}<br/></strong>
                                                {item.transaction_fee !== 0 ? <p>fee: {item.transaction_fee} {item.transaction_fee_currency_type.toUpperCase()}</p> : null}
                                            </ListGroup.Item>
                                        )
                                    }): <div>There is no system bill report</div>}
                                </ListGroup>
                            </Col>
                        </Row>
                    </div>
                </Tab>
            </Tabs>
        </div>
        </>
    )
}

export default Report