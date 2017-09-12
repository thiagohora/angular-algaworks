package br.com.thiagohora.infrastructure.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/*@EnableWebSecurity*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
/*
        auth.inMemoryAuthentication()
                .withUser("teste")
                .password("123")
                .roles("teste");*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

  /*      http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                    .httpBasic()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .csrf().disable();*/
    }
}
