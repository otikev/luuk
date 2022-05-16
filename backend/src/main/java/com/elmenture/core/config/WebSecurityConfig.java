package com.elmenture.core.config;

import com.elmenture.core.security.RestAuthenticationFilter;
import com.elmenture.core.security.RestAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RestAuthenticationProvider restAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(restAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/items/open");//for testing
        web.ignoring().antMatchers("/items/search");//for testing
        web.ignoring().antMatchers("/items/with-property");//for testing
        web.ignoring().antMatchers("/");
        web.ignoring().antMatchers("/image/**");
        web.ignoring().antMatchers(HttpMethod.POST, "/auth/facebooksignin");
        web.ignoring().antMatchers(HttpMethod.POST, "/auth/googlesignin");
        web.ignoring().antMatchers(HttpMethod.POST, "/order/payment-confirmed");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable().authorizeRequests()
                .anyRequest().authenticated().and()
                .requestCache().disable();
        http.authenticationProvider(restAuthenticationProvider);
        http.addFilterBefore(new RestAuthenticationFilter(), BasicAuthenticationFilter.class);
    }
}
