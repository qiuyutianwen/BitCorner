import React, { useState, useEffect } from 'react';

import { Button, Form } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router';
import { loginUser } from '../../actions/authActions';
import { signInWithGoogle } from '../../utils/firebase.utils';

const LoginForm = () => {
	const [userInfo, setUserInfo] = useState({ username: '', passwd: '' });
	const user = useSelector((state) => state.auth);

	const dispatch = useDispatch();
	const history = useHistory();

	useEffect(() => {
		console.log('user', user);
		if (user && user.msg && user.msg.length > 0) {
			alert(user.msg);
		}
	}, [user]);

	const onSubmit = async (e) => {
		e.preventDefault();
		await dispatch(loginUser(userInfo));
	};

	return (
		<>
			<Form onSubmit={onSubmit} method='POST'>
				<Form.Group controlId='formGridEmailLogin'>
					<Form.Label>Username: </Form.Label>
					<Form.Control
						type='email'
						placeholder='example@gmail.com'
						value={userInfo.username}
						onChange={(e) =>
							setUserInfo({
								...userInfo,
								username: e.target.value,
							})
						}
						required
						name='username'
					/>
				</Form.Group>
				<Form.Text className='text-muted'>
					We'll never share your info with anyone else.
				</Form.Text>

				<Form.Group controlId='formGridPasswordLogin'>
					<Form.Label>Password</Form.Label>
					<Form.Control
						type='password'
						placeholder='Password'
						value={userInfo.passwd}
						onChange={(e) =>
							setUserInfo({ ...userInfo, passwd: e.target.value })
						}
						required
						name='passwd'
					/>
				</Form.Group>
				{/* <Form.Text className="text-muted">
          Haven't have account?
          <Button variant="link">Sign me in!</Button>
        </Form.Text> */}
				<Button
					variant='danger'
					type='submit'
					style={{ width: '250px' }}
				>
					Submit
				</Button>
			</Form>
			<p></p>
			<Button
				variant='primary'
				type='submit'
				style={{ width: '250px' }}
				onClick={() => signInWithGoogle(dispatch, history)}
			>
				Login With Google Account
			</Button>
		</>
	);
};

export default LoginForm;
