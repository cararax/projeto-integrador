package com.carara.nursenow.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * Extension of Spring Security User class to store additional data.
 */
public class HttpUserDetails extends User {

    public final Long id;

    public HttpUserDetails(final Long id, final String username, final String hash,
                           final Collection<? extends GrantedAuthority> authorities) {
        super(username, hash, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
