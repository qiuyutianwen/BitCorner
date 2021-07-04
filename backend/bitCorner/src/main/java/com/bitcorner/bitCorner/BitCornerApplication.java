package com.bitcorner.bitCorner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.kastkode.springsandwich.filter", "com.bitcorner.bitCorner.*"})
@SpringBootApplication
public class BitCornerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitCornerApplication.class, args);
	}

}
