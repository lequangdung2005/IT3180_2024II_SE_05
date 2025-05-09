package com.hust.ittnk68.cnpm.security;

import com.hust.ittnk68.cnpm.security.Token;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Token
{

    public Token ()
    {

    }

    public String get()
    {
        Authentication authentication = SecurityContextHolder.getContext ().getAuthentication ();
        if (authentication instanceof TokenAuthentication)
        {
                return (String) authentication.getCredentials ();
        }
        else {
                throw new InsufficientAuthenticationException ("Authentication token is required but missing or invalid.");
        }
    }
}
