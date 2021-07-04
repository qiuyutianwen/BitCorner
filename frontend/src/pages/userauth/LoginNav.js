import React from "react";
import { Navbar, Nav, NavDropdown, Button } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { getFromBalance, getFromBillTransaction, getFromOrderTransaction, getUsers, logout } from "../../actions/authActions";
import { useHistory } from "react-router-dom";
import { getFromOrders } from "../../actions/orderActions";
import { getBills } from "../../actions/billAction";

const LoginNav = () => {
  const user = useSelector((state) => (state.auth ? state.auth.user : null));
  const dispatch = useDispatch();
  const history = useHistory();

  const dashNav = async (e) => {
    await dispatch(getFromOrders(user.username));
    history.push("/dashboard/board");
  };

  const profile = async () => {
    await dispatch(getFromBalance(user.username))
    history.push("/profile/personal");
  };

  const bank = async () => {
    history.push("/profile/bank");
  };

  const bills = async () => {
    await dispatch(getUsers());
    await dispatch(getBills(user.id))
    await dispatch(getFromBalance(user.username));
    history.push("/profile/bills");
  };

  const report = async () => {
    await dispatch(getFromBillTransaction())
    await dispatch(getFromOrderTransaction())
    history.push("/profile/report");
  };

  return (
    <>
      <Navbar>
        <Navbar.Brand
          href="/"
          style={{ color: "black" }}
          className="col-md-9 col-sm-6"
        >
          BitCorner{" "}
        </Navbar.Brand>
        <Nav className="justify-content-end" activeKey="/home">
          <Nav.Item>
            <Button variant="link" onClick={dashNav}>
              Dashboard
            </Button>
          </Nav.Item>
          <NavDropdown title={user && user.username && user.username.split("@")[0]} id="nav-dropdown">
            
            {user.bank ? <NavDropdown.Item eventKey="4.1" onClick={profile}>
              Profile
            </NavDropdown.Item> : null}
            {user.bank ? null : <NavDropdown.Item eventKey="4.2" onClick={bank}>
              Bank
            </NavDropdown.Item>}
            <NavDropdown.Item eventKey="4.3" onClick={bills}>
              Bills
            </NavDropdown.Item>
            <NavDropdown.Item eventKey="4.4" onClick={report}>
              Report
            </NavDropdown.Item>
            <NavDropdown.Divider />
            <NavDropdown.Item
              eventKey="4.5"
              onClick={() => dispatch(logout(history))}
            >
              Logout
            </NavDropdown.Item>
          </NavDropdown>
        </Nav>
      </Navbar>
    </>
  );
};

export default LoginNav;
