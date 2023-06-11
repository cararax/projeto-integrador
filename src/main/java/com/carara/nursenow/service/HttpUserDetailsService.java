package com.carara.nursenow.service;

import com.carara.nursenow.domain.Users;
import com.carara.nursenow.model.HttpUserDetails;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.repos.UsersRepository;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class HttpUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public HttpUserDetailsService(final UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public HttpUserDetails loadUserByUsername(final String username) {
        final Users users = usersRepository.findByEmailIgnoreCase(username);
        if (users == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        final List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(ROLE.CAREGIVER.name()));
        return new HttpUserDetails(users.getId(), username, users.getPassword(), roles);
    }

}
