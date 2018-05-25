package org.vno.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//TODO: real username:password check

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    Logger logger = Logger.getLogger(this.getClass().getName());

    public CustomAuthenticationProvider() {

    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (! (username.equals("kk")
                && password.equals("112345"))) {
            throw new BadCredentialsException("Bad credentials");
        }

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        return new UsernamePasswordAuthenticationToken(username, password,
                roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        //return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
