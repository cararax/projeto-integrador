package com.carara.my_app.service;


import com.carara.my_app.config.JwtService;
import com.carara.my_app.domain.Caregiver;
import com.carara.my_app.domain.Carerecivier;
import com.carara.my_app.model.*;
import com.carara.my_app.repos.CaregiverRepository;
import com.carara.my_app.repos.CarerecivierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CaregiverRepository caregiverRepository;
    private final CarerecivierRepository carerecivierRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse signup(RegisterRequest registerRequest) {
        var user = Caregiver.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.CAREGIVER)
                .build();
        caregiverRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse signup(CaregiverDTO caregiverDTO) {
        var caregiver = Caregiver.builder()
                .firstname(caregiverDTO.getFirstname())
                .lastname(caregiverDTO.getLastname())
                .password(passwordEncoder.encode(caregiverDTO.getPassword()))
                .email(caregiverDTO.getEmail())
                .role(Role.CAREGIVER)
                .build();
        caregiverRepository.save(caregiver);
        var jwtToken = jwtService.generateToken(caregiver);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse signup(CarerecivierDTO carerecivierDTO) {
        var carerecivier = Carerecivier.builder()
                .firstname(carerecivierDTO.getFirstname())
                .lastname(carerecivierDTO.getLastname())
                .password(passwordEncoder.encode(carerecivierDTO.getPassword()))
                .email(carerecivierDTO.getEmail())
                .role(Role.CAREGIVER)
                .elderyName(carerecivierDTO.getElderyName())
                .elderyBithDate(carerecivierDTO.getElderyBithDate())
                .healthDetails(carerecivierDTO.getHealthDetails())
                .build();
        carerecivierRepository.save(carerecivier);
        var jwtToken = jwtService.generateToken(carerecivier);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse signin(AuthenticationRequest registerRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                ));
        if(caregiverRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            var user = caregiverRepository.findByEmail(registerRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }else{
            var user = carerecivierRepository.findByEmail(registerRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }
}
