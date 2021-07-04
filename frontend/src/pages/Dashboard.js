import React from 'react'
import { Tabs, Tab } from 'react-bootstrap';
import Sell from './Sell'
import Buy from './Buy'
import Cancelled from './Cancelled';
import Countup from 'react-countup'
import Fulfilled from './Fulfilled';
import axios from 'axios';
import * as constant from '../constants/constant'

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            key: 'Buy',
            start_ltp: 40000,
            start_lbp: 40000,
            start_lap: 40000,
            current_ltp: 0,
            current_lbp: 0,
            current_lap: 0,
        }
    }
    async componentDidMount() {
        try {
            setInterval(async () => {
                const { data } = await axios.get('http://13.56.168.160:9000/allprice');
                let lapPrice;
                let lbpPrice;
                if(data.LAP.currency!== "USD"){
                    lapPrice = (constant[data.LAP.currency + `_USD`]) * parseFloat(data.LAP.price)
                }else{
                    lapPrice = parseFloat(data.LAP.price)
                }
                if(data.LBP.currency!== "USD"){
                    lbpPrice = (constant[data.LBP.currency + `_USD`]) * parseFloat(data.LBP.price)
                }else{
                    lbpPrice = parseFloat(data.LBP.price)
                }
                localStorage.setItem('price', data.LTP)
                this.setState({
                    current_lap: lapPrice,
                    current_lbp: lbpPrice,
                    current_ltp: parseFloat(data.LTP),
                })
            }, 5000);
            
            
        } catch(e) {
        console.log(e);
        }
    }
    render() {
    return (
        <div style={{width: "60%", margin: "0 auto"}}>
            <h6 style={{color: "red"}}>Latest Ask Price: {this.state.current_lap === 0 ? <>Loading..</> : <Countup start={this.state.start_lap} end={this.state.current_lap} duration={1} prefix='$' decimals={2}/>}</h6>
            <h6 style={{color: "green"}}>Last Bid price: {this.state.current_lbp === 0 ? <>Loading...</> : <Countup start={this.state.start_lbp} end={this.state.current_lbp} duration={1} prefix='$' decimals={2}/>}</h6>
            <h6 style={{color: "blue"}}>Latest Transaction Price: {this.state.current_ltp === 0 ? <>Loading...</> : <Countup start={this.state.start_ltp} end={this.state.current_ltp} duration={1} prefix='$' decimals={2}/>}</h6>
            <Tabs
            id="controlled-tab-example"
            activeKey={this.state.key}
            onSelect={(k) => this.setState({key: k})}
            style={{margin: "auto 0"}}
            >
                <Tab eventKey="Buy" title="Buy">
                    <Buy />
                </Tab>
                <Tab eventKey="Sell" title="Sell">
                    <Sell />
                </Tab>
                <Tab eventKey="Cancelled" title="Cancelled">
                    <Cancelled />
                </Tab>
                <Tab eventKey="Fulfilled" title="Fulfilled">
                    <Fulfilled />
                </Tab>
            </Tabs>
        </div>
    )
    }
}


export default Home 
