import React, {useState} from 'react'
import { Button, Col, Card, Modal } from 'react-bootstrap';
import Detail from './Detail';

const Listing = (props) => {
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const item = props.value;
    return (
        <>
        <Col md={4}>
            <Card className="text-center" style={{marginBottom: "10px"}}>
                <Card.Header>{item.type}</Card.Header>
                <Card.Body>
                    <Card.Title>{item.transaction_type}</Card.Title>
                    <Card.Text>
                    <h6>Order ID #{item.id}</h6>
                    <p>Amount of bitcoin:</p><strong>{item.btc_quantity}</strong>
                    </Card.Text>
                    <Card.Text>
                    <p>Currency: <strong>{item.currency}</strong></p>
                    <p>Price: <strong>{item.price}</strong></p>
                    <p>Status: <strong>{item.status}</strong></p>
                    </Card.Text>
                    {props.fulfilled ? null : <Button variant="primary" onClick={handleShow}>
                        Edit
                    </Button>}
                    <Modal show={show} onHide={handleClose}>
                        <Modal.Header closeButton>
                        <Modal.Title>Bill Detail</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <Detail type={item.type} detail={item}/>
                        </Modal.Body>
                    </Modal>
                </Card.Body>
            </Card>
        </Col>
        </>
    )
}

export default Listing