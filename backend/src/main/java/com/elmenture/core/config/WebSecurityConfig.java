package com.elmenture.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/all").permitAll()
                .antMatchers("/measurements/update").permitAll()
                .antMatchers("/update-measurements").permitAll()
                .antMatchers("/**","/image/**").permitAll()
                .antMatchers(HttpMethod.POST,"/auth/facebooksignin").permitAll()
                .antMatchers(HttpMethod.POST,"/auth/googlesignin").permitAll()
                .antMatchers(HttpMethod.GET,"/items/all").permitAll()
                .anyRequest().authenticated();
    }
}
