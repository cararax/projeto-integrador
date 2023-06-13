package com.carara.nursenow.service;

import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.RegistrationRequest;
import com.carara.nursenow.repos.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RegistrationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(final UsersRepository usersRepository,
            final PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(final RegistrationRequest registrationRequest) {
        return usersRepository.existsByEmailIgnoreCase(registrationRequest.getEmail());
    }

    public void register(final RegistrationRequest registrationRequest) {
        //todo: adicionar log para cada tipo de usuario
        log.info("registering new user: {}", registrationRequest.getEmail());

        final Users users = new Users();
        users.setFirstname(registrationRequest.getFirstname());
        users.setLastname(registrationRequest.getLastname());
        users.setEmail(registrationRequest.getEmail());
        users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        users.setRole(registrationRequest.getRole());
        users.setDescription(registrationRequest.getDescription());
        users.setElderyName(registrationRequest.getElderyName());
        users.setHealthDetails(registrationRequest.getHealthDetails());
        users.setElderyBirthDate(registrationRequest.getElderyBirthDate());
        usersRepository.save(users);
    }

}
