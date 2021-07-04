import React from 'react';
import { Jumbotron, Button, Row, Col } from 'react-bootstrap';
import logo from '../images/btc_logo.svg';

const Home = () => {
	// let signupButton = localStorage.getItem('jwtToken') ? null : (
	// 	<Button variant='primary'>
	// 		<a
	// 			href='/signup'
	// 			style={{
	// 				textDecoration: 'none',
	// 				color: 'white',
	// 			}}
	// 		>
	// 			Sign Up
	// 		</a>
	// 	</Button>
	// );
	return (
		<>
			<Jumbotron style={{ minHeight: '450px' }}>
				<Row>
					<Col sm={4}>
						<h1>Welcome to Bitcorner</h1>
						{/* {signupButton} */}
						<br />
						<br />
						<img
							className='btc_logo'
							alt=''
							width='150'
							height='150'
							src={logo}
						/>
						<p></p>
					</Col>
					<Col sm={8}></Col>
				</Row>
			</Jumbotron>
		</>
	);
};

export default Home;
