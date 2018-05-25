package org.vno.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.vno.domain.Role;
import org.vno.domain.UserAccount;
import org.vno.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class CustomAuthProvider implements AuthenticationProvider {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public CustomAuthProvider(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserAccount user = userAccountRepository.findByUsername(username);
        if (null == user) {
            throw new BadCredentialsException("Bad credentials");
        }
        if (! (username.equals(user.getUsername())
                && password.equals(user.getPassword()))) {
            throw new BadCredentialsException("Bad credentials");
        }

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (Role r : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(r.name()));
        }
        return new UsernamePasswordAuthenticationToken(username, password,
                roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
