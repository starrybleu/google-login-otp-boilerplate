package com.example.auth.config.security;

import javax.servlet.Filter;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final Filter ssoFilter;

    @Bean
    @Profile({"default", "dev", "local"})
    public WebSecurityConfigurerAdapter forDevelopment() {
        return new WebSecurityConfigurerAdapter() {
            protected void configure(HttpSecurity http) throws Exception {
                http.antMatcher("/**")
                        .authorizeRequests()
                        .antMatchers("/", "/favicon.ico", "/login**").permitAll()
                        .antMatchers("/me").access("hasRole('ADMIN')")
                        .anyRequest().authenticated()
                        .and().logout().logoutSuccessUrl("/").permitAll()
                        .and().headers().frameOptions().sameOrigin()
                        .and().csrf().disable()
                        .addFilterBefore(ssoFilter, BasicAuthenticationFilter.class);
            }
        };
    }

}
