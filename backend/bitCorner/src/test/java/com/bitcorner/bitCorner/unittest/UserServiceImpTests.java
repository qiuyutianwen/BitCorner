package com.bitcorner.bitCorner.unittest;

import com.bitcorner.bitCorner.exception.DataExistsException;
import com.bitcorner.bitCorner.exception.DataNotFoundException;
import com.bitcorner.bitCorner.models.Balance;
import com.bitcorner.bitCorner.models.Bank;
import com.bitcorner.bitCorner.models.User;
import com.bitcorner.bitCorner.repositories.UserRepository;
import com.bitcorner.bitCorner.services.UserService;
import com.bitcorner.bitCorner.services.UserServiceImp;
import net.bytebuddy.utility.RandomString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;


@RunWith(SpringRunner.class)
public class UserServiceImpTests {

    private static String USERNAME = "username@gmail.com";

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService = new UserServiceImp(userRepository);

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setUsername(USERNAME);
        Bank bank = new Bank();
        bank.setBalance(new Balance(10.0, 20.0, 30.0, 40.0, 50.0, 60.0));
        user.setBank(bank);
        doReturn(Optional.of(user)).when(userRepository).findByUsername(USERNAME);
    }


    @Test
    public void getCurrencyTest(){
        double expectedEurBalance = userService.getBalance("eur", USERNAME);
        assertEquals( 40.0, expectedEurBalance,0);

    }

    @Test
    public void getBtcTest(){
        double expectedBtcBalance = userService.getBalance("btc", USERNAME);
        assertEquals( 60.0, expectedBtcBalance,0);
    }

    @Test
    public void verifyUser_whenNoUserFound_Test(){

        doReturn(null).when(userRepository).findByToken("token1");
        boolean actualResult_whenNoUser = userService.verifyUser("token1");
        assertEquals(false, actualResult_whenNoUser);

        User userWithoutVerified = new User();
        userWithoutVerified.setVerified(false);
        doReturn(userWithoutVerified).when(userRepository).findByToken("token2");
        boolean actualResult_whenNotVerified = userService.verifyUser("token2");
        assertEquals( true, actualResult_whenNotVerified );

        User userVerified = new User();
        userVerified.setVerified(true);
        doReturn(userVerified).when(userRepository).findByToken("token3");
        boolean actualResult_whenVerified = userService.verifyUser("token3");
        assertEquals( false, actualResult_whenVerified );
    }

    @Test
    public void verifyUser_whenUserNotVerified_Test(){

        User userWithoutVerified = new User();
        userWithoutVerified.setVerified(false);
        doReturn(userWithoutVerified).when(userRepository).findByToken("token2");
        boolean actualResult_whenNotVerified = userService.verifyUser("token2");
        assertEquals( true, actualResult_whenNotVerified );

    }

    @Test
    public void verifyUser_whenUserAlreadyVerified_Test(){

        User userVerified = new User();
        userVerified.setVerified(true);
        doReturn(userVerified).when(userRepository).findByToken("token3");
        boolean actualResult_whenVerified = userService.verifyUser("token3");
        assertEquals( false, actualResult_whenVerified );
    }

}

