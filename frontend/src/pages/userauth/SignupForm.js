import React, { useState } from "react";
import { Button, Form, Col, Row } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { signupUser } from "../../actions/authActions";
import { useHistory } from "react-router-dom";

const SignupForm = (props) => {
  const [userInfo, setUserInfo] = useState({
    nickname: "",
    username: "",
    passwd: "",
  });
  const dispatch = useDispatch();
  const history = useHistory();

  const onSubmit = async (e) => {
    e.preventDefault();
    console.log("sign up userInfo", userInfo);
    await dispatch(signupUser(userInfo, history))
  };

  return (
    <Row className="justify-content-md-center">
      <Col sm="4"></Col>
      <Col sm="4">
        <Form onSubmit={onSubmit} method="POST">
          <Form.Group controlId="formGridUsername">
            <Form.Label>Nickname</Form.Label>
            <Form.Control
              type="text"
              placeholder="Nickname"
              value={userInfo.nickname}
              onChange={(e) =>
                setUserInfo({ ...userInfo, nickname: e.target.value })
              }
              required="[a-zA-Z]*"
              name="nickname"
            />
            {/* {errors.username && <span>{errors.username}</span>} */}
          </Form.Group>

          <Form.Group controlId="formGridEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              type="email"
              placeholder="example@gmail.com"
              value={userInfo.username}
              onChange={(e) =>
                setUserInfo({ ...userInfo, username: e.target.value })
              }
              required
              name="username"
            />
            {/* {errors.email && <span>{errors.email}</span>} */}
          </Form.Group>

          <Form.Group controlId="formGridPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Password"
              value={userInfo.passwd}
              onChange={(e) =>
                setUserInfo({ ...userInfo, passwd: e.target.value })
              }
              required
              name="passwd"
            />
          </Form.Group>

          <Form.Text className="text-muted">
            Already had account? <Button variant="link">Sign me in!</Button>
          </Form.Text>

          <Form.Group>
            <Button variant="primary" type="submit">
              Sign Up
            </Button>
          </Form.Group>
        </Form>
      </Col>
    </Row>
  );
};

export default SignupForm;
