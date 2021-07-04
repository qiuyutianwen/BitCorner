import React, { useState } from 'react';
import { Form, Button, Row } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { sendToBill } from '../actions/billAction';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'react-datepicker/dist/react-datepicker-cssmodules.min.css';

const Bill = (props) => {
	const dispatch = useDispatch();

	console.log('props.value', props.value);
	const [billInfo, setBillInfo] = useState({
		sender_id: parseInt(localStorage.getItem('id')),
		receiver_id: props.value ? props.value.ID : null,
		currency: 'EUR',
		description: '',
		amount: '',
		status: 'Waiting',
		start_date: new Date(),
		due_date: new Date(),
	});

	const currencies = ['USD', 'EUR', 'GBP', 'INR', 'CNY', 'BITCOIN'];
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
			target.type === 'checkbox' ? target.checked : target.value;
		setBillInfo({ ...billInfo, currency: value });
	};

	const onSubmit = async () => {
		console.log('on submit bill', billInfo);
		if (billInfo.currency === 'BITCOIN') {
			if (billInfo.amount.match('^[0-9]*(.[0-9]{0,8})?$') != null) {
				await dispatch(sendToBill(billInfo));
			} else {
				alert('Please enter amount with at most 8 decimal');
			}
		} else {
			if (billInfo.amount.match('^[0-9]*(.[0-9]{0,2})?$') != null) {
				await dispatch(sendToBill(billInfo));
			} else {
				alert('Please enter amount with at most 2 decimal');
			}
		}
	};

	return (
		<>
			<h6>
				Send To: {props.value.nickname} | {props.value.username}
			</h6>
			<Form.Group controlId='formBasicDescription'>
				<Form.Label>Description: </Form.Label>
				<Form.Control
					value={billInfo.description}
					onChange={(e) =>
						setBillInfo({
							...billInfo,
							description: e.target.value,
						})
					}
					type='text'
					placeholder=''
				/>
			</Form.Group>
			<div className='form-group'>
				<label htmlFor='currency'>Default Currency</label>
				<select
					name='currency'
					id='currency'
					value={billInfo.currency}
					className='form-control'
					onChange={handleChange}
				>
					{currencyOption}
				</select>
			</div>
			<Form.Group controlId='formBasicDate'>
				<Form.Label>Amount: </Form.Label>
				{billInfo.currency === 'BITCOIN' ? (
					<Form.Control
						value={billInfo.amount}
						onChange={(e) =>
							setBillInfo({ ...billInfo, amount: e.target.value })
						}
						type='number'
						placeholder=''
						presicion={8}
						min='0.00'
						step='0.00000001'
					/>
				) : (
					<Form.Control
						value={billInfo.amount}
						onChange={(e) =>
							setBillInfo({ ...billInfo, amount: e.target.value })
						}
						type='number'
						placeholder=''
						presicion={2}
						min='0.00'
						step='0.01'
					/>
				)}
			</Form.Group>
			<Form.Group controlId='formBasicDate'>
				<Form.Label>Due Date: </Form.Label>
				<br />
				<DatePicker
					selected={billInfo.due_date}
					onChange={(date) =>
						setBillInfo({ ...billInfo, due_date: date })
					}
				/>
			</Form.Group>
			<Row>
				<Button
					variant='primary'
					onClick={onSubmit}
					style={{ width: '70%', margin: '0 auto' }}
				>
					Send
				</Button>
			</Row>
		</>
	);
};

export default Bill;
