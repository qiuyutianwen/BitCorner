import React, { useEffect, useState } from 'react';
import { Button, Form, Col, Row, Table } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { updateBalance } from '../actions/authActions';

const Profile = () => {
	const user = useSelector((state) => (state.auth ? state.auth.user : null));
	const allBalance = useSelector((state) =>
		state.auth ? state.auth.balance : null
	);
	const [balance, setBalance] = useState();
	const [bitCoin, setBitCoin] = useState();

	useEffect(() => {
		if (allBalance) setBalance(allBalance);
	}, [allBalance]);

	const dispatch = useDispatch();

	const [balanceInfo, setBalanceInfo] = useState({
		username: user.username,
		currency: user.bank ? user.bank.currency.toUpperCase() : 'EUR',
		operation: 'deposit',
		amount: '',
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
			target.type === 'checkbox' ? target.checked : target.value;
		setBalanceInfo({ ...balanceInfo, currency: value });
	};

	const action = async (input) => {
		balanceInfo['operation'] = input;
		console.log('balanceInfo', balanceInfo, input);
		// eslint-disable-next-line no-useless-escape
		if (balanceInfo.amount.match('^[0-9]*(.[0-9]{0,2})?$') != null) {
			await dispatch(updateBalance(balanceInfo));
			setBalanceInfo({ ...balanceInfo, operation: '', amount: '' });
		} else {
			alert('Please enter amount with at most 2 decimal');
		}
	};

	const actionWithBit = async (input) => {
		balanceInfo['operation'] = input;
		let initializeCurrency = balanceInfo.currency;
		balanceInfo['currency'] = 'Btc';
		console.log('balanceInfo', balanceInfo, input);
		// eslint-disable-next-line no-useless-escape
		if (bitCoin.match('^[0-9]*(.[0-9]{0,8})?$') != null) {
			await dispatch(updateBalance({ ...balanceInfo, amount: bitCoin }));
			balanceInfo['currency'] = initializeCurrency;
			setBitCoin('');
		} else {
			alert('Please enter amount with at most 8 decimal');
		}
	};

	return (
		<>
			<div style={{ width: '60%', margin: '0 auto' }}>
				<Row className='justify-content-center'>
					<h4>User Profile</h4>
				</Row>
				<h6>
					Default Currency:{' '}
					<strong>{user.bank ? user.bank.currency : 'NONE'}</strong>
				</h6>
				{/* <h6>Bank Account Balance: <strong>{balance ? balance.}</strong></h6> */}

				<div style={{ width: '50%' }}>
					<Row>
						<Col>
							<div className='form-group'>
								<label htmlFor='currency'>Currency</label>
								<select
									name='currency'
									id='currency'
									value={balanceInfo.currency}
									className='form-control'
									onChange={handleChange}
								>
									{currencyOption}
								</select>
							</div>
						</Col>
						<Col>
							<Form.Group controlId='formAmount'>
								<Form.Label>Amount</Form.Label>
								<Form.Control
									value={balanceInfo.amount}
									onChange={(e) =>
										setBalanceInfo({
											...balanceInfo,
											amount: e.target.value,
										})
									}
									type='number'
									required
									presicion={2}
									min='0.00'
									step='0.01'
									pattern='^[0-9]*\.[0-9]{2}$'
								/>
							</Form.Group>
						</Col>
					</Row>
					<Button
						variant='primary'
						value='withdraw'
						onClick={(e) => action(e.target.value)}
					>
						Withdraw
					</Button>
					<Button
						variant='danger'
						value='deposit'
						onClick={(e) => action(e.target.value)}
						style={{ marginLeft: '10px' }}
					>
						Deposit
					</Button>
					<h6>
						BitCoin Balance:{' '}
						<strong>
							{balance && balance.btc ? balance.btc : 0}
						</strong>
					</h6>
					<Form.Group controlId='formBitcoin'>
						<Form.Label>BitCoin</Form.Label>
						<Form.Control
							value={bitCoin}
							onChange={(e) => setBitCoin(e.target.value)}
							type='number'
							required
							presicion={8}
							min='0.00'
							step='0.00000001'
						/>
					</Form.Group>
					<Button
						variant='primary'
						value='withdraw'
						onClick={(e) => actionWithBit(e.target.value)}
					>
						Withdraw
					</Button>
					<Button
						variant='danger'
						value='deposit'
						onClick={(e) => actionWithBit(e.target.value)}
						style={{ marginLeft: '10px' }}
					>
						Deposit
					</Button>
				</div>
				<p></p>
				<h6>Bank Account Balance In Different Currency Type:</h6>
				<Row>
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Currency</th>
								<th>Balance</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>USD</td>
								<td>
									{balance && balance.usd ? balance.usd : 0}
								</td>
							</tr>
							<tr>
								<td>CNY</td>
								<td>
									{balance && balance.cny ? balance.cny : 0}
								</td>
							</tr>
							<tr>
								<td>INR</td>
								<td>
									{balance && balance.inr ? balance.inr : 0}
								</td>
							</tr>
							<tr>
								<td>EUR</td>
								<td>
									{balance && balance.eur ? balance.eur : 0}
								</td>
							</tr>
							<tr>
								<td>GBP</td>
								<td>
									{balance && balance.gbp ? balance.gbp : 0}
								</td>
							</tr>
							<tr>
								<td>BitCoin</td>
								<td>
									{balance && balance.btc ? balance.btc : 0}
								</td>
							</tr>
						</tbody>
					</Table>
				</Row>
			</div>
		</>
	);
};

export default Profile;
