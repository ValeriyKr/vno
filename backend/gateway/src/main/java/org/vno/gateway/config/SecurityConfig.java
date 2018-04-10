package org.vno.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.vno.gateway.security.MongoAuthenticationProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MongoAuthenticationProvider mongoAuthenticationProvider;

    @Autowired
    public SecurityConfig(
            MongoAuthenticationProvider mongoAuthenticationProvider) {
        this.mongoAuthenticationProvider = mongoAuthenticationProvider;
        assert null != mongoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.headers().cacheControl();

        httpSecurity.authorizeRequests().anyRequest().authenticated()
                .antMatchers("/login").permitAll()
                .and().httpBasic();
    }

    @Autowired
    public void configureAuthentication(
            AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(mongoAuthenticationProvider);
        //authenticationManagerBuilder.passwordEncoder(passwordEncoder());
    }

}
