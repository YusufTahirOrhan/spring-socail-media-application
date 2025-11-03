package com.example.social.web.controller;

import com.example.social.domain.entity.User;
import com.example.social.service.AuthService;
import com.example.social.web.dto.AuthDTOs;
import com.example.social.web.dto.UserDTOs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTOs.UserResponse> signup(@RequestBody @Valid AuthDTOs.SignupRequest signupRequest){
        User newUser = authService.signup(signupRequest.username(), signupRequest.password());

        var responseDto = new UserDTOs.UserResponse(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getRole().name(),
                newUser.getCreatedAt().toString()
        );

        return ResponseEntity.created(URI.create("/api/users/" + newUser.getId())).body(responseDto);
    }

    @PostMapping("login")
    public ResponseEntity<AuthDTOs.TokenResponse> login(@RequestBody @Valid AuthDTOs.LoginRequest loginRequest, HttpServletRequest http){
        var response = authService.login(loginRequest.username(), loginRequest.password(),
                http.getHeader("User-Agent"), http.getRemoteAddr());

        return ResponseEntity.ok(new AuthDTOs.TokenResponse(response.accessToken(), response.expiresInSeconds()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader (name = "Authorization", required = false) String authHeader){
        String raw = (authHeader != null &&  authHeader.startsWith("Bearer "))?authHeader.substring(7):null;
        authService.logout(raw);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthDTOs.MeResponse> me(){
        var currentUser = authService.requireCurrent();
        return ResponseEntity.ok(new AuthDTOs.MeResponse(currentUser.id(), currentUser.username(), currentUser.role().name()));
    }
}
