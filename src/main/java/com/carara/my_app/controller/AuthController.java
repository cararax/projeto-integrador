package com.carara.my_app.controller;

import com.carara.my_app.model.*;
import com.carara.my_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/signup/caregiver")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody CaregiverDTO caregiverDTO) {
        logger.info("signup: {}", caregiverDTO);
        return ResponseEntity.ok(authService.signup(caregiverDTO));
    }
    @PostMapping("/signup/carereceiver")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody CarerecivierDTO carerecivierDTO) {
        logger.info("signup: {}", carerecivierDTO);
        return ResponseEntity.ok(authService.signup(carerecivierDTO));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody AuthenticationRequest registerRequest) {
        logger.info("signin: {}", registerRequest);
        return ResponseEntity.ok(authService.signin(registerRequest));

    }
}
