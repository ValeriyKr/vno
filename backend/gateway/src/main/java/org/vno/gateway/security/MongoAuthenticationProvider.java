package org.vno.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.vno.gateway.bridge.MongoBridge;
import org.vno.gateway.domain.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class MongoAuthenticationProvider implements AuthenticationProvider {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final MongoBridge mongoBridge;

    @Autowired
    public MongoAuthenticationProvider(MongoBridge mongoBridge) {
        this.mongoBridge = mongoBridge;
        assert null != mongoBridge;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserAccount user = mongoBridge.getUserByUsername(username);

        if (! (username.equals(user.getUsername())
                && password.equals(user.getPassword()))) {
            throw new BadCredentialsException("Bad credentials");
        }

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (Long r : user.getRoleIds()) {
            roles.add(new SimpleGrantedAuthority(mongoBridge.getRoleById(r)
                    .getName()));
        }
        return new UsernamePasswordAuthenticationToken(username, password,
                roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        //return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
