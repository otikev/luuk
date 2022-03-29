package com.elmenture.core.service;

import com.elmenture.core.model.User;
import com.elmenture.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by otikev on 23-Mar-2022
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);//.orElseThrow(() -> new RuntimeException("User not found: " + username));
        if(user == null){
            return null;
        }
        //GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name()); //TODO
        GrantedAuthority authority = new SimpleGrantedAuthority("user");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Arrays.asList(authority));
    }
}