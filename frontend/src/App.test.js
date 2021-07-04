const request = require('request')
const expect = require("chai").expect
var chai = require('chai'), chaiHttp = require('chai-http');
chai.use(chaiHttp);
var app = 'http://localhost:9000';

describe("returns user", function(){
    it('returns user', function(done){
        request.get({url:'http://localhost:9000/dashboard'},
        function(error, response, body){
                // console.log("response",response);
                expect(response.statusCode).to.equal(200)
            done();
        })
    })
})

describe("check for duplicate email", function(){
    it("get The error from duplicate email in signup", function(done) {
        // Send some Form Data
            chai.request(app)
        .post('/accounts/signup')
        .send({email: "123@gmail.com", psswd: "123"})
        .end(function (err, res) {
            expect(res.statusCode).to.equal(404)
            expect(res.body.msg).to.equal('User already exists.')
        done();
        }).timeout(10000);
    })
})

describe("check for duplicate username", function(){
    it("get The error from duplicate username in signup", function(done) {
        // Send some Form Data
            chai.request(app)
        .post('/accounts/signup')
        .send({username: "chen01", psswd: "123"})
        .end(function (err, res) {
            expect(res.statusCode).to.equal(400)
            expect(res.body.msg).to.equal('Email already exists, please try another one')
        done();
        });
    })
})

describe("accept invitation from group", function(){
    it("accept invitation from group in dashboard", function(done) {
        // Send some Form Data
            chai.request(app)
        .post('/groups/accpet')
        .send({ member: '6060042bf8a35c1a2c45d064', _id: '606573f4d88f4eda2560f7d3' })
        .end(function (err, res) {
            expect(res.statusCode).to.equal(200)
            expect(res.body.msg).to.equal('Accept invitation successfully')
        done();
        });
    })
})

describe("get all recent activity ", function(){
    it("accept invitation from group in dashboard", function(done) {
        // Send some Form Data
            chai.request(app)
        .post('/expense/74')
        .send({ gid: '606447bcc5dae29c011e65e5', name: 'group 14' })
        .end(function (err, res) {
            expect(res.statusCode).to.equal(200)
            expect(res.body.msg).to.equal('Done with getting all expense')
        done();
        });
    })
})


