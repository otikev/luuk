package com.elmenture.core.security;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by otikev on 23-Mar-2022
 */

public class RestAuthenticationFilter  extends OncePerRequestFilter {
    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("X-Content-Type-Options","nosniff");//Prevent content sniffing
        response.addHeader("Access-Control-Allow-Origin","*");

        String xAuth = request.getHeader("Luuk-X-Authorization");
        if(xAuth == null){
            filterChain.doFilter(request, response);
            return;
        }

        byte[] decoded = Base64.decode(xAuth.getBytes("utf-8"));
        String authorization = IOUtils.toString(decoded,"utf-8");

        if(!authorization.contains(":") || authorization.split(":").length != 2){
            throw new SecurityException("Invalid Luuk-X-Authorization header");
        }
        String[] splitted = authorization.split(":");
        String email = splitted[0];
        String token = splitted[1];

        Authentication restToken = new RestToken(email,token);
        SecurityContextHolder.getContext().setAuthentication(restToken);
        filterChain.doFilter(request, response);
    }
}