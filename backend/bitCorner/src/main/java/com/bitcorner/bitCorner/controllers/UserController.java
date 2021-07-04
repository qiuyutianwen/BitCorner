package com.bitcorner.bitCorner.controllers;

import com.bitcorner.bitCorner.dto.BalancePatchDTO;
import com.bitcorner.bitCorner.dto.UserPatchDTO;
import com.bitcorner.bitCorner.handlers.AuthHandler;
import com.bitcorner.bitCorner.models.*;
import com.bitcorner.bitCorner.services.UserService;
import com.bitcorner.bitCorner.utility.JWTBuilder;
import com.kastkode.springsandwich.filter.annotation.Before;
import com.kastkode.springsandwich.filter.annotation.BeforeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JWTBuilder jwtbuilder;

    // testing jwt
    @Before(@BeforeElement(AuthHandler.class))
    @GetMapping(value="/jwt" )
    public String test(HttpServletRequest request) {
        System.out.println("in /jwt" + request.getAttribute("username"));
        return "jwt testing is successful";
    }

    @RequestMapping(
            value = "/accounts/login",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody User user , HttpServletRequest request, HttpServletResponse response) {
        System.out.println("login request user " + user);

        // using google login
        if(user.getGoogleId() != null) {
            user.setPasswd(user.getGoogleId());
            User existGoogleUser = userService.getGoogleUser(user.getUsername(), user.getGoogleId(), getSiteURL(request));
            if(existGoogleUser == null){ // if gmail already in used
                User userCreateResponse = userService.createUser(user, getSiteURL(request));
                return new ResponseEntity<>("Account Created. Please check your email for verification.", HttpStatus.CREATED);
            }else{
                // if user not verified
                if(!existGoogleUser.getVerified()){
                    return new ResponseEntity<>("Please first verify your account!", HttpStatus.UNAUTHORIZED);
                }

                // successful google login
                String jwtToken = jwtbuilder.generateToken(user.getUsername(),"pass");
                response.addHeader("Authorization", jwtToken);
                System.out.println("Generated jwt token: " + jwtToken);

                System.out.println("after google login " + existGoogleUser.getNickname());
                return ResponseEntity.ok().body(existGoogleUser);
            }
        }

        // normal login
        else {
            User existUser = userService.getUser(user.getUsername(), user.getPasswd());
            if(!existUser.getVerified()){
                return new ResponseEntity<>("Please first verify your account!", HttpStatus.UNAUTHORIZED);
            }
            System.out.println("after login " + existUser.getNickname());
            String jwtToken = jwtbuilder.generateToken(user.getUsername(),"pass");
            System.out.println("Generated jwt token: " + jwtToken);

            response.addHeader("Authorization", jwtToken);
            return ResponseEntity.ok().body(existUser);
        }
    }


    @RequestMapping(
            value = "/accounts/signup",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody User user,  HttpServletRequest request) {
        System.out.println("request " + user.getUsername() + ' ' + user.getNickname() + ' ' + user.getPasswd());

        if(!user.getNickname().matches("[A-Za-z0-9]+")){
            throw new IllegalArgumentException("Nickname can only include alphanumeric value");
        }

        User userCreateResponse = userService.createUser(user, getSiteURL(request));
        return new ResponseEntity<>(userCreateResponse, HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token,  HttpServletRequest request) {

        if (userService.verifyUser(token)) {
            String loginURL = getSiteURL(request).replace("9000", "3000"); // + "/login";
            return new ResponseEntity<>(
                    String.format("Verified Successfully. <br /><br />Please <a href=\"%s\">Login Now</a>.", loginURL),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Verification Failed", HttpStatus.BAD_REQUEST);
        }
    }

//    @Before(@BeforeElement(AuthHandler.class))
    @RequestMapping(
            value = "/accounts/bank",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> updateBank(@RequestBody UserPatchDTO user) {
//        System.out.println("adding bank for userid " + user.getId());
        System.out.println(user.toString());
        User updateBankResponse = userService.updateBank(user);
        return new ResponseEntity<>(updateBankResponse, HttpStatus.OK);
    }

//    @Before(@BeforeElement(AuthHandler.class))
    @RequestMapping(
            value = "/accounts/balances",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> fetchBalances(@RequestParam String username) { // username = email
        User user = userService.findByUsername(username);
        Balance balance = user.getBank().getBalance();
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

//    @Before(@BeforeElement(AuthHandler.class))
    @RequestMapping(
            value = "/accounts/balance/updatebalance",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateBalances(@RequestBody BalancePatchDTO updateRequest) { // username = email
        System.out.println(updateRequest);
        Balance updatedBalance = userService.updateBalance(updateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedBalance);
    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping(value = "/users")
    public ResponseEntity<?> fetchOrder() {
        MediaType mediaType = MediaType.APPLICATION_JSON;

        List<User> users_username = userService.getUsers();
        List<Map<String, Object>> response = userService.convertUserListToMap(users_username);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }
}
