package kz.am.imagehosting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.withDefaultPasswordEncoder()
                .username("user11")
                .password("user11")
                .roles("USER")
                .build();
        UserDetails user2= User.withDefaultPasswordEncoder()
                .username("user22")
                .password("user22")
                .roles("USER")
                .build();
        UserDetails user3= User.withDefaultPasswordEncoder()
                .username("user33")
                .password("user33")
                .roles("USER")
                .build();
        UserDetails admin= User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, user3, admin);
    }
}
