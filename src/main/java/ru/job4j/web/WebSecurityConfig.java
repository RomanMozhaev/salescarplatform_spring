package ru.job4j.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationSuccessWithSessionHandler successHandler = new AuthenticationSuccessWithSessionHandler();
        http.authorizeRequests()
                .antMatchers("/add", "/change", "/upload", "/cabinet").authenticated()
                .antMatchers("/", "/filter", "/registration", "/signin", "/download").permitAll()
                .and()
                .formLogin()
                .successHandler(successHandler)
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
