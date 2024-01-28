package kz.am.imagehosting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


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
        UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("user22")
                .password("user22")
                .roles("USER")
                .build();
        UserDetails user3 = User.withDefaultPasswordEncoder()
                .username("user33")
                .password("user33")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, user3, admin);
    }
    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\n");
        return hierarchy;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/posts/**").hasRole("USER")
                    .requestMatchers("/collections/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(withDefaults());
        return http.build();
    }
}
