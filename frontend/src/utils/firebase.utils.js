import firebase from 'firebase/app';
import 'firebase/auth';
import { loginUser } from '../actions/authActions';
var config = {
	apiKey: 'AIzaSyBBeCIYkeLaJ0lUA2KGpNpMlvXwLvNrBo8',
	authDomain: 'fir-demo-fd377.firebaseapp.com',
	databaseURL: 'https://fir-demo-fd377.firebaseio.com',
	projectId: 'fir-demo-fd377',
	storageBucket: 'fir-demo-fd377.appspot.com',
	messagingSenderId: '230297250895',
	appId: '1:230297250895:web:c3cab715836619c6a68e3a',
	measurementId: 'G-G3J8XRHPZS',
};

firebase.initializeApp(config);

export const auth = firebase.auth();

const provider = new firebase.auth.GoogleAuthProvider();
provider.setCustomParameters({ prompt: 'select_account' });
export const signInWithGoogle = (dispatch, history) => {
	console.log('google signin');
	auth.signInWithPopup(provider)
		.then(async (result) => {
			console.log(result.credential.accessToken);
			const user = result.user;
			localStorage.setItem('email', user.email);
			localStorage.setItem('google_id', user.uid);
			let nickname = user.email.split('@')[0];
			nickname = nickname.replace(/[^a-z0-9+]+/gi, '');
			await dispatch(
				loginUser({
					username: user.email,
					// google: true,
					nickname: nickname,
					googleId: user.uid,
				})
			);
			history.push('/');
		})
		.catch((err) => {
			console.log(err);
		});
};

export default firebase;
