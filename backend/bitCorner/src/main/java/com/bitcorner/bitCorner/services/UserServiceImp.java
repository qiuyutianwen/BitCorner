package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.dto.BalancePatchDTO;
import com.bitcorner.bitCorner.dto.UserPatchDTO;
import com.bitcorner.bitCorner.exception.DataExistsException;
import com.bitcorner.bitCorner.exception.DataNotFoundException;
import com.bitcorner.bitCorner.exception.UnauthorizedOperationException;
import com.bitcorner.bitCorner.models.*;
import com.bitcorner.bitCorner.repositories.UserRepository;
import com.bitcorner.bitCorner.utility.CommonUtil;
import com.bitcorner.bitCorner.utility.EmailSender;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;

@Service
@Slf4j
@Transactional
public class UserServiceImp implements UserService{
//    private final UserRepository userRepository;

    private static final String USER = "user";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user, String siteURL) {
        System.out.println("inside create");
        User existingEntity = null;
        try {
            existingEntity = findByUsernameOrNickname(user.getUsername(), user.getNickname());
        } catch (DataNotFoundException ex) {
            // swallow
        }

        if (Objects.nonNull(existingEntity)) {
            throw new DataExistsException(USER, user.getUsername(), user.getNickname());
        }

        String confirmationToken = RandomString.make(64);
        user.setToken(confirmationToken);
        user.setVerified(false);
        User savedUser = userRepository.save(user);

        sendVerificationEmail(user, siteURL);

        return convertEntity(user);
//        return convertEntity(savedUser);
    }

    @Override
    public User getUser(String username, String passwd) {
        System.out.println("in user service "+ username + " " + passwd);
        User user = userRepository.findByUsernameAndPasswd(username, passwd);
        if(user != null){
            System.out.println("found");
        }else{
            System.out.println("not found");
            throw new DataNotFoundException(USER, username);
        }
        return convertEntity(user);
    }

    @Override
    public User getUserById(long userId){
        Optional<User> entityOptional = userRepository.findById(userId);

        if (!entityOptional.isPresent()) {
            throw new DataNotFoundException(USER, Long.toString(userId));
        }
        return entityOptional.get();
    }

    @Override
    public User getGoogleUser(String username, String googleId, String siteURL) {
        User user = userRepository.findByUsernameAndPasswd(username, googleId);
        if(user != null){
            System.out.println("Google user found");
        }else{
            System.out.println("Google user not found"); // if not exists, sign it up
            return null;
        }
        return convertEntity(user);
    }

    @Override
    public boolean verifyUser(String verificationToken) {
        User user = userRepository.findByToken(verificationToken);

        if(user == null || user.getVerified()){
            return false;
        } else {
            user.setToken(null);
            user.setVerified(true);
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public User updateBank(UserPatchDTO updateBankRequest) {

        User existedUser = getById(updateBankRequest.getId().toString());

        // update bank
        Bank existedBank = existedUser.getBank(); //Objects.isNull(existedUser.getBank()) ? new Bank() : existedUser.getBank();
        Bank requestedBank = updateBankRequest.getBank();
        // set default balance to 0
        if(Objects.isNull(existedBank)) {
            existedBank = initializeBank();
        }
        if(Objects.nonNull(requestedBank)) {
            CommonUtil.copyNonNullProperties(requestedBank, existedBank);
        }

        // update address
        Address existedAddress = Objects.isNull(existedUser.getAddress()) ? new Address() : existedUser.getAddress();
        Address requestedAddress = updateBankRequest.getAddress();
        if(Objects.nonNull(requestedAddress)) {
            CommonUtil.copyNonNullProperties(requestedAddress, existedAddress);
        }

        existedUser.setBank(existedBank);
        existedUser.setAddress(existedAddress);

//        Balance existedBalance = existedUser.getBank().getBalance();
//        Balance requestedBalance = updateBankRequest.getBalance();
//        log.info(requestedBalance.toString());
//        CommonUtil.copyNonNullProperties(requestedBalance, existedUser);
//        existedUser.getBank().setBalance(existedBalance);

        log.info("Saving updated user");
        User updatedUser = userRepository.saveAndFlush(existedUser);

        return convertEntity(updatedUser);
    }



    public User findByUsername(String username) {

        Optional<User> entityOptional = userRepository.findByUsername(username);

        if (!entityOptional.isPresent()) {
            throw new DataNotFoundException(USER, username);
        }

        return entityOptional.get();
    }

    public User findByNickname(String username) {

        Optional<User> entityOptional = userRepository.findByNickname(username);

        if (!entityOptional.isPresent()) {
            throw new DataNotFoundException(USER, username);
        }

        return entityOptional.get();
    }

    @SneakyThrows
    @Override
    public Balance updateBalance(BalancePatchDTO updateRequest) {

        Optional<User> entityOptional = userRepository.findByUsername(updateRequest.getUsername());
        if (!entityOptional.isPresent()) {
            throw new DataNotFoundException(USER, updateRequest.getUsername());
        }

        User existed = entityOptional.get();
        Balance balance = existed.getBank().getBalance();

        // todo: check bank non-null. Enforce add bank before updating balance

        String currency = updateRequest.getCurrency();
        String capCurrency = currency.substring(0, 1).toUpperCase() + currency.substring(1).toLowerCase();
        String getMethodName = "get" + capCurrency;
        String setMethodName = "set" + capCurrency;

        Method getMethod = balance.getClass().getMethod(getMethodName);
        Method setMethod = balance.getClass().getMethod(setMethodName, Double.class);

        double curr = (double)(getMethod.invoke(balance));

        if(updateRequest.getOperation().equals("deposit")){
            double sum = curr + updateRequest.getAmount();
            if(updateRequest.getCurrency().toLowerCase().equals("btc"))
                sum = Math.round(sum * 100000000.0) / 100000000.0;
            else
                sum = Math.round(sum * 100.0) / 100.0;
            setMethod.invoke(balance, sum);
            existed.getBank().setBalance(balance);
        }
        else if(updateRequest.getOperation().equals("withdraw")){
            System.out.println(curr);
            System.out.println(updateRequest.getAmount());
            if(curr < updateRequest.getAmount()){
                throw new UnauthorizedOperationException("You do not have enough balance to withdraw.");
            }
            double diff = curr - updateRequest.getAmount();
            System.out.println(diff);
            if(updateRequest.getCurrency().toLowerCase().equals("btc"))
                diff = Math.round(diff * 100000000.0) / 100000000.0;
            else
                diff = Math.round(diff * 100.0) / 100.0;
            System.out.println(diff);
            setMethod.invoke(balance, diff);
            existed.getBank().setBalance(balance);
        } else {
            throw new IllegalArgumentException("Bad operation.");
        }

        User updatedUser = userRepository.saveAndFlush(existed);
        System.out.println(updatedUser.toString());
        return updatedUser.getBank().getBalance();

    }


    /**
     * Get currency or btc balance for particular user
     *  @param currency : usd, cny, btc ...
     *
     * @param username : user email
     * @return
     */

    @SneakyThrows
    @Override
    public double getBalance(String currency, String username) {

        Optional<User> entityOptional = userRepository.findByUsername(username);
        if (!entityOptional.isPresent()) {
            throw new DataNotFoundException(USER, username);
        }

        Balance balance = entityOptional.get().getBank().getBalance();

        String capCurrency = currency.substring(0, 1).toUpperCase() + currency.substring(1).toLowerCase();
        String getMethodName = "get" + capCurrency;
        Method getMethod = balance.getClass().getMethod(getMethodName);

        return (double)(getMethod.invoke(balance));

    }

    @Override
    public void updateUser(User user){
        userRepository.save(user);
    }

    // old hardcoded set USD
//    public void setBalance(Double amount, String username) {
//
//        Optional<User> entityOptional = userRepository.findByUsername(username);
//
//        if (!entityOptional.isPresent()) {
//            throw new DataNotFoundException(USER, username);
//        }
//        User existed = entityOptional.get();
//        Balance balance = existed.getBank().getBalance();
//
//        existed.getBank().getBalance().setUsd(amount);
//        User updatedUser = userRepository.saveAndFlush(existed);
//    }






    @Override
    public Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("ID", user.getId());
        map.put("username", user.getUsername());
        map.put("nickname", user.getNickname());
        return map;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> convertUserListToMap(List<User> users) {
        List<Map<String, Object>> res = new ArrayList<>();
        for(User user : users){
            res.add(convertUserToMap(user));
        }
        return res;
    }


    private Bank initializeBank() {
        Bank bank = new Bank();
        bank.setBalance(new Balance(0.0,0.0,0.0,0.0,0.0,0.0));
        return bank;
    }

    private void sendVerificationEmail(User user, String siteURL){

        String emailMsg = String.format(
                "Hello %s,\n\nPlease click the link below to verify your registration:\n%s\n\nThank you,\nTeam BitCorner",
                user.getNickname(),
                siteURL + "/verify?token=" + user.getToken()
        );
        emailSender.sendEmail(user.getUsername(), "Please Verify Your BitCorner Registration", emailMsg);
    }


    private User getById(String id){
        Optional<User> entityOptional = userRepository.findById(Long.parseLong(id));

        if (!entityOptional.isPresent()) {
            throw new DataNotFoundException(USER, id);
        }
        return entityOptional.get();
    }

    private User findByUsernameOrNickname(String username, String nickname) {

        Optional<List<User>> entityOptional = userRepository.findByUsernameOrNickname(username, nickname);

        if (!entityOptional.isPresent() || (entityOptional.isPresent() && entityOptional.get().size() == 0)) {
            throw new DataNotFoundException(USER, username);
        }

        return entityOptional.get().get(0);
    }

    private User convertEntity(User entity) {
        User userResponse = new User();
        BeanUtils.copyProperties(entity, userResponse, "passwd", "token");
        return userResponse;
    }
}
