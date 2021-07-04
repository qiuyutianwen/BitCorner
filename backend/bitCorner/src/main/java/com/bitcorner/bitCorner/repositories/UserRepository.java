package com.bitcorner.bitCorner.repositories;

import com.bitcorner.bitCorner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPasswd(String username, String passwd);

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    Optional<List<User>> findByUsernameOrNickname(String username, String nickname);

    User findByToken(String verificationToken);
}