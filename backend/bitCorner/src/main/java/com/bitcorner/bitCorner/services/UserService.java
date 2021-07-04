package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.dto.BalancePatchDTO;
import com.bitcorner.bitCorner.dto.UserPatchDTO;
import com.bitcorner.bitCorner.models.Balance;
import com.bitcorner.bitCorner.models.User;

import java.util.List;
import java.util.Map;


public interface UserService {
    User createUser(User user, String siteURL);

    User updateBank(UserPatchDTO user);

    User getUser(String username, String passwd);

    User getGoogleUser(String username, String googleId, String siteURL);

    boolean verifyUser(String verificationToken);

    Map<String, Object> convertUserToMap(User user);

    List<User> getUsers();

    List<Map<String, Object>> convertUserListToMap(List<User> users);

    User findByUsername(String username);

    Balance updateBalance(BalancePatchDTO updateRequest);

    double getBalance(String currency, String username);

    void updateUser(User user);

    User getUserById(long userId);
}
