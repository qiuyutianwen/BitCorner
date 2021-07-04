import React, { useState, useEffect } from "react";
import {
  Button,
  Row,
  Col,
  Form,
  Table,
  InputGroup,
  FormControl,
  ListGroup,
  Modal,
} from "react-bootstrap";
import { faTimes, faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useDispatch, useSelector } from "react-redux";
import { Application } from "../components/export";
import Bill from "../components/Bill";
import {
  updateToBill,
  payABill,
  cancelToBill,
  rejectToBill,
} from "../actions/billAction";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "react-datepicker/dist/react-datepicker-cssmodules.min.css";
import * as api from "../api/index";
import * as constant from "../constants/constant";
import axios from "axios";

const Bills = () => {
  const allUser = useSelector((state) =>
    state.auth ? state.auth.allUser : null
  );
  const user = useSelector((state) => (state.auth ? state.auth.user : null));
  const allBills = useSelector((state) =>
    state.bill ? state.bill.allBill : null
  );
  const userBalance = useSelector((state) =>
    state.auth.balance ? state.auth.balance : null
  );

  const [editDisplay, setEditDisplay] = useState("none");
  const [payDisplay, setPayDisplay] = useState("none");
  const [users, setUsers] = useState("none");
  const [search, setSearch] = useState("");
  const [searchOutput, setSearchOutput] = useState([]);
  const [show, setShow] = useState(false);
  const [name, setName] = useState();
  const [currentCurrency, setCurrentCurrency] = useState("");
  const [view, setView] = useState(false);
  const [bills, setBills] = useState(
    allBills ? allBills.filter((b) => b.status !== "Cancelled") : null
  );
  const [payBill, setPayBill] = useState({ currency: "USD" });

  const [editBill, setEditBill] = useState({
    status: "",
    amount: "",
    description: "",
    due_date: "",
    check: false,
  });

  const handleClose = () => setShow(false);

  const dispatch = useDispatch();

  useEffect(() => {
    if (allUser) setUsers(allUser);
    if (allBills) {
      setBills(allBills);
      console.log("bills inside Bills.js", bills);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [allUser, allBills]);

  const toggleEditDisplay = async (input, check) => {
    if (editDisplay === "none") {
      const { data } = await api.getOneBill(input);
      setEditBill(data);
      setView(check);
      setEditDisplay("display");
    } else {
      setEditDisplay("none");
    }
  };

  const togglePayDisplay = async () => {
    if (payDisplay === "none") {
      setPayDisplay("display");
    } else {
      setPayDisplay("none");
    }
  };

  const searchUser = (name) => {
    console.log("search for", name);
    setSearch(name);
    let selectedUser = users.filter((a) => a.username.includes(name));
    selectedUser = selectedUser.filter((a) => a.nickname !== user.nickname);

    console.log("afterUser", selectedUser);
    setSearchOutput(selectedUser);
  };

  const statusList = ["Cancelled", "Waiting"];
  let statusOption = statusList.map((item, index) => {
    return (
      <option value={item} key={index}>
        {item}
      </option>
    );
  });

  const currencies = ["EUR", "USD", "GBP", "INR", "CNY", "BITCOIN"];
  let currencyOption = currencies.map((item, index) => {
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
    if (targetname === "status") {
      setEditBill({ ...editBill, [targetname]: value });
    } else {
      setPayBill({ ...payBill, currency: value });
      console.log(
        "hehehe",
        currentCurrency + `_USD`,
        payBill.amount,
        `USD_` + payBill.currency
      );
    }
  };

  const detail = (input) => {
    console.log("getting detail of user", input);
    let currentUser = allUser.filter((a) => a.nickname === input);
    setName(currentUser && currentUser.length > 0 ? currentUser[0] : null);
    setShow(true);
    setSearch("");
  };

  const reject = async (id) => {
    console.log("reject", id);
    const { data } = await api.getOneBill(id);
    await dispatch(rejectToBill(data));
    alert("You rejected a bill");
  };

  const pay = async (id) => {
    togglePayDisplay();
    const { data } = await api.getOneBill(id);
    setPayBill(data);
    setCurrentCurrency(data.currency);
  };

  const edit = async () => {
    delete editBill["check"];
    console.log("edit", editBill);
    await dispatch(updateToBill(editBill));
    alert("You updated a bill");
  };

  const cancel = async (id) => {
    const { data } = await api.getOneBill(id);
    await dispatch(cancelToBill(data));
    alert("You cancelled a bill");
  };

  const submitPay = async () => {
    console.log("currentCurrency:", currentCurrency);
    console.log("payBill.currency:", payBill.currency);

    const { data } = await axios.get("http://13.56.168.160:9000/allprice");
    localStorage.setItem("price", data.LTP);
    const LTP = localStorage.getItem("price");
    console.log(LTP);

    if (currentCurrency !== payBill.currency) {
      if (currentCurrency !== "USD" && payBill.currency !== "USD") {
        if (currentCurrency === "BITCOIN") {
          console.log("CASE: BTC");
          payBill["paidAmount"] =
            payBill.amount * LTP * constant[`USD_` + payBill.currency] * 1.05;
          payBill["transaction_fee"] =
            payBill.amount *
            LTP *
            constant[`USD_` + payBill.currency] *
            1.05 *
            0.0001;
        } else if (payBill.currency === "BITCOIN") {
          console.log("CASE: BTC");
          payBill["paidAmount"] =
            ((payBill.amount * constant[currentCurrency + `_USD`]) / LTP) *
            1.05;
          payBill["transaction_fee"] =
            ((payBill.amount * constant[currentCurrency + `_USD`]) / LTP) *
            1.05 *
            0.0001;
        } else {
          console.log("CASE: A");
          payBill["paidAmount"] =
            constant[currentCurrency + `_USD`] *
            payBill.amount *
            constant[`USD_` + payBill.currency];
          payBill["transaction_fee"] =
            constant[currentCurrency + `_USD`] *
            payBill.amount *
            constant[`USD_` + payBill.currency] *
            0.0001;
        }
      } else if (currentCurrency !== "USD" && payBill.currency === "USD") {
        if (currentCurrency === "BITCOIN") {
          console.log("CASE: BTC");
          payBill["paidAmount"] = payBill.amount * LTP * 1.05;
          payBill["transaction_fee"] = payBill.amount * LTP * 1.05 * 0.0001;
        } else {
          console.log("CASE: B");
          payBill["paidAmount"] =
            constant[currentCurrency + `_USD`] * payBill.amount;
          payBill["transaction_fee"] =
            constant[currentCurrency + `_USD`] * payBill.amount * 0.0001;
        }
      } else if (currentCurrency === "USD" && payBill.currency !== "USD") {
        if (payBill.currency === "BITCOIN") {
          console.log("CASE: BTC");
          payBill["paidAmount"] = (payBill.amount / LTP) * 1.05;
          payBill["transaction_fee"] = (payBill.amount / LTP) * 1.05 * 0.0001;
        } else {
          console.log("CASE: C");
          payBill["paidAmount"] =
            constant[`USD_` + payBill.currency] * payBill.amount;
          payBill["transaction_fee"] =
            constant[`USD_` + payBill.currency] * payBill.amount * 0.0001;
        }
      } else {
        console.log("ERROR");
      }
    } else {
      console.log("CASE: D");
      payBill["paidAmount"] = payBill.amount;
      payBill["transaction_fee"] = 0;
    }

    if (payBill.currency === "BITCOIN") {
      payBill["paidType"] = "btc";
    } else payBill["paidType"] = payBill.currency.toLowerCase();

    console.log("payBill", payBill);

    let isAbleToPay = false;
    switch (payBill.paidType) {
      case "usd": {
        if (userBalance.usd > payBill.paidAmount) isAbleToPay = true;
        break;
      }
      case "eur": {
        if (userBalance.eur > payBill.paidAmount) isAbleToPay = true;
        break;
      }
      case "cny": {
        if (userBalance.cny > payBill.paidAmount) isAbleToPay = true;
        break;
      }
      case "gbp": {
        if (userBalance.gbp > payBill.paidAmount) isAbleToPay = true;
        break;
      }
      case "inr": {
        if (userBalance.inr > payBill.paidAmount) isAbleToPay = true;
        break;
      }
      case "btc": {
        if (userBalance.btc > payBill.paidAmount) isAbleToPay = true;
        break;
      }
      default:
        break;
    }

    if (isAbleToPay) {
      await dispatch(
        payABill(
          payBill.id,
          payBill.paidType,
          payBill.paidAmount,
          payBill.transaction_fee
        )
      ); //billId, paidType, paidAmount, transaction_fee
    } else {
      window.alert("You don't have enough fund to pay bill");
    }
  };

  return (
    <>
      <div style={{ width: "60%", margin: "0 auto" }}>
        <div style={{ height: "100px" }}>
          <Form.Group controlId="formBasicDescription">
            <Row>
              <Col>
                <InputGroup className="col-mb-3">
                  <InputGroup.Prepend>
                    <InputGroup.Text>
                      <FontAwesomeIcon icon={faSearch} />
                    </InputGroup.Text>
                  </InputGroup.Prepend>
                  <FormControl
                    placeholder="Send a bill to other user"
                    aria-label="Search"
                    aria-describedby="basic-addon2"
                    value={search}
                    onChange={(e) => searchUser(e.target.value)}
                  />
                </InputGroup>
                <ListGroup style={{ zIndex: 10 }}>
                  {console.log(
                    "before search selectedUser",
                    search,
                    searchOutput
                  )}
                  {search && searchOutput
                    ? // eslint-disable-next-line array-callback-return
                      searchOutput.map((item, index) => {
                        if (item.nickname) {
                          return (
                            <ListGroup.Item
                              className="search-result"
                              action
                              onClick={() => detail(item.nickname)}
                              key={index}
                            >
                              {item.nickname ? item.nickname : item.username}
                            </ListGroup.Item>
                          );
                        }
                      })
                    : null}
                </ListGroup>
              </Col>
            </Row>
          </Form.Group>
        </div>
        <Row className="justify-content-center">
          <h4>Bills</h4>
        </Row>
        <Row>
          {/* {console.log("bills",bills)} */}
          {bills && bills.length !== 0 ? (
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Status</th>
                  <th>Amount</th>
                  <th>Edit</th>
                  <th>Operation</th>
                </tr>
              </thead>
              <tbody>
                {bills.map((item, index) => {
                  return (
                    <tr key={index}>
                      <td>{item.id}</td>
                      <td>{item.status}</td>
                      <td>{item.amount}</td>
                      {item.receiver_id !== user.id &&
                      item.status !== "Cancelled" &&
                      item.status !== "Paid" &&
                      item.status !== "Rejected" ? (
                        <td>
                          <Button
                            variant="link"
                            onClick={() => toggleEditDisplay(item.id, true)}
                            style={{ padding: "0px" }}
                          >
                            Edit
                          </Button>
                        </td>
                      ) : (
                        <td>
                          <Button
                            variant="link"
                            onClick={() => toggleEditDisplay(item.id, false)}
                            style={{ padding: "0px" }}
                          >
                            View
                          </Button>
                        </td>
                      )}
                      {item.sender_id !== user.id &&
                      item.status !== "Paid" &&
                      item.status !== "Rejected" &&
                      item.status !== "Cancelled" &&
                      item.status !== "Overdue" ? (
                        <td>
                          <Button
                            variant="link"
                            onClick={() => reject(item.id)}
                            style={{ padding: "0px" }}
                          >
                            Reject
                          </Button>
                          <Button
                            className="ml-3"
                            variant="link"
                            onClick={() => pay(item.id)}
                            style={{ padding: "0px" }}
                          >
                            Pay
                          </Button>
                        </td>
                      ) : (
                        <td>
                          {item.status === "Cancelled" ||
                          item.status === "Paid" ||
                          item.status === "Rejected" ? (
                            <>None Action</>
                          ) : (
                            <Button
                              variant="link"
                              onClick={() => cancel(item.id)}
                              style={{ padding: "0px" }}
                            >
                              Cancel
                            </Button>
                          )}
                        </td>
                      )}
                    </tr>
                  );
                })}
              </tbody>
            </Table>
          ) : (
            <div>There is no bill available now!</div>
          )}
        </Row>
        <Modal show={show} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Send Bills</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Bill value={name} key={name} />
          </Modal.Body>
        </Modal>
        <Application.Base display={editDisplay}>
          <Application.Close toggleDisplay={toggleEditDisplay}>
            <FontAwesomeIcon icon={faTimes} />
          </Application.Close>
          {/* {console.log("editBill",editBill)} */}
          {view ? (
            <>
              <h6>Edit Bill</h6>
              <div className="form-group">
                <label htmlFor="status">Bill Status</label>
                <select
                  name="status"
                  id="status"
                  value={editBill.status}
                  className="form-control"
                  onChange={handleChange}
                >
                  {statusOption}
                </select>
              </div>
              <Form.Group controlId="formBasicDate">
                <Form.Label>Bill Amount: </Form.Label>
                <Form.Control
                  value={editBill.amount}
                  onChange={(e) =>
                    setEditBill({ ...editBill, amount: e.target.value })
                  }
                  type="text"
                  placeholder=""
                />
              </Form.Group>
              <Form.Group controlId="formBasicDate">
                <Form.Label>Bill Description: </Form.Label>
                <Form.Control
                  value={editBill.description}
                  onChange={(e) =>
                    setEditBill({ ...editBill, description: e.target.value })
                  }
                  type="text"
                  placeholder=""
                />
              </Form.Group>
              <Form.Group controlId="formBasicDate">
                <Form.Label>Due Date: </Form.Label>
                <br />
                <DatePicker
                  selected={editBill.due_date}
                  onChange={(date) =>
                    setEditBill({ ...editBill, due_date: date })
                  }
                />
              </Form.Group>
              <Button variant="primary" onClick={edit}>
                Edit
              </Button>
            </>
          ) : (
            <div>
              <h6>View Bill</h6>
              <Form.Group controlId="formBasicAmount">
                <Form.Label>Bill Status: </Form.Label>
                <Form.Label>{editBill.status}</Form.Label>
              </Form.Group>
              <Form.Group controlId="formBasicAmount">
                <Form.Label>Bill Currency: </Form.Label>
                <Form.Label>{editBill.currency}</Form.Label>
              </Form.Group>
              <Form.Group controlId="formBasicAmount">
                <Form.Label>Bill Amount: </Form.Label>
                <Form.Label>{editBill.amount}</Form.Label>
              </Form.Group>
              <Form.Group controlId="formBasicDescription">
                <Form.Label>Bill Description: </Form.Label>
                <br />
                <Form.Label>{editBill.description}</Form.Label>
              </Form.Group>
              <Form.Group controlId="formBasicDue">
                <Form.Label>Due Date: </Form.Label>
                <br />
                <Form.Label>
                  {Date(editBill.due_date).substring(0, 24)}
                </Form.Label>
              </Form.Group>
            </div>
          )}
        </Application.Base>
        <Application.Base display={payDisplay}>
          <Application.Close toggleDisplay={togglePayDisplay}>
            <FontAwesomeIcon icon={faTimes} />
          </Application.Close>
          <h6>Pay a bill</h6>
          <div className="form-group">
            <Form.Group controlId="formBasicAmount">
              <Form.Label>Bill Currency: </Form.Label>
              <Form.Label>
                {currentCurrency ? currentCurrency : null}
              </Form.Label>
              <br />
              <Form.Label>Bill Amount: </Form.Label>
              <Form.Label>
                {payBill && payBill.amount ? payBill.amount : null}
              </Form.Label>
            </Form.Group>
            <label htmlFor="payCurrency">Pay Currency</label>
            <select
              name="payCurrency"
              id="payCurrency"
              value={payBill && payBill.currency ? payBill.currency : null}
              className="form-control"
              onChange={handleChange}
            >
              {currencyOption}
            </select>
          </div>
          {payBill &&
            (currentCurrency !== payBill.currency ? (
              <Form.Label>
                Fee:{" "}
                {currentCurrency !== "USD"
                  ? payBill.currency === "USD"
                    ? payBill.amount *
                      constant[currentCurrency + `_USD`] *
                      0.0001
                    : constant[currentCurrency + `_USD`] *
                      payBill.amount *
                      constant[`USD_` + payBill.currency] *
                      0.0001
                  : constant[`USD_` + payBill.currency] *
                    payBill.amount *
                    0.0001}{" "}
                (0.01%)
              </Form.Label>
            ) : null)}
          <br />
          <Button variant="primary" onClick={submitPay}>
            Pay
          </Button>
        </Application.Base>
      </div>
    </>
  );
};

export default Bills;
