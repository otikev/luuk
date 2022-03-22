package com.elmenture.core.security;

import com.elmenture.core.model.User;
import com.elmenture.core.repository.UserRepository;
import com.elmenture.core.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Created by otikev on 23-Mar-2022
 */

@Component
public class RestAuthenticationProvider  implements AuthenticationProvider {

    @Autowired
    UserDetailsServiceImpl userService;

    @Autowired
    UserRepository repository;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RestToken restToken = (RestToken) authentication;

        String username = restToken.getUsername();
        String token = restToken.getToken();

        UserDetails userDetails = userService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Email not found.");
        }

        User user = repository.findByAuthToken(token);
        if(user == null){
            throw new BadCredentialsException("Invalid or expired token");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,token,userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return authenticationToken;
    }

    public boolean supports(Class<?> authentication) {
        return true;
    }
}
