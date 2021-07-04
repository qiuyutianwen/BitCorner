import React from "react";
import { Navbar, Button } from "react-bootstrap";
import LoginPage from "../pages/userauth/LoginPage";
import LoginNav from "../pages/userauth/LoginNav";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
const Header = () => {
  const isAuthenticated = useSelector((state) => state.auth);

  return (
    <>
      {isAuthenticated.isAuthenticated ? (
        <LoginNav />
      ) : (
        <>
          <Navbar bg="light" variant="dark">
            <Navbar.Brand
              href="/"
              style={{ color: "black" }}
              className="col-md-9 col-sm-6"
            >
              Bitcoiner{" "}
            </Navbar.Brand>
            <div className="col-md-3 col-sm-6">
              <Button variant="link">
                <Link to="/signup">Sign up</Link>
              </Button>
              <LoginPage />
            </div>
          </Navbar>
        </>
      )}
    </>
  );
};

export default Header;
