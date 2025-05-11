package com.hust.ittnk68.cnpm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception
    {
        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll ()            // hello, world!
                .requestMatchers(new AntPathRequestMatcher("/auth")).permitAll ()
                .requestMatchers(new AntPathRequestMatcher("/bank-transfer")).permitAll ()
                .requestMatchers(new AntPathRequestMatcher("/check-banking")).permitAll ()
                .requestMatchers(new AntPathRequestMatcher("/test")).permitAll ()
                .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll ()
                .anyRequest().authenticated()
            );
        return http.build ();
    }

}
