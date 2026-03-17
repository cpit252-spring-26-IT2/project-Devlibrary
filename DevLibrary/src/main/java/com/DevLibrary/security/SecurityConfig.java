package com.DevLibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     Temporary in-memory users used only to test the login system.

     These hardcoded users will be replaced later with a database-based
     authentication system where users can register and log in using their own accounts.
    */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 = User.withUsername("azzam")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build();

        UserDetails user2 = User.withUsername("hossam")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .defaultSuccessUrl("/home", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }
}