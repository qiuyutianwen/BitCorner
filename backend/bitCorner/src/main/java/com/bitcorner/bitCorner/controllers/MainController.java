package com.bitcorner.bitCorner.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping("/hehe")
    public String Hello() {
        return "Hello World!";
    }

    //Just trying out the branch/master pull request and how it all works
    //Now let's see if this works with git checkout.

}
