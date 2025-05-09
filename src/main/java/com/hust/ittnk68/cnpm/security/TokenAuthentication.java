package com.hust.ittnk68.cnpm.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication implements Authentication {

    private String token;
    private boolean authenticated = true;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Return authorities if needed, or null if not used
    }

    @Override
    public Object getCredentials() {
        return token; // Return the token as credentials
    }

    @Override
    public Object getDetails() {
        return null; // Return additional details if necessary
    }

    @Override
    public Object getPrincipal() {
        return token; // Return token as principal, or return user information if you have it
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return token; // Or return the username if you have one
    }
}
