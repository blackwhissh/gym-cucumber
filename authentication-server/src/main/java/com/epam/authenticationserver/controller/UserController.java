package com.epam.authenticationserver.controller;

import com.epam.authenticationserver.config.LogEntryExit;
import com.epam.authenticationserver.dto.LoginDTO;
import com.epam.authenticationserver.dto.RefreshTokenRequest;
import com.epam.authenticationserver.dto.RefreshTokenResponse;
import com.epam.authenticationserver.dto.UserInfoDTO;
import com.epam.authenticationserver.entity.RefreshToken;
import com.epam.authenticationserver.entity.User;
import com.epam.authenticationserver.exception.RefreshTokenNotFoundException;
import com.epam.authenticationserver.repository.UserRepository;
import com.epam.authenticationserver.security.jwt.AuthTokenFilter;
import com.epam.authenticationserver.security.jwt.JwtUtils;
import com.epam.authenticationserver.security.jwt.RefreshTokenService;
import com.epam.authenticationserver.security.jwt.TokenManager;
import com.epam.authenticationserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/v1/user",produces = {"application/JSON", "application/XML"})
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final TokenManager tokenManager;
    private final AuthTokenFilter authTokenFilter;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(TokenManager tokenManager, AuthTokenFilter authTokenFilter, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, UserService userService, UserRepository userRepository) {
        this.tokenManager = tokenManager;
        this.authTokenFilter = authTokenFilter;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/login")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "User Login", description = "This method is used to Log In")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        return userService.authenticate(loginDTO);
    }

    @PutMapping(value = "/change-password")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "Change Current User Password", description = "This method changes User's password and returns new password")
   @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINEE', 'ROLE_TRAINER')")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal String username, @RequestBody UserInfoDTO userInfoDTO) {
        return userService.changePassword(username, userInfoDTO);
    }

    @PatchMapping(value = "/on-off/{username}")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "Activate/Deactivate", description = "This method Activates/Deactivates User")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> onOffTrainee(@PathVariable String username) {
        return userService.activateDeactivate(username);
    }

    @PostMapping(value = "/refresh")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "Refresh Token", description = "This method is used to refresh JWT token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        String refreshToken = request.getRefreshToken();
        String oldActiveToken = authTokenFilter.parseJwt(httpRequest);
        tokenManager.invalidateToken(oldActiveToken);
        return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            String token = jwtUtils.generateJwtToken(user.getUsername());
            return ResponseEntity.ok(new RefreshTokenResponse(token, refreshToken));
        }).orElseThrow(RefreshTokenNotFoundException::new);
    }

    @PostMapping("/logout")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "Log Out", description = "This method is used to Log Out current User")
    public ResponseEntity<?> logoutUser(HttpServletRequest httpRequest) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).orElseThrow(EntityNotFoundException::new);
        String oldActiveToken = authTokenFilter.parseJwt(httpRequest);
        tokenManager.invalidateToken(oldActiveToken);
        Long userId = user.getUserId();
        refreshTokenService.deleteByUserId(userId);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Log out successful");
    }

}
