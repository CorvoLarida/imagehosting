package kz.am.imagehosting.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsImpl;
    @Autowired
    public SecurityConfig(UserDetailsImpl userDetailsImpl){
        this.userDetailsImpl = userDetailsImpl;
    }
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\n");
        return hierarchy;
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    InMemoryUserDetailsManager userDetailsService() {
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
    AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider inMemoryAuthProvider = new DaoAuthenticationProvider();
        inMemoryAuthProvider.setUserDetailsService(userDetailsService);
        inMemoryAuthProvider.setPasswordEncoder(passwordEncoder);

        DaoAuthenticationProvider formAuthProvider = new DaoAuthenticationProvider();
        formAuthProvider.setUserDetailsService(userDetailsImpl);
        formAuthProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(inMemoryAuthProvider, formAuthProvider);
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/posts/**").hasRole("USER")
                        .requestMatchers("/{username}/**").hasRole("USER")
                        .requestMatchers("/collections/**").hasRole("ADMIN")
                        .anyRequest().authenticated()

                )
                .httpBasic(withDefaults())
            .formLogin(withDefaults())
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/")
//                        .loginProcessingUrl("/login")
//                        .failureUrl("/login?error=true")
//                        .permitAll()
//                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }
}
