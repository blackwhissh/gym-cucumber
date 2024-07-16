package com.epam.authenticationserver.repository;

import com.epam.authenticationserver.entity.RefreshToken;
import com.epam.authenticationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    int deleteByUser(User user);
}
