package org.vno.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.vno.security.CustomAuthProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthProvider customAuthProvider;

    @Autowired
    public SecurityConfig(
            CustomAuthProvider mongoAuthProvider) {
        this.customAuthProvider = mongoAuthProvider;
        assert null != mongoAuthProvider;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.headers().cacheControl();

        httpSecurity.authorizeRequests()
                .antMatchers("/user/register").permitAll()
                .and().httpBasic();
    }

    @Autowired
    public void configureAuth(
            AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(customAuthProvider);
    }

}
