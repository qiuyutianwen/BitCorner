package com.bitcorner.bitCorner.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@Entity
@Table(name = "user")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "NICKNAME", unique = true)
    private String nickname;

    @Column
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String passwd;

    @Column(columnDefinition = "boolean default false")
    private Boolean verified;

    @Column(length = 255)
    private String token;

    @Embedded
    private Bank bank;

    @Embedded
    private Address address;

//    @Transient
//    private Boolean google;

    @Transient
    private String googleId;

    public User() {
    }

    public User(Long id, String nickname, String username, String passwd, String token, Boolean verified, Address address, Bank bank) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.passwd = passwd;
        this.address = address;
        this.bank = bank;
        this.verified = verified;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}

