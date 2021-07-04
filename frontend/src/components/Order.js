import React, { useState } from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { getFromOrders, send, sendLimit } from '../actions/orderActions';
const Order = (props) => {
	const [show, setShow] = useState(false);
	const handleClose = () => setShow(false);
	const handleShow = () => setShow(true);

	const user = useSelector((state) => (state.auth ? state.auth.user : null));
	const dispatch = useDispatch();

	const [sellOrder, setSellOrder] = useState({
		username: user ? user.username : null,
		amount: '',
		currency: 'EUR',
		orderType: props ? props.type : '',
		price: '',
		transactionType: '',
	});

	const currencies = ['EUR', 'USD', 'GBP', 'INR', 'CNY'];
	let currencyOption = currencies.map((item, index) => {
		return (
			<option value={item} key={index}>
				{item}
			</option>
		);
	});

	const handleChange = (event) => {
		const target = event.target;
		const value =
			target.transactionType === 'checkbox'
				? target.checked
				: target.value;
		setSellOrder({ ...sellOrder, currency: value });
	};

	const onSubmit = async (e) => {
		e.preventDefault();
		console.log('submit sell order', sellOrder);
		if (sellOrder.transaction_type === 'Market Order') {
			setSellOrder({
				...sellOrder,
				price: localStorage.getItem('price'),
			});
			await dispatch(send(sellOrder, user._id));
		} else await dispatch(sendLimit(sellOrder, user._id));
		setShow(false);
		await dispatch(getFromOrders(user.username));
	};

	return (
		<>
			{props.type !== 'BUY' ? (
				<Button
					variant='primary'
					onClick={handleShow}
					style={{ margin: '10px' }}
				>
					Place a Sell Order
				</Button>
			) : (
				<Button
					variant='primary'
					onClick={handleShow}
					style={{ margin: '10px' }}
				>
					Place a Buy Order
				</Button>
			)}

			<Modal show={show} onHide={handleClose}>
				<Modal.Header closeButton>
					{props.type !== 'BUY' ? (
						<Modal.Title>Add Sell Order</Modal.Title>
					) : (
						<Modal.Title>Add Buy Order</Modal.Title>
					)}
				</Modal.Header>

				<Modal.Body>
					<Form onSubmit={onSubmit} method='POST'>
						<Form.Group controlId='formBasicEmail'>
							<Form.Label>Amount of BitCoin</Form.Label>
							<Form.Control
								value={sellOrder.amount}
								onChange={(e) =>
									setSellOrder({
										...sellOrder,
										amount: e.target.value,
									})
								}
								type='number'
								placeholder='Enter amount'
								required
								min='0.00'
								step='0.00000001'
							/>
						</Form.Group>

						<div className='form-group'>
							<label htmlFor='currency'>Sell Currency</label>
							<select
								name='currency'
								id='currency'
								value={sellOrder.currency}
								className='form-control'
								onChange={handleChange}
							>
								{currencyOption}
							</select>
						</div>
						<p>Sell Type</p>
						<div key='inline-radio' className='mb-3'>
							<Form.Check
								inline
								name='group1'
								value='Market Order'
								onClick={(e) =>
									setSellOrder({
										...sellOrder,
										transactionType: e.target.value,
									})
								}
								label='Market Order'
								type='radio'
								id='inline-radio-1'
							/>
							<Form.Check
								inline
								name='group1'
								value='Limit Order'
								onClick={(e) =>
									setSellOrder({
										...sellOrder,
										transactionType: e.target.value,
									})
								}
								label='Limit Order'
								type='radio'
								id='inline-radio-1'
							/>
						</div>
						{sellOrder.transactionType === 'Limit Order' ? (
							<Form.Group controlId='formBasicEmail'>
								<Form.Label>Limit Order</Form.Label>
								<Form.Control
									value={sellOrder.price}
									onChange={(e) =>
										setSellOrder({
											...sellOrder,
											price: e.target.value,
										})
									}
									type='number'
									placeholder='Enter Limit Order'
									required
									presicion={2}
									min='0.00'
									step='0.01'
								/>
							</Form.Group>
						) : null}
						<Button variant='primary' type='submit'>
							Submit
						</Button>
					</Form>
				</Modal.Body>
			</Modal>
		</>
	);
};

export default Order;
