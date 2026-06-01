package kz.am.imagehosting.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsImpl;

    private final DataSource dataSource;

    @Value("${server.servlet.session.cookie.name}")
    private String sessionCookieName;

    @Autowired
    public SecurityConfig(
        UserDetailsImpl userDetailsImpl,
        DataSource dataSource
    ){
        this.userDetailsImpl = userDetailsImpl;
        this.dataSource = dataSource;
        // System.out.println(this.dataSource.toString());
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

        PasswordEncoder pswEnc = passwordEncoder();

        final class UserDetailsCreator {
            UserDetails createUSER(String username) {
                return User.builder()
                .username(username)
                .password(pswEnc.encode(username))
                .roles("USER")
                .build();
            }

            UserDetails createADMIN(String username) {
                return User.builder()
                .username(username)
                .password(pswEnc.encode(username))
                .roles("ADMIN")
                .build();
            }
        }

        UserDetailsCreator userDetailsCreator = new UserDetailsCreator();
        UserDetails user1 = userDetailsCreator.createUSER("user11");
        UserDetails user2 = userDetailsCreator.createUSER("user22");
        UserDetails user3 = userDetailsCreator.createUSER("user33");
        UserDetails admin = userDetailsCreator.createADMIN("admin");

        return new InMemoryUserDetailsManager(user1, user2, user3, admin);
    }

    @Bean
    AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider inMemoryAuthProvider = new DaoAuthenticationProvider();
        inMemoryAuthProvider.setUserDetailsService(userDetailsService);
        inMemoryAuthProvider.setPasswordEncoder(passwordEncoder);

        DaoAuthenticationProvider formAuthProvider = new DaoAuthenticationProvider();
        formAuthProvider.setUserDetailsService(this.userDetailsImpl);
        formAuthProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(inMemoryAuthProvider, formAuthProvider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors
                    .configurationSource(corsConfigurationSource())
                )
                // .csrf(Customizer.withDefaults())
                .csrf((csrf) -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())   
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(authorize -> authorize.
                        requestMatchers(
                                AntPathRequestMatcher.antMatcher("/"),
                                AntPathRequestMatcher.antMatcher("/api/**"),
                                AntPathRequestMatcher.antMatcher("/images/**"),
                                AntPathRequestMatcher.antMatcher("/register"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/posts"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/collections"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/users"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/{username:\\w+}"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/{username:\\w+}/posts"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/{username:\\w+}/posts/{id}"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/{username:\\w+}/posts/{id}/download"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/{username:\\w+}/collections"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/{username:\\w+}/collections/{id}"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/{username:\\w+}/collections/{id}/download")
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
            // .formLogin(withDefaults())
//            .logout(LogoutConfigurer::permitAll);
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout((logout) -> logout
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies(this.sessionCookieName)
                    .logoutSuccessUrl("/")
                    .permitAll()
                )
                ;

        return http.build();
    }
}

