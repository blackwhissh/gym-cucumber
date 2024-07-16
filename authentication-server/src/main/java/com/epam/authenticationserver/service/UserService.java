package com.epam.authenticationserver.service;

import com.epam.authenticationserver.config.PasswordConfig;
import com.epam.authenticationserver.dto.JwtResponse;
import com.epam.authenticationserver.dto.LoginDTO;
import com.epam.authenticationserver.dto.UserInfoDTO;
import com.epam.authenticationserver.entity.RefreshToken;
import com.epam.authenticationserver.entity.RoleEnum;
import com.epam.authenticationserver.entity.User;
import com.epam.authenticationserver.exception.*;
import com.epam.authenticationserver.repository.UserRepository;
import com.epam.authenticationserver.security.ApplicationUser;
import com.epam.authenticationserver.security.BruteForceProtector;
import com.epam.authenticationserver.security.jwt.JwtUtils;
import com.epam.authenticationserver.security.jwt.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final BruteForceProtector bruteForceProtector;

    public UserService(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenService refreshTokenService, BruteForceProtector bruteForceProtector) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.bruteForceProtector = bruteForceProtector;
    }

    public ResponseEntity<?> authenticate(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(EntityNotFoundException::new);
        if (!user.getActive()) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("User set to inactive, please contact support", LocalDateTime.now()));
        }
        if (bruteForceProtector.isUserBlocked(loginDTO.getUsername())) {
            throw new UserIsBlockedException();
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()));
        } catch (Exception e) {
            bruteForceProtector.recordFailedLoginAttempt(loginDTO.getUsername());
            throw new AuthenticationErrorException();
        }
        bruteForceProtector.unblockUser(loginDTO.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(applicationUser.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                applicationUser.getId(),
                applicationUser.getUsername(),
                applicationUser.getAuthorities().stream().findFirst()
                        .orElseThrow(NoAuthorityException::new).toString()
        ));
    }

    public ResponseEntity<?> changePassword(@AuthenticationPrincipal String username,
                                            @NonNull UserInfoDTO userInfoDTO) {
        if (userInfoDTO.getNewPassword() == null || userInfoDTO.getNewPassword().length() < 8) {
            throw new WrongPasswordException();
        }
        if (userInfoDTO.getNewPassword().equals(userInfoDTO.getOldPassword())) {
            throw new SamePasswordException();
        }
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        String encryptedNewPass = PasswordConfig.passwordEncoder().encode(userInfoDTO.getNewPassword());
        User userToUpdate = userRepository.findById(user.getUserId()).orElseThrow(EntityNotFoundException::new);
        userToUpdate.setPassword(encryptedNewPass);
        userRepository.save(userToUpdate);
        return ResponseEntity.status(200).body("Password changed successfully");
    }

    public ResponseEntity<?> activateDeactivate(@NonNull String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        user.setActive(!user.getActive());

        userRepository.save(user);
        return ResponseEntity.ok("User activated/deactivated successfully");
    }

    public void saveAdmin() {
        User admin = new User();
        String password = PasswordConfig.passwordEncoder().encode("admin");
        admin.setActive(true);
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setUsername("admin");
        admin.setPassword(password);
        admin.setRole(RoleEnum.ADMIN);
        userRepository.save(admin);
    }
}
