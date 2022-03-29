package com.elmenture.core.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Created by otikev on 23-Mar-2022
 */

public class RestToken extends UsernamePasswordAuthenticationToken {

    public RestToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public String getUsername() {
        return (String) getPrincipal();
    }

    public String getToken() {
        return (String) getCredentials();
    }
}
