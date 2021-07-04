import React, { useState } from 'react';
import { Button, Row, Col, Form } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { useHistory } from 'react-router';
import { addBank } from '../actions/authActions';

const Bank = () => {
	const [bankInfo, setBankInfo] = useState({
		bank: '',
		number: '',
		name: '',
		currency: 'EUR',
		balance: '',
	});
	// todo: address should be replaced with street and city
	const [addressInfo, setAddressInfo] = useState({
		address: '',
		country: 'Algeria',
		state: '',
		zip: '',
	});

	const dispatch = useDispatch();
	const history = useHistory()

	const onSubmit = async (e) => {
		e.preventDefault();

		// todo change bank structure?
		// ad-hoc fixing right now
		let bankInfoCopy = { ...bankInfo };
		const currencyName = bankInfoCopy.currency.toLowerCase();
		bankInfoCopy.balance = {};
		bankInfoCopy.balance[currencyName] = bankInfo.balance;

		let addressInfoCopy = { ...addressInfo };
		// addressInfoCopy['city'] = 'San Jose'; // hardcode
		addressInfoCopy['street'] = addressInfoCopy.address;
		delete addressInfoCopy.address;

		const userId = localStorage.getItem('id'); // todo get from redux

		const bankData = {
			id: userId,
			bank: bankInfoCopy,
			address: addressInfoCopy,
		};
		// console.log('add bankinfo', bankInfo, addressInfo);
		console.log('add bank', bankData);
		dispatch(addBank(bankData, history));
	};

	const currencies = ['EUR',  "USD", 'GBP', 'INR', 'RMB'];
	let currencyOption = currencies.map((item, index) => {
		return (
			<option value={item} key={index}>
				{item}
			</option>
		);
	});

	const countires = [
		'Algeria',
		'China',
		'Malaysia',
		'The United State',
		'France',
	];
	let countryOption = countires.map((item, index) => {
		return (
			<option value={item} key={index}>
				{item}
			</option>
		);
	});

	const handleChange = (event) => {
		const target = event.target;
		const value = target.value;
		const targetname = target.name;
		console.log(targetname, value);
		if (targetname === 'country') {
			setAddressInfo({ ...addressInfo, [targetname]: value });
		} else {
			setBankInfo({ ...bankInfo, [targetname]: value });
		}
	};

	return (
		<>
			<Row className='justify-content-center'>
				<h4>Add Bank</h4>
			</Row>
			<Row className='justify-content-center'>
				<Form
					onSubmit={onSubmit}
					method='POST'
					style={{ width: '50%' }}
				>
					<h5>Billing address</h5>
					<Form.Group controlId='formBasicAddress'>
						<Form.Label>Address</Form.Label>
						<Form.Control
							type='text'
							placeholder='1234 Main St'
							value={addressInfo.address}
							onChange={(e) =>
								setAddressInfo({
									...addressInfo,
									address: e.target.value,
								})
							}
							required
						/>
					</Form.Group>
					<Row>
						<Col xs={3}>
							<div className='form-group'>
								<label htmlFor='country'>Country</label>
								<select
									name='country'
									id='country'
									value={addressInfo.country}
									className='form-control'
									onChange={handleChange}
								>
									{countryOption}
								</select>
							</div>
						</Col>
						<Col xs={3}>
							<Form.Group controlId='formBasicState'>
								<Form.Label>City</Form.Label>
								<Form.Control
									value={addressInfo.city}
									onChange={(e) =>
										setAddressInfo({
											...addressInfo,
											city: e.target.value,
										})
									}
									type='city'
									placeholder='Cupertino'
									required
								/>
							</Form.Group>
						</Col>
						<Col xs={3}>
							<Form.Group controlId='formBasicState'>
								<Form.Label>State</Form.Label>
								<Form.Control
									value={addressInfo.state}
									onChange={(e) =>
										setAddressInfo({
											...addressInfo,
											state: e.target.value,
										})
									}
									type='state'
									placeholder='CA'
									required
								/>
							</Form.Group>
						</Col>
						<Col xs={3}>
							<Form.Group controlId='formBasicZip'>
								<Form.Label>Zip</Form.Label>
								<Form.Control
									type='zip'
									value={addressInfo.zip}
									onChange={(e) =>
										setAddressInfo({
											...addressInfo,
											zip: e.target.value,
										})
									}
									placeholder='95111'
									required
								/>
							</Form.Group>
						</Col>
					</Row>

					<hr className='my-4'></hr>

					<h5>Payment Information</h5>
					<div className='form-group'>
						<label htmlFor='currency'>Default Currency</label>
						<select
							name='currency'
							id='currency'
							value={bankInfo.currency}
							className='form-control'
							onChange={handleChange}
						>
							{currencyOption}
						</select>
					</div>
					<Form.Group controlId='formBasicBankName'>
						<Form.Label>Bank Name</Form.Label>
						<Form.Control
							value={bankInfo.bank}
							onChange={(e) =>
								setBankInfo({
									...bankInfo,
									bank: e.target.value,
								})
							}
							type='text'
							placeholder='Wells Fargo Bank'
							required
						/>
					</Form.Group>
					<Row>
						<Col>
							<Form.Group controlId='formBasicCardName'>
								<Form.Label>Name On Card</Form.Label>
								<Form.Control
									type='text'
									value={bankInfo.name}
									onChange={(e) =>
										setBankInfo({
											...bankInfo,
											name: e.target.value,
										})
									}
									required
								/>
								<Form.Text>
									Full name as displayed on card
								</Form.Text>
							</Form.Group>
						</Col>
						<Col>
							<Form.Group controlId='formBasicState'>
								<Form.Label>Bank Account Number</Form.Label>
								<Form.Control
									value={bankInfo.number}
									onChange={(e) =>
										setBankInfo({
											...bankInfo,
											number: e.target.value,
										})
									}
									type='text'
									required
								/>
							</Form.Group>
						</Col>
						<Form.Group controlId='formBasicState'>
							<Form.Label>Balance</Form.Label>
							<Form.Control
								value={bankInfo.balance}
								onChange={(e) =>
									setBankInfo({
										...bankInfo,
										balance: e.target.value,
									})
								}
								type='number'
								required
							/>
						</Form.Group>
					</Row>
					<Button
						variant='primary'
						type='submit'
						style={{ width: '100%' }}
					>
						Add Bank
					</Button>
				</Form>
			</Row>
		</>
	);
}

export default Bank;
